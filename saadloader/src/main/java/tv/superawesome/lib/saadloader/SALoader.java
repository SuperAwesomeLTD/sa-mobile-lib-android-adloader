/**
 * @Copyright:   SuperAwesome Trading Limited 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package tv.superawesome.lib.saadloader;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tv.superawesome.lib.saadloader.postprocessor.SAProcessEvents;
import tv.superawesome.lib.saadloader.postprocessor.SAProcessHTML;
import tv.superawesome.lib.sajsonparser.SAJsonParser;
import tv.superawesome.lib.samodelspace.SAAd;
import tv.superawesome.lib.samodelspace.SACreativeFormat;
import tv.superawesome.lib.samodelspace.SAResponse;
import tv.superawesome.lib.samodelspace.SAVASTAd;
import tv.superawesome.lib.sanetwork.file.SAFileDownloader;
import tv.superawesome.lib.sanetwork.file.SAFileDownloaderInterface;
import tv.superawesome.lib.sanetwork.listdownload.SAFileListDownloader;
import tv.superawesome.lib.sanetwork.listdownload.SAFileListDownloaderInterface;
import tv.superawesome.lib.sanetwork.request.SANetwork;
import tv.superawesome.lib.sanetwork.request.SANetworkInterface;
import tv.superawesome.lib.sasession.SASession;
import tv.superawesome.lib.savastparser.SAVASTParser;
import tv.superawesome.lib.savastparser.SAVASTParserInterface;

/**
 * This class abstracts away the loading of a SuperAwesome ad server by the server.
 * It tries to handle two major case
 *  - when the ad comes alone, in the case of image, rich media, tag, video
 *  - when the ads come as an array, in the case of app wall
 *
 * Additionally it will try to:
 *  - for image, rich media and tag ads, format the needed HTML to display the ad in a web view
 *  - for video ads, parse the associated VAST tag and get all the events and media files;
 *    also it will try to download the media resource on disk
 *  - for app wall ads, download all the image resources associated with each ad in the wall
 */
public class SALoader {

    // private context
    private Context context = null;

    /**
     * Standard constructor with a context
     *
     * @param context copy a reference to the context
     */
    public SALoader (Context context) {
        this.context = context;
    }

    /**
     * Method that creates the standard AwesomeAds session
     *
     * @param session       current session
     * @param placementId   current placement Id
     * @return              an url of the form https://ads.superawesome.tv/v2/ad/7212
     */
    public String getAwesomeAdsEndpoint (SASession session, int placementId) {
        return session.getBaseUrl() + "/ad/" + placementId;
    }

    /**
     * Method that creates the additional query paramters that will need to be appended to the
     * AwesomeAds endpoint
     *
     * @param session   current session
     * @return          a JSONObject containing the necessary query params, such as:
     *                  - test mode
     *                  - sdk version
     *                  - cache buster
     *                  - bundle & app name
     *                  - DAU Id for frequency capping
     *                  - connection type as an integer
     *                  - current language as "en_US"
     *                  - type of device as string, "phone" or "tablet"
     */
    public JSONObject getAwesomeAdsQuery (SASession session) {
        return SAJsonParser.newObject(new Object[]{
                "test", session.getTestMode(),
                "sdkVersion", session.getVersion(),
                "rnd", session.getCachebuster(),
                "bundle", session.getPackageName(),
                "name", session.getAppName(),
                "dauid", session.getDauId(),
                "ct", session.getConnectionType().ordinal(),
                "lang", session.getLang(),
                "device", session.getDevice()
                // "preload", true
        });
    }

    public JSONObject getAwesomeAdsHeader (SASession session) {
        return SAJsonParser.newObject(new Object[]{
                "Content-Type", "application/json",
                "User-Agent", session.getUserAgent()
        });
    }

    /**
     * Shorthand method that only takes a placement Id and a session
     *
     * @param placementId   the AwesomeAds ID to load an ad for
     * @param session       the current session to load the placement Id for
     * @param listener      listener copy so that the loader can return the response to the
     *                      library user
     */
    public void loadAd(final int placementId, final SASession session, final SALoaderInterface listener){

        // get connection things to AwesomeAds
        String endpoint = getAwesomeAdsEndpoint(session, placementId);
        JSONObject query = getAwesomeAdsQuery(session);
        JSONObject header = getAwesomeAdsHeader(session);

        // call to the load ad method
        loadAd(endpoint, query, header, placementId, session, listener);
    }

    /**
     * Method that actually loads the ad
     *
     * @param endpoint      endpoint from where to get an ad resource
     * @param query         additional query parameters
     * @param header        request header
     * @param placementId   placement Id (bc it's not returned by the request)
     * @param session       current session
     * @param listener      listener copy so that the loader can return the response to the
     *                      library user
     */
    public void loadAd (String endpoint, JSONObject query, JSONObject header, final int placementId, final SASession session, SALoaderInterface listener) {

        // create a local listener to avoid null pointer exceptions
        final SALoaderInterface localListener = listener != null ? listener : new SALoaderInterface() { @Override public void didLoadAd(SAResponse response) {} };


        SANetwork network = new SANetwork();
        network.sendGET(context, endpoint, query, header, new SANetworkInterface() {
            /**
             * Overridden method of the SANetworkInterface in which I process the ad response
             *
             * @param status    status of the SuperAwesome ad request
             * @param data      payload returned by the ad server
             * @param success   success status
             */
            @Override
            public void response(int status, String data, boolean success) {

                // create a new object of type SAResponse
                final SAResponse response = new SAResponse();
                response.status = status;
                response.placementId = placementId;

                // error case, just bail out with a non-null invalid response
                if (!success || data == null) {
                    localListener.didLoadAd(response);
                }
                // good case, continue trying to figure out what kind of ad this is
                else {

                    // declare the two possible json outcomes
                    JSONObject jsonObject = null;
                    JSONArray jsonArray = null;

                    // try to get json Object
                    try {
                        jsonObject = new JSONObject(data);
                    } catch (JSONException e) {
                        // do nothing
                    }

                    // try to get json Array
                    try {
                        jsonArray = new JSONArray(data);
                    } catch (JSONException e) {
                        // do nothing
                    }

                    // Normal Ad case
                    if (jsonObject != null) {

                        // parse the final ad
                        final SAAd ad = new SAAd(jsonObject);
                        ad.placementId = placementId;
                        // add events
                        SAProcessEvents.addAdEvents(ad, session);

                        // update type in response as well
                        response.format = ad.creative.creativeFormat;
                        response.ads.add(ad);

                        switch (ad.creative.creativeFormat) {
                            // in this case return whatever we have at this moment
                            case invalid:
                                localListener.didLoadAd(response);
                                break;
                            // in this case process the HTML and return the response
                            case image:
                                ad.creative.details.media.html = SAProcessHTML.formatCreativeIntoImageHTML(ad);
                                localListener.didLoadAd(response);
                                break;
                            // in this case process the HTML and return the response
                            case rich:
                                ad.creative.details.media.html = SAProcessHTML.formatCreativeIntoRichMediaHTML(ad);
                                localListener.didLoadAd(response);
                                break;
                            // in this case process the HTML and return the response
                            case tag: {
                                ad.creative.details.media.html = SAProcessHTML.formatCreativeIntoTagHTML(ad);
                                localListener.didLoadAd(response);
                                break;
                            }
                            // in this case process the VAST response, download the files and return
                            case video: {
                                SAVASTParser parser = new SAVASTParser(context);
                                parser.parseVAST(ad.creative.details.vast, new SAVASTParserInterface() {
                                    @Override
                                    public void didParseVAST(SAVASTAd savastAd) {

                                        // copy the vast media
                                        ad.creative.details.media.playableMediaUrl = savastAd.mediaUrl;
                                        // copy the vast events
                                        ad.creative.events.addAll(savastAd.vastEvents);
                                        // download file
                                        SAFileDownloader.getInstance().downloadFileFrom(context, ad.creative.details.media.playableMediaUrl, new SAFileDownloaderInterface() {
                                            @Override
                                            public void response(boolean success, String playableDiskUrl) {

                                                ad.creative.details.media.playableDiskUrl = playableDiskUrl;
                                                ad.creative.details.media.isOnDisk = playableDiskUrl != null;

                                                // finally respond with a response
                                                localListener.didLoadAd(response);

                                            }
                                        });
                                    }
                                });
                                break;
                            }
                        }
                    }
                    // ÅppWall case
                    else if (jsonArray != null) {

                        // assign correct format
                        response.format = SACreativeFormat.appwall;

                        // add ads to it
                        for (int i = 0; i < jsonArray.length(); i++) {

                            try {
                                // parse ad
                                SAAd ad = new SAAd(jsonArray.getJSONObject(i));
                                ad.placementId = placementId;
                                SAProcessEvents.addAdEvents(ad, session);

                                // only add image type ads - no rich media or videos in the
                                // GameWall for now
                                if (ad.creative.creativeFormat == SACreativeFormat.image) {
                                    response.ads.add(ad);
                                }
                            } catch (JSONException e) {
                                // do nothing
                            }
                        }

                        // add all the images that'll need to be downloaded
                        List<String> filesToDownload = new ArrayList<>();
                        for (SAAd ad : response.ads) {
                            filesToDownload.add(ad.creative.details.image);
                        }

                        // use the file list downloader to download them in the same
                        // correct order
                        SAFileListDownloader fileListDownloader = new SAFileListDownloader(context);
                        fileListDownloader.downloadListOfFiles(filesToDownload, new SAFileListDownloaderInterface() {
                            @Override
                            public void didGetAllFiles(List<String> list) {

                                for (int i = 0; i < list.size(); i++) {
                                    try {
                                        String diskUrl = list.get(i);
                                        SAAd ad = response.ads.get(i);
                                        ad.creative.details.media.playableMediaUrl = ad.creative.details.image;
                                        ad.creative.details.media.isOnDisk = diskUrl != null;
                                        ad.creative.details.media.playableDiskUrl = diskUrl;
                                    } catch (Exception e) {
                                        // do nothing
                                    }
                                }

                                // and finally send a response
                                localListener.didLoadAd(response);
                            }
                        });
                    }
                    // it's not a normal ad or an app wall, then return
                    else {
                        localListener.didLoadAd(response);
                    }
                }
            }
        });

    }
}
