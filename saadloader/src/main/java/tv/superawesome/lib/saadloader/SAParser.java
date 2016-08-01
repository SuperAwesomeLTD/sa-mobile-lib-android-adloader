/**
 * @class: SAParser.java
 * @copyright: (c) 2015 SuperAwesome Ltd. All rights reserved.
 * @author: Gabriel Coman
 * @date: 29/10/2015
 *
 */
package tv.superawesome.lib.saadloader;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ConcurrentModificationException;

import tv.superawesome.lib.samodelspace.SACampaignType;
import tv.superawesome.lib.sasession.SASession;
import tv.superawesome.lib.sautils.*;
import tv.superawesome.lib.samodelspace.SAAd;
import tv.superawesome.lib.samodelspace.SACreativeFormat;

/**
 * This is a class of static functions that make it easy to parse Ad responses from the
 * server into SuperAwesome SDK models
 */
public class SAParser {

    /**
     * Parses a dictionary received from the server into a valid Ad object
     * @param dict - the dictionary to parse
     * @param placementId = the placement id - just used because it's not returned by the ad server
     */
    public static SAAd parseDictionaryIntoAd(JSONObject dict, int placementId) {

        SAUtils.SAConnectionType ct = SAUtils.SAConnectionType.unknown;
        Context c = SAApplication.getSAApplicationContext();
        if (c != null) ct = SAUtils.getNetworkConnectivity(c);

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

            /** cpm vs cpi */
            ad.saCampaignType = SACampaignType.CPM;
            if (ad.campaignType == 1) ad.saCampaignType = SACampaignType.CPI;

            /** create the tracking URL */
            JSONObject trackingDict = new JSONObject();
            try {
                trackingDict.put("placement", ad.placementId);
                trackingDict.put("line_item", ad.lineItemId);
                trackingDict.put("creative", ad.creative.id);
                trackingDict.put("sdkVersion", SASession.getInstance().getVersion());
                trackingDict.put("rnd", SAUtils.getCacheBuster());
                trackingDict.put("ct", ct.ordinal());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ad.creative.trackingUrl = SASession.getInstance().getBaseUrl() +
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
                impressionDict2.put("sdkVersion", SASession.getInstance().getVersion());
                impressionDict2.put("rnd", SAUtils.getCacheBuster());
                impressionDict2.put("ct", ct.ordinal());
                impressionDict2.put("data", SAUtils.encodeDictAsJsonDict(impressionDict1));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ad.creative.viewableImpressionUrl = SASession.getInstance().getBaseUrl() + "/event?" + SAUtils.formGetQueryFromDict(impressionDict2);

            /** create the parental gate URLs */
            JSONObject pgcloseDict1 = new JSONObject();
            try {
                pgcloseDict1.put("placement", ad.placementId);
                pgcloseDict1.put("line_item", ad.lineItemId);
                pgcloseDict1.put("creative", ad.creative.id);
                pgcloseDict1.put("type", "parentalGateClose");
            } catch (JSONException e){
                e.printStackTrace();
            }
            JSONObject pgcloseDict2 = new JSONObject();
            try {
                pgcloseDict2.put("sdkVersion", SASession.getInstance().getVersion());
                pgcloseDict2.put("rnd", SAUtils.getCacheBuster());
                pgcloseDict2.put("ct", ct.ordinal());
                pgcloseDict2.put("data", SAUtils.encodeDictAsJsonDict(pgcloseDict1));
            } catch (JSONException e){
                e.printStackTrace();
            }
            ad.creative.parentalGateCloseUrl = SASession.getInstance().getBaseUrl() + "/event?" + SAUtils.formGetQueryFromDict(pgcloseDict2);

            JSONObject pgopenDict1 = new JSONObject();
            try {
                pgopenDict1.put("placement", ad.placementId);
                pgopenDict1.put("line_item", ad.lineItemId);
                pgopenDict1.put("creative", ad.creative.id);
                pgopenDict1.put("type", "parentalGateOpen");
            } catch (JSONException e){
                e.printStackTrace();
            }
            JSONObject pgopenDict2 = new JSONObject();
            try {
                pgopenDict2.put("sdkVersion", SASession.getInstance().getVersion());
                pgopenDict2.put("rnd", SAUtils.getCacheBuster());
                pgopenDict2.put("ct", ct.ordinal());
                pgopenDict2.put("data", SAUtils.encodeDictAsJsonDict(pgopenDict1));
            } catch (JSONException e){
                e.printStackTrace();
            }
            ad.creative.parentalGateOpenUrl = SASession.getInstance().getBaseUrl() + "/event?" + SAUtils.formGetQueryFromDict(pgopenDict2);

            JSONObject pgfailDict1 = new JSONObject();
            try {
                pgfailDict1.put("placement", ad.placementId);
                pgfailDict1.put("line_item", ad.lineItemId);
                pgfailDict1.put("creative", ad.creative.id);
                pgfailDict1.put("type", "parentalGateFail");
            } catch (JSONException e){
                e.printStackTrace();
            }
            JSONObject pgfailDict2 = new JSONObject();
            try {
                pgfailDict2.put("sdkVersion", SASession.getInstance().getVersion());
                pgfailDict2.put("rnd", SAUtils.getCacheBuster());
                pgfailDict2.put("ct", ct.ordinal());
                pgfailDict2.put("data", SAUtils.encodeDictAsJsonDict(pgfailDict1));
            } catch (JSONException e){
                e.printStackTrace();
            }
            ad.creative.parentalGateFailUrl = SASession.getInstance().getBaseUrl() + "/event?" + SAUtils.formGetQueryFromDict(pgfailDict2);

            JSONObject pgsuccessDict1 = new JSONObject();
            try {
                pgsuccessDict1.put("placement", ad.placementId);
                pgsuccessDict1.put("line_item", ad.lineItemId);
                pgsuccessDict1.put("creative", ad.creative.id);
                pgsuccessDict1.put("type", "parentalGateSuccess");
            } catch (JSONException e){
                e.printStackTrace();
            }
            JSONObject pgsuccessDict2 = new JSONObject();
            try {
                pgsuccessDict2.put("sdkVersion", SASession.getInstance().getVersion());
                pgsuccessDict2.put("rnd", SAUtils.getCacheBuster());
                pgsuccessDict2.put("ct", ct.ordinal());
                pgsuccessDict2.put("data", SAUtils.encodeDictAsJsonDict(pgsuccessDict1));
            } catch (JSONException e){
                e.printStackTrace();
            }
            ad.creative.parentalGateSuccessUrl = SASession.getInstance().getBaseUrl() + "/event?" + SAUtils.formGetQueryFromDict(pgsuccessDict2);

            // cdn url
            switch (ad.creative.creativeFormat) {
                case tag:
                case invalid: { break; }
                case image: {
                    ad.creative.details.cdnUrl = SAUtils.findBaseURLFromResourceURL(ad.creative.details.image);
                    break;
                }
                case video: {
                    ad.creative.details.cdnUrl = SAUtils.findBaseURLFromResourceURL(ad.creative.details.video);
                    break;
                }
                case rich: {
                    ad.creative.details.cdnUrl = SAUtils.findBaseURLFromResourceURL(ad.creative.details.url);
                    break;
                }
            }

            /** do a validity check */
            if (!ad.isValid()) {
                return null;
            }

            /** return proper ad */
            return ad;
        } catch (Exception e){
            return null;
        }
    }

}
