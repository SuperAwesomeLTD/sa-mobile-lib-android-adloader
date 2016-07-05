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

import org.json.JSONException;
import org.json.JSONObject;

import tv.superawesome.lib.sautils.SAUtils;
import tv.superawesome.lib.savastparser.SAVASTParser;
import tv.superawesome.lib.savastparser.SAVASTParserInterface;
import tv.superawesome.lib.samodelspace.SAVASTAd;
import tv.superawesome.lib.sautils.*;
import tv.superawesome.lib.samodelspace.SAAd;
import tv.superawesome.lib.samodelspace.SACreativeFormat;
import tv.superawesome.lib.samodelspace.SAData;
import tv.superawesome.lib.sanetwork.request.*;

/**
 * This class gathers all the other parts of the "data" package and unifies the whole loading
 * experience for the user
 */
public class SALoader {

    /**
     * the function that actually loads the Ad
     * @param placementId - the placement ID a user might want to preload an Ad for
     * @param listener - a reference to the listener
     */
    public void loadAd(final int placementId, final SALoaderInterface listener){

        final String endpoint = SALoaderSession.getInstance().getBaseUrl() + "/ad/" + placementId;

        SAUtils.SAConnectionType type = SAUtils.SAConnectionType.unknown;
        String packageName = "unknown";
        Context c = SAApplication.getSAApplicationContext();
        if (c != null) {
            type = SAUtils.getNetworkConnectivity(c);
            packageName = c.getPackageName();
        }

        JSONObject query = new JSONObject();
        try {
            query.put("test", SALoaderSession.getInstance().getTest());
            query.put("sdkVersion", SALoaderSession.getInstance().getVersion());
            query.put("rnd", SAUtils.getCacheBuster());
            query.put("bundle", packageName);
            query.put("name", SAUtils.getAppLabel());
            query.put("dauid", SALoaderSession.getInstance().getDauId());
            query.put("ct", type.ordinal());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject header = new JSONObject();
        try {
            header.put("Content-Type", "application/json");
            header.put("User-Agent", SAUtils.getUserAgent());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SANetwork network = new SANetwork();
        network.sendGET(c, endpoint, query, header, new SANetworkInterface() {
            @Override
            public void success(int status, String data) {
                if (data == null) {
                    failAd(listener, placementId);
                    return;
                }

                JSONObject dataJson = null;
                try {
                    dataJson = new JSONObject(data.toString());
                    Log.d("SuperAwesome-data", dataJson.toString());
                    final SAAd ad = SAParser.parseDictionaryIntoAd(dataJson, placementId);

                    if (ad != null) {

                        ad.creative.details.data = new SAData();
                        SACreativeFormat type = ad.creative.creativeFormat;

                        switch (type) {
                            case invalid:
                            case image:
                            case rich:
                            case tag: {
                                ad.creative.details.data.adHtml = SAHTMLParser.formatCreativeDataIntoAdHTML(ad);
                                didLoadAd(listener, ad);
                                break;
                            }
                            case video: {
                                SAVASTParser parser = new SAVASTParser();

                                parser.parseVASTAds(ad.creative.details.vast, new SAVASTParserInterface() {
                                    @Override
                                    public void didParseVAST(SAVASTAd vastAd) {
                                        if (vastAd != null) {
                                            ad.creative.details.data.vastAd = vastAd;
                                            didLoadAd(listener, ad);
                                        } else {
                                            failAd(listener, ad.placementId);
                                        }
                                    }
                                });
                                break;
                            }
                        }
                    } else {
                        failAd(listener, placementId);
                    }
                } catch (JSONException e) {
                    failAd(listener, placementId);
                }
            }
            @Override
            public void failure() {
                failAd(listener, placementId);
            }
        });
    }

    void didLoadAd(SALoaderInterface listener, SAAd ad) {
        if (listener != null) {
            listener.didLoadAd(ad);
        }
    }

    private void failAd(SALoaderInterface listener, int placementId){
        if (listener != null){
            listener.didFailToLoadAdForPlacementId(placementId);
        }
    }
}
