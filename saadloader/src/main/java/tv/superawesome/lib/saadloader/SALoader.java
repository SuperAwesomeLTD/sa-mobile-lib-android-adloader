/**
 * @class: SALoader.java
 * @copyright: (c) 2015 SuperAwesome Ltd. All rights reserved.
 * @author: Gabriel Coman
 * @date: 28/09/2015
 *
 */
package tv.superawesome.lib.saadloader;

/**
 * Imports needed for this implementation
 */

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.Locale;

import tv.superawesome.lib.sajsonparser.SAJsonParser;
import tv.superawesome.lib.samodelspace.SAMedia;
import tv.superawesome.lib.sasession.SASession;
import tv.superawesome.lib.sautils.SAUtils;
import tv.superawesome.lib.sautils.*;
import tv.superawesome.lib.samodelspace.SAAd;
import tv.superawesome.lib.samodelspace.SACreativeFormat;
import tv.superawesome.lib.sanetwork.request.*;

/**
 * This class gathers all the other parts of the "data" package and unifies the whole loading
 * experience for the user
 */
public class SALoader {

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

        SAUtils.SAConnectionType type = SAUtils.SAConnectionType.unknown;
        String packageName = "unknown";
        Context c = SAApplication.getSAApplicationContext();
        if (c != null) {
            type = SAUtils.getNetworkConnectivity(c);
            packageName = c.getPackageName();
        }

        JSONObject query = SAJsonParser.newObject(new Object[]{
                "test", session.getTestMode(),
                "sdkVersion", session.getVersion(),
                "rnd", SAUtils.getCacheBuster(),
                "bundle", packageName,
                "name", SAUtils.getAppLabel(),
                "dauid", session.getDauId(),
                "ct", type.ordinal(),
                "lang", Locale.getDefault().toString(),
                "device", SAUtils.getSystemSize() == SAUtils.SASystemSize.mobile ? "mobile" : "tablet"
        });

        JSONObject header = SAJsonParser.newObject(new Object[]{
                "Content-Type", "application/json",
                "User-Agent", SAUtils.getUserAgent()
        });

        SANetwork network = new SANetwork();
        network.sendGET(c, endpoint, query, header, new SANetworkInterface() {
            @Override
            public void response(int status, String data, boolean success) {
                if (!success || data == null) {
                    localListener.didLoadAd(null);
                } else {
                    // get data
                    JSONObject dataJson = SAJsonParser.newObject(data);
                    final SAAd ad = SAAdParser.parseInitialAdDataFromNetwork(dataJson, session, placementId);

                    if (ad != null) {

                        Log.d("SuperAwesome", "Ad data is: " + ad.isValid() + " | " + data);

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
                                SAVASTParser parser = new SAVASTParser();
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
