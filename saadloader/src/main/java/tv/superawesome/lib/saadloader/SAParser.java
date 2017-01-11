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

import tv.superawesome.lib.sajsonparser.SAJsonParser;
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
    public static SAAd parseDictionaryIntoAd(Context c, JSONObject dict, int placementId) {

        SAUtils.SAConnectionType ct = SAUtils.SAConnectionType.unknown;
        // Context c = SAApplication.getSAApplicationContext();
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


            JSONObject trackingDict = SAJsonParser.newObject(new Object[]{
                    "placement", ad.placementId,
                    "line_item", ad.lineItemId,
                    "creative", ad.creative.id,
                    "sdkVersion", SASession.getInstance().getVersion(),
                    "rnd", SAUtils.getCacheBuster(),
                    "ct", ct.ordinal()
            });

            ad.creative.trackingUrl = SASession.getInstance().getBaseUrl() +
                    (ad.creative.creativeFormat == SACreativeFormat.video ? "/video/click/?" : "/click?") +
                    SAUtils.formGetQueryFromDict(trackingDict);

            JSONObject impressionDict1 = SAJsonParser.newObject(new Object[]{
                    "placement", ad.placementId,
                    "line_item", ad.lineItemId,
                    "creative", ad.creative.id,
                    "type", "viewable_impression"
            });

            JSONObject impressionDict2 = SAJsonParser.newObject(new Object[]{
                    "sdkVersion", SASession.getInstance().getVersion(),
                    "rnd", SAUtils.getCacheBuster(),
                    "ct", ct.ordinal(),
                    "data", SAUtils.encodeDictAsJsonDict(impressionDict1)
            });

            ad.creative.viewableImpressionUrl = SASession.getInstance().getBaseUrl() + "/event?" + SAUtils.formGetQueryFromDict(impressionDict2);

            /** create the parental gate URLs */
            JSONObject pgcloseDict1 = SAJsonParser.newObject(new Object[]{
                    "placement", ad.placementId,
                    "line_item", ad.lineItemId,
                    "creative", ad.creative.id,
                    "type", "parentalGateClose"
            });

            JSONObject pgcloseDict2 = SAJsonParser.newObject(new Object[]{
                    "sdkVersion", SASession.getInstance().getVersion(),
                    "rnd", SAUtils.getCacheBuster(),
                    "ct", ct.ordinal(),
                    "data", SAUtils.encodeDictAsJsonDict(pgcloseDict1)
            });

            ad.creative.parentalGateCloseUrl = SASession.getInstance().getBaseUrl() + "/event?" + SAUtils.formGetQueryFromDict(pgcloseDict2);

            JSONObject pgopenDict1 = SAJsonParser.newObject(new Object[]{
                    "placement", ad.placementId,
                    "line_item", ad.lineItemId,
                    "creative", ad.creative.id,
                    "type", "parentalGateOpen"
            });

            JSONObject pgopenDict2 = SAJsonParser.newObject(new Object[]{
                    "sdkVersion", SASession.getInstance().getVersion(),
                    "rnd", SAUtils.getCacheBuster(),
                    "ct", ct.ordinal(),
                    "data", SAUtils.encodeDictAsJsonDict(pgopenDict1)
            });

            ad.creative.parentalGateOpenUrl = SASession.getInstance().getBaseUrl() + "/event?" + SAUtils.formGetQueryFromDict(pgopenDict2);

            JSONObject pgfailDict1 = SAJsonParser.newObject(new Object[]{
                    "placement", ad.placementId,
                    "line_item", ad.lineItemId,
                    "creative", ad.creative.id,
                    "type", "parentalGateFail"
            });


            JSONObject pgfailDict2 = SAJsonParser.newObject(new Object[]{
                    "sdkVersion", SASession.getInstance().getVersion(),
                    "rnd", SAUtils.getCacheBuster(),
                    "ct", ct.ordinal(),
                    "data", SAUtils.encodeDictAsJsonDict(pgfailDict1)
            });

            ad.creative.parentalGateFailUrl = SASession.getInstance().getBaseUrl() + "/event?" + SAUtils.formGetQueryFromDict(pgfailDict2);

            JSONObject pgsuccessDict1 = SAJsonParser.newObject(new Object[] {
                    "placement", ad.placementId,
                    "line_item", ad.lineItemId,
                    "creative", ad.creative.id,
                    "type", "parentalGateSuccess"
            });

            JSONObject pgsuccessDict2 = SAJsonParser.newObject(new Object[] {
                    "sdkVersion", SASession.getInstance().getVersion(),
                    "rnd", SAUtils.getCacheBuster(),
                    "ct", ct.ordinal(),
                    "data", SAUtils.encodeDictAsJsonDict(pgsuccessDict1)
            });

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
