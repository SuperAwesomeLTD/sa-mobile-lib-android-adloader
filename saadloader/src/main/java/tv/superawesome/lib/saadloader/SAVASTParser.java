package tv.superawesome.lib.saadloader;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import tv.superawesome.lib.sajsonparser.SAJsonParser;
import tv.superawesome.lib.samodelspace.SAAd;
import tv.superawesome.lib.samodelspace.SACreative;
import tv.superawesome.lib.samodelspace.SADetails;
import tv.superawesome.lib.samodelspace.SAMedia;
import tv.superawesome.lib.samodelspace.SATracking;
import tv.superawesome.lib.samodelspace.SAVASTAdType;
import tv.superawesome.lib.sanetwork.file.SAFileDownloader;
import tv.superawesome.lib.sanetwork.file.SAFileDownloaderInterface;
import tv.superawesome.lib.sanetwork.request.SANetwork;
import tv.superawesome.lib.sanetwork.request.SANetworkInterface;
import tv.superawesome.lib.sasession.SASession;
import tv.superawesome.lib.sautils.SAUtils;

/**
 * Created by gabriel.coman on 25/08/16.
 */
public class SAVASTParser {

    private Context context = null;
    private SAUtils.SAConnectionType connectionType = SAUtils.SAConnectionType.unknown;

    public SAVASTParser (Context context) {
        this.context = context;
    }

    /**
     * The main parse function of the parser
     * @param url - URL where the VAST resides
     * @param session - the current session
     * @param listener - a SAVASTParserInterface listener object
     */
    public void parseVASTAds(final String url, final SASession session, final SAVASTParserInterface listener) {

        // get the current connectivity status
        connectionType = session.getConnectionType();

        // parse the current vast
        this.parseVAST(url, new SAVASTParserInterface() {
            @Override
            public void didParseVAST(final SAAd ad) {
                if (ad.creative.details.media != null) {

                    SAFileDownloader.getInstance().downloadFileFrom(context, ad.creative.details.media.playableMediaUrl, new SAFileDownloaderInterface() {
                        @Override
                        public void response(boolean success, String diskUrl) {
                            ad.creative.details.media.isOnDisk = success;
                            ad.creative.details.media.playableDiskUrl = diskUrl;
                            listener.didParseVAST(ad);
                        }
                    });

                } else {
                    listener.didParseVAST(ad);
                }
            }
        });
    }

    /**
     * The main parseing function
     * @param url - URL to the VAST
     * @return an array of VAST ads
     */
    private void parseVAST(String url, final SAVASTParserInterface listener) {

        /** step 1: get the XML */
        JSONObject header = SAJsonParser.newObject(new Object[]{
                "Content-Type", "application/json",
                "User-Agent", SAUtils.getUserAgent(context)
        });

        final SANetwork network = new SANetwork();
        network.sendGET(context, url, new JSONObject(), header, new SANetworkInterface() {
            @Override
            public void response(int status, String VAST, boolean success) {
                SAAd empty = new SAAd();

                if (!success) {
                    listener.didParseVAST(empty);
                } else {
                    Document doc;

                    try {
                        doc = SAXMLParser.parseXML(VAST);
                        final Element root = (Element) doc.getElementsByTagName("VAST").item(0);

                        if (root == null) {
                            listener.didParseVAST(empty);
                            return;
                        }
                        if (!SAXMLParser.checkSiblingsAndChildrenOf(root, "Ad")) {
                            listener.didParseVAST(empty);
                            return;
                        }

                        // get the proper vast ad
                        Element adXML = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(root, "Ad");
                        final SAAd ad = parseAdXML(adXML);

                        // inline case
                        if (ad.vastType == SAVASTAdType.InLine) {
                            listener.didParseVAST(ad);
                        }
                        // wrapper case
                        else if (ad.vastType == SAVASTAdType.Wrapper) {
                            parseVAST(ad.vastRedirect, new SAVASTParserInterface() {
                                @Override
                                public void didParseVAST(SAAd wrapper) {
                                    ad.sumAd(wrapper);
                                    listener.didParseVAST(ad);
                                }
                            });
                        }
                        // some other invalid case
                        else {
                            listener.didParseVAST(empty);
                        }
                    } catch (ParserConfigurationException | IOException | SAXException e) {
                        Log.d("SuperAwesome", "VAST parsing error " + e.getMessage());
                        listener.didParseVAST(empty);
                    }
                }
            }
        });
    }

    /**
     * Function that parses a VAST XML "Ad" element into a SAVASTAd model
     * @param adElement XML element
     * @return a SAAd object
     */
    private SAAd parseAdXML(Element adElement) {
        final SAAd ad = new SAAd();

        ad.error = 0;
        ad.isVAST = true;
        ad.vastType = SAVASTAdType.InLine;

        // init arrays of errors & impressions
        final List<SATracking> errors = new ArrayList<>();
        List<SATracking> impressions = new ArrayList<>();

        // check ad type
        ad.vastType = SAVASTAdType.Invalid;
        boolean isInLine = SAXMLParser.checkSiblingsAndChildrenOf(adElement, "InLine");
        boolean isWrapper = SAXMLParser.checkSiblingsAndChildrenOf(adElement, "Wrapper");

        if (isInLine) ad.vastType = SAVASTAdType.InLine;
        if (isWrapper) ad.vastType= SAVASTAdType.Wrapper;

        Element vastUri = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(adElement, "VASTAdTagURI");
        if (vastUri != null) {
            ad.vastRedirect = vastUri.getTextContent();
        }

        // get errors
        SAXMLParser.searchSiblingsAndChildrenOf(adElement, "Error", new SAXMLParser.SAXMLIterator() {
            @Override
            public void foundElement(Element e) {
                SATracking tracking = new SATracking();
                tracking.event = "error";
                tracking.URL = e.getTextContent();
                errors.add(tracking);
            }
        });

        // get impressions
        SAXMLParser.searchSiblingsAndChildrenOf(adElement, "Impression", new SAXMLParser.SAXMLIterator() {
            @Override
            public void foundElement(Element e) {
                SATracking tracking = new SATracking();
                tracking.event = "impression";
                tracking.URL = e.getTextContent();
                errors.add(tracking);
            }
        });

        Element creativeXML = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(adElement, "Creative");
        ad.creative = parseCreativeXML(creativeXML);

        ad.creative.events.addAll(errors);
        ad.creative.events.addAll(impressions);

        return ad;
    }

    /**
     * Function that parses a XML "Linear" VAST element and returns a SAVASTLinearCreative model
     * @param element a XML element
     * @return a valid SACreative model
     */
    private SACreative parseCreativeXML (Element element) {

        final SACreative creative = new SACreative();
        creative.events = new ArrayList<>();

        SAXMLParser.searchSiblingsAndChildrenOf(element, "ClickThrough", new SAXMLParser.SAXMLIterator() {
            @Override
            public void foundElement(Element e) {
                SATracking tracking = new SATracking();
                tracking.event = "click_through";
                tracking.URL = e.getTextContent().replace("&amp;", "&").replace("%3A", ":").replace("%2F", "/");
                creative.events.add(tracking);
            }
        });

        SAXMLParser.searchSiblingsAndChildrenOf(element, "ClickTracking", new SAXMLParser.SAXMLIterator() {
            @Override
            public void foundElement(Element e) {
                SATracking tracking = new SATracking();
                tracking.event = "click_tracking";
                tracking.URL = e.getTextContent();
                creative.events.add(tracking);
            }
        });

        SAXMLParser.searchSiblingsAndChildrenOf(element, "CustomClicks", new SAXMLParser.SAXMLIterator() {
            @Override
            public void foundElement(Element e) {
                SATracking tracking = new SATracking();
                tracking.event = "custom_clicks";
                tracking.URL = e.getTextContent();
                creative.events.add(tracking);
            }
        });

        SAXMLParser.searchSiblingsAndChildrenOf(element, "Tracking", new SAXMLParser.SAXMLIterator() {
            @Override
            public void foundElement(Element e) {
                SATracking tracking = new SATracking();
                tracking.event = e.getAttribute("event");
                tracking.URL = e.getTextContent();
                creative.events.add(tracking);
            }
        });

        creative.details = new SADetails();

        final List<SAMedia> mediaFiles = new ArrayList<>();
        final SAMedia[] defaultMedia = {null};

        SAXMLParser.searchSiblingsAndChildrenOf(element, "MediaFile", new SAXMLParser.SAXMLIterator() {
            @Override
            public void foundElement(Element e) {
                SAMedia media = parseMediaXML(e);
                if (media.type.contains("mp4") || media.type.contains(".mp4")) {
                    mediaFiles.add(media);
                    defaultMedia[0] = media;
                }
            }
        });

        if (mediaFiles.size() >= 1 && defaultMedia[0] != null) {
            // get the videos at different bit rates
            List<SAMedia> bitrate360 = new ArrayList<>();
            for (SAMedia m : mediaFiles) {
                if (m.bitrate == 360) {
                    bitrate360.add(m);
                }
            }
            List<SAMedia> bitrate540 = new ArrayList<>();
            for (SAMedia m : mediaFiles) {
                if (m.bitrate == 540) {
                    bitrate540.add(m);
                }
            }
            List<SAMedia> bitrate720 = new ArrayList<>();
            for (SAMedia m : mediaFiles) {
                if (m.bitrate == 720) {
                    bitrate720.add(m);
                }
            }

            SAMedia media360 = bitrate360.size() >= 1 ? bitrate360.get(0) : null;
            SAMedia media540 = bitrate540.size() >= 1 ? bitrate540.get(0) : null;
            SAMedia media720 = bitrate720.size() >= 1 ? bitrate720.get(0) : null;

            // when connection is:
            //  1) cellular unknown
            //  2) 2g
            // try to get the lowest media possible
            if (connectionType == SAUtils.SAConnectionType.cellular_unknown ||
                connectionType == SAUtils.SAConnectionType.cellular_2g) {
                creative.details.media = media360;
            }
            // when connection is:
            //  1) 3g
            // try to get the medium media
            else if (connectionType == SAUtils.SAConnectionType.cellular_3g) {
                creative.details.media = media540;
            }
            // when connection is:
            //  1) unknown
            //  2) 4g
            //  3) wifi
            //  4) ethernet
            // try to get the best media available
            else {
                creative.details.media = media720;
            }
        }

        // if somehow no media was added (because of legacy VAST)
        // then just add the default media (which should be the 720 one)
        if (creative.details.media == null) {
            creative.details.media = defaultMedia[0];
        }

        return creative;
    }

    /**
     * Parse a media element
     * @param element the source XML element
     * @return a SAMedia object
     */
    private static SAMedia parseMediaXML(Element element) {
        SAMedia media = new SAMedia();
        media.type = element.getAttribute("type");
        media.playableMediaUrl = element.getTextContent().replace(" ", "");
        String bitstr = element.getAttribute("bitrate");
        if (bitstr != null) {
            try {
                media.bitrate = Integer.parseInt(bitstr);
            } catch (NumberFormatException e) {
                // do nothing
            }
        }
        media.playableDiskUrl = null;
        return media;
    }

}
