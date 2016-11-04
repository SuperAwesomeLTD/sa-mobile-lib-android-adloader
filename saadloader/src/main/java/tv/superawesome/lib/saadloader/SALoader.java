package tv.superawesome.lib.saadloader;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tv.superawesome.lib.sajsonparser.SAJsonParser;
import tv.superawesome.lib.samodelspace.SAAd;
import tv.superawesome.lib.samodelspace.SACreativeFormat;
import tv.superawesome.lib.samodelspace.SAMedia;
import tv.superawesome.lib.samodelspace.SAResponse;
import tv.superawesome.lib.sanetwork.request.SANetwork;
import tv.superawesome.lib.sanetwork.request.SANetworkInterface;
import tv.superawesome.lib.sasession.SASession;

/**
 * This class gathers all the other parts of the "data" package and unifies the whole loading
 * experience for the user
 */
public class SALoader {

    private Context context = null;
    private SAAdParser adParser = null;

    /**
     * Constructor
     * @param context - current contet
     */
    public SALoader (Context context) {
        this.context = context;
        adParser = new SAAdParser();
    }

    /**
     * the function that actually loads the Ad
     * @param placementId - the placement ID a user might want to preload an Ad for
     * @param session - the current session that helps w/ formatting URLs
     * @param listener - a reference to the listener
     */
    public void loadAd(final int placementId, final SASession session, final SALoaderInterface listener){

        // create a local listener to avoid the "chain of listeners"
        final SALoaderInterface localListener = listener != null ? listener : new SALoaderInterface() { @Override public void didLoadAd(SAResponse response) {} };

        // form the endpoint
        final String endpoint = session.getBaseUrl() + "/ad/" + placementId;

        JSONObject query = SAJsonParser.newObject(new Object[]{
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

        JSONObject header = SAJsonParser.newObject(new Object[]{
                "Content-Type", "application/json",
                "User-Agent", session.getUserAgent()
        });

        SANetwork network = new SANetwork();
        network.sendGET(context, endpoint, query, header, new SANetworkInterface() {
            @Override
            public void response(int status, String data, boolean success) {

                final SAResponse response = new SAResponse();
                response.status = status;
                response.placementId = placementId;

                if (!success || data == null) {
                    localListener.didLoadAd(response);
                }
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
                        final SAAd ad = adParser.parseInitialAdDataFromNetwork(jsonObject, session, placementId);

                        if (ad != null) {

                            // define type
                            SACreativeFormat type = ad.creative.creativeFormat;

                            // update type in response as well
                            response.format = type;
                            response.ads.add(ad);

                            switch (type) {
                                case invalid:
                                case image:
                                case rich:
                                case tag: {
                                    ad.creative.details.media = new SAMedia();
                                    ad.creative.details.media.html = SAHTMLParser.formatCreativeDataIntoAdHTML(ad);
                                    localListener.didLoadAd(response);
                                    break;
                                }
                                case video: {
                                    SAVASTParser parser = new SAVASTParser(context);
                                    parser.parseVASTAds(ad.creative.details.vast, session, new SAVASTParserInterface() {
                                        @Override
                                        public void didParseVAST(SAAd vastAd) {
                                            ad.sumAd(vastAd);
                                            localListener.didLoadAd(response);
                                        }
                                    });
                                    break;
                                }
                            }
                        } else {
                            localListener.didLoadAd(response);
                        }
                    }
                    // GameWall case
                    else if (jsonArray != null) {

                        // assign correct format
                        response.format = SACreativeFormat.gamewall;

                        // add ads to it
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {

                                // parse ad
                                SAAd ad = adParser.parseInitialAdDataFromNetwork(jsonArray.getJSONObject(i), session, placementId);

                                // if all's OK add to response
                                if (ad != null) {

                                    SACreativeFormat format = ad.creative.creativeFormat;

                                    // only add image type ads - no rich media or videos in the
                                    // GameWall for now
                                    if (format == SACreativeFormat.image) {
                                        response.ads.add(ad);
                                    }
                                }
                            } catch (JSONException e) {
                                // do nothing
                            }
                        }

                        // get all resources for the GameWall
                        SAAppWallParser gameWallParser = new SAAppWallParser(context);
                        gameWallParser.getAppWallResources(response.ads, new SAAppWallParserInterface() {
                            @Override
                            public void gotAllImages() {
                                // return response
                                localListener.didLoadAd(response);
                            }
                        });
                    }
                    else {
                        localListener.didLoadAd(response);
                    }
                }
            }
        });
    }
}
