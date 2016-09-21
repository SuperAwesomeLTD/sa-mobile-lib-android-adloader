package tv.superawesome.lib.saadloader;

import android.content.Context;

import org.json.JSONObject;

import tv.superawesome.lib.sajsonparser.SAJsonParser;
import tv.superawesome.lib.samodelspace.SAAd;
import tv.superawesome.lib.samodelspace.SACreativeFormat;
import tv.superawesome.lib.samodelspace.SAMedia;
import tv.superawesome.lib.sanetwork.request.SANetwork;
import tv.superawesome.lib.sanetwork.request.SANetworkInterface;
import tv.superawesome.lib.sasession.SASession;

/**
 * This class gathers all the other parts of the "data" package and unifies the whole loading
 * experience for the user
 */
public class SALoader {

    private Context context = null;

    public SALoader (Context context) {
        this.context = context;
    }

    /**
     * the function that actually loads the Ad
     * @param placementId - the placement ID a user might want to preload an Ad for
     * @param session - the current session that helps w/ formatting URLs
     * @param listener - a reference to the listener
     */
    public void loadAd(final int placementId, final SASession session, final SALoaderInterface listener){

        // create a local listener to avoid the "chain of listeners"
        final SALoaderInterface localListener = listener != null ? listener : new SALoaderInterface() { @Override public void didLoadAd(SAAd ad) {} };

        // form the endpoint
        final String endpoint = session.getBaseUrl() + "/ad/" + placementId;

        JSONObject query = SAJsonParser.newObject(new Object[]{
                "test", session.getTestMode(),
                "sdkVersion", session.getVersion(),
                "rnd", session.getCachebuster(),
                "bundle", session.getPackageName(),
                "name", session.getAppName(),
                "dauid", session.getDauId(),
                "ct", session.getConnectionType(),
                "lang", session.getLang(),
                "device", session.getDevice()
        });

        JSONObject header = SAJsonParser.newObject(new Object[]{
                "Content-Type", "application/json",
                "User-Agent", session.getUserAgent()
        });

        SANetwork network = new SANetwork();
        network.sendGET(context, endpoint, query, header, new SANetworkInterface() {
            @Override
            public void response(int status, String data, boolean success) {
                if (!success || data == null) {
                    localListener.didLoadAd(null);
                } else {
                    // get data
                    JSONObject dataJson = SAJsonParser.newObject(data);
                    SAAdParser adParser = new SAAdParser();
                    final SAAd ad = adParser.parseInitialAdDataFromNetwork(dataJson, session, placementId);

                    if (ad != null) {

                        SACreativeFormat type = ad.creative.creativeFormat;

                        switch (type) {
                            case invalid:
                            case image:
                            case rich:
                            case tag: {
                                ad.creative.details.media = new SAMedia();
                                ad.creative.details.media.html = SAHTMLParser.formatCreativeDataIntoAdHTML(ad);
                                localListener.didLoadAd(ad);
                                break;
                            }
                            case video: {
                                SAVASTParser parser = new SAVASTParser(context);
                                parser.parseVASTAds(ad.creative.details.vast, new SAVASTParserInterface() {
                                    @Override
                                    public void didParseVAST(SAAd vastAd) {
                                        ad.sumAd(vastAd);
                                        localListener.didLoadAd(ad);
                                    }
                                });
                                break;
                            }
                        }
                    } else {
                        localListener.didLoadAd(null);
                    }
                }
            }
        });
    }
}
