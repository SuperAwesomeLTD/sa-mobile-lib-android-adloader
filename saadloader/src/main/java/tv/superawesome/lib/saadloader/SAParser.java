/**
 * @class: SAParser.java
 * @copyright: (c) 2015 SuperAwesome Ltd. All rights reserved.
 * @author: Gabriel Coman
 * @date: 29/10/2015
 *
 */
package tv.superawesome.lib.saadloader;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import tv.superawesome.lib.sautils.*;
import tv.superawesome.lib.saadloader.models.SAAd;
import tv.superawesome.lib.saadloader.models.SACreativeFormat;

/**
 * This is a class of static functions that make it easy to parse Ad responses from the
 * server into SuperAwesome SDK models
 */
public class SAParser {

    /**
     * This function tests wherer an Ad is valid; It does so by looking at some crucial ad
     * data that has to be there, as well as data that has to exist depending on the actual type
     * of the ad
     * @param ad - the ad that you want to test
     * @return - true or false, depending on the success of the tests
     */
    public static boolean isAdDataValid(SAAd ad) {

        if (ad == null) return false;
        if (ad.creative == null)  return false;
        if (ad.creative.creativeFormat == SACreativeFormat.invalid) return false;
        if (ad.creative.details == null) return false;

        switch (ad.creative.creativeFormat) {
            case image:{ if (ad.creative.details.image == null) return false; break; }
            case video:{ if (ad.creative.details.vast == null)  return false; break; }
            case rich:{ if (ad.creative.details.url == null)  return false; break; }
            case tag:{ if (ad.creative.details.tag == null)  return false; break; }
            default:{ break; }
        }

        return true;
    }

    /**
     * This function performs the Basic integrity check on a piece of data loaded from the
     * internet
     * @param dict
     */
    private static boolean performIntegrityCheck(JSONObject dict){
        if (!SAUtils.isJSONEmpty(dict)){
            JSONObject creative = dict.optJSONObject("creative");
            if (!SAUtils.isJSONEmpty(creative)){
                JSONObject details = creative.optJSONObject("details");
                if (!SAUtils.isJSONEmpty(details)){
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    /**
     * Parses a dictionary received from the server into a valid Ad object
     * @param dict - the dictionary to parse
     * @param placementId = the placement id - just used because it's not returned by the ad server
     */
    public static SAAd parseDictionaryIntoAd(JSONObject dict, int placementId) {
        /** perform integrity check */
        if (!SAParser.performIntegrityCheck(dict)){
            Log.d("SuperAwesome", "Did not pass integrity check");
            return null;
        }

        /** surround with a try catch block */
        try {
            SAAd ad = new SAAd(dict);
            ad.placementId = placementId;

            /** prform the next steps of the parsing */
            ad.creative.creativeFormat = SACreativeFormat.invalid;
            if (ad.creative.format.equals("image_with_link")) ad.creative.creativeFormat = SACreativeFormat.image;
            else if (ad.creative.format.equals("video")) ad.creative.creativeFormat = SACreativeFormat.video;
            else if (ad.creative.format.contains("rich_media")) ad.creative.creativeFormat = SACreativeFormat.rich;
            else if (ad.creative.format.contains("tag")) ad.creative.creativeFormat = SACreativeFormat.tag;

            /** create the tracking URL */
            JSONObject trackingDict = new JSONObject();
            try {
                trackingDict.put("placement", ad.placementId);
                trackingDict.put("line_item", ad.lineItemId);
                trackingDict.put("creative", ad.creative.id);
                trackingDict.put("sdkVersion", SALoaderSession.getInstance().getVersion());
                trackingDict.put("rnd", SAUtils.getCacheBuster());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ad.creative.trackingUrl = SALoaderSession.getInstance().getBaseUrl() +
                    (ad.creative.creativeFormat == SACreativeFormat.video ? "/video/click/?" : "/click?") +
                    SAUtils.formGetQueryFromDict(trackingDict);

            /** create the viewable impression URL */
            JSONObject impressionDict1 = new JSONObject();
            try {
                impressionDict1.put("placement", ad.placementId);
                impressionDict1.put("line_item", ad.lineItemId);
                impressionDict1.put("creative", ad.creative.id);
                impressionDict1.put("type", "viewable_impression");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject impressionDict2 = new JSONObject();
            try {
                impressionDict2.put("sdkVersion", SALoaderSession.getInstance().getVersion());
                impressionDict2.put("rnd", SAUtils.getCacheBuster());
                impressionDict2.put("data", SAUtils.encodeDictAsJsonDict(impressionDict1));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ad.creative.viewableImpressionUrl = SALoaderSession.getInstance().getBaseUrl() + "/event?" + SAUtils.formGetQueryFromDict(impressionDict2);

            /** create the parental gate URL */
            JSONObject pgDict1 = new JSONObject();
            try {
                pgDict1.put("placement", ad.placementId);
                pgDict1.put("line_item", ad.lineItemId);
                pgDict1.put("creative", ad.creative.id);
                pgDict1.put("type", "custom.parentalGateAccessed");
            } catch (JSONException e){
                e.printStackTrace();
            }
            JSONObject pgDict2 = new JSONObject();
            try {
                pgDict2.put("sdkVersion", SALoaderSession.getInstance().getVersion());
                pgDict2.put("rnd", SAUtils.getCacheBuster());
                pgDict2.put("data", SAUtils.encodeDictAsJsonDict(pgDict1));
            } catch (JSONException e){
                e.printStackTrace();
            }

            ad.creative.parentalGateClickUrl = SALoaderSession.getInstance().getBaseUrl() + "/event?" + SAUtils.formGetQueryFromDict(pgDict2);

            /** do a validity check */
            if (!isAdDataValid(ad)) {
                return null;
            }

            /** return proper ad */
            return ad;
        } catch (Exception e){
            return null;
        }
    }

}
