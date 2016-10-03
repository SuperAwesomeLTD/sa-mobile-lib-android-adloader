/**
 * @class: SAAdParser.java
 * @copyright: (c) 2015 SuperAwesome Ltd. All rights reserved.
 * @author: Gabriel Coman
 * @date: 29/10/2015
 *
 */
package tv.superawesome.lib.saadloader;

import android.content.Context;

import org.json.JSONObject;

import tv.superawesome.lib.sajsonparser.SAJsonParser;
import tv.superawesome.lib.samodelspace.SAAd;
import tv.superawesome.lib.samodelspace.SACampaignType;
import tv.superawesome.lib.samodelspace.SACreativeFormat;
import tv.superawesome.lib.samodelspace.SATracking;
import tv.superawesome.lib.sasession.SASession;
import tv.superawesome.lib.sautils.SAUtils;

/**
 * This is a class of static functions that make it easy to parse Ad responses from the
 * server into SuperAwesome SDK models
 */
public class SAAdParser {

    /**
     * Parses a dictionary received from the server into a valid Ad object
     * @param dict - the dictionary to parse
     * @param session - the current session
     * @param placementId = the placement id - just used because it's not returned by the ad server
     */
    public SAAd parseInitialAdDataFromNetwork(JSONObject dict, SASession session, int placementId) {

        try {
            SAAd ad = new SAAd(dict);
            ad.placementId = placementId;

            // perform the next steps of the parsing
            ad.creative.creativeFormat = SACreativeFormat.invalid;
            if (ad.creative.format.equals("image_with_link")) ad.creative.creativeFormat = SACreativeFormat.image;
            else if (ad.creative.format.equals("video")) ad.creative.creativeFormat = SACreativeFormat.video;
            else if (ad.creative.format.contains("rich_media")) ad.creative.creativeFormat = SACreativeFormat.rich;
            else if (ad.creative.format.contains("tag")) ad.creative.creativeFormat = SACreativeFormat.tag;
            else if (ad.creative.format.contains("gamewall")) ad.creative.creativeFormat = SACreativeFormat.gamewall;

            // cpm vs cpi/
            ad.saCampaignType = SACampaignType.CPM;
            if (ad.campaignType == 1) ad.saCampaignType = SACampaignType.CPI;

            JSONObject trackingDict = SAJsonParser.newObject(new Object[]{
                    "placement", ad.placementId,
                    "line_item", ad.lineItemId,
                    "creative", ad.creative.id,
                    "sdkVersion", session.getVersion(),
                    "rnd", session.getCachebuster(),
                    "ct", session.getConnectionType()
            });

            SATracking trackingEvt = new SATracking();
            trackingEvt.event = "sa_tracking";
            trackingEvt.URL = session.getBaseUrl() +
                    (ad.creative.creativeFormat == SACreativeFormat.video ? "/video/click/?" : "/click?") +
                    SAUtils.formGetQueryFromDict(trackingDict);

            JSONObject impressionDict = SAJsonParser.newObject(new Object[] {
                    "placement", ad.placementId,
                    "creative", ad.creative.id,
                    "line_item", ad.lineItemId,
                    "sdkVersion", session.getVersion(),
                    "rnd", session.getCachebuster(),
                    "no_image", true
            });

            SATracking saImpressionEvt = new SATracking();
            saImpressionEvt.event = "sa_impr";
            saImpressionEvt.URL = session.getBaseUrl() + "/impression?" + SAUtils.formGetQueryFromDict(impressionDict);

            JSONObject impressionDict1 = SAJsonParser.newObject(new Object[]{
                    "placement", ad.placementId,
                    "line_item", ad.lineItemId,
                    "creative", ad.creative.id,
                    "type", "viewable_impression"
            });

            JSONObject impressionDict2 = SAJsonParser.newObject(new Object[]{
                    "sdkVersion", session.getVersion(),
                    "rnd", session.getCachebuster(),
                    "ct", session.getConnectionType(),
                    "data", SAUtils.encodeDictAsJsonDict(impressionDict1)
            });

            SATracking viewableImpression = new SATracking();
            viewableImpression.event = "viewable_impr";
            viewableImpression.URL = session.getBaseUrl() + "/event?" + SAUtils.formGetQueryFromDict(impressionDict2);


            /** create the parental gate URLs */
            JSONObject pgcloseDict1 = SAJsonParser.newObject(new Object[]{
                    "placement", ad.placementId,
                    "line_item", ad.lineItemId,
                    "creative", ad.creative.id,
                    "type", "parentalGateClose"
            });

            JSONObject pgcloseDict2 = SAJsonParser.newObject(new Object[]{
                    "sdkVersion", session.getVersion(),
                    "rnd", session.getCachebuster(),
                    "ct", session.getConnectionType(),
                    "data", SAUtils.encodeDictAsJsonDict(pgcloseDict1)
            });

            SATracking parentalGateClose = new SATracking();
            parentalGateClose.event = "pg_close";
            parentalGateClose.URL = session.getBaseUrl() + "/event?" + SAUtils.formGetQueryFromDict(pgcloseDict2);


            JSONObject pgopenDict1 = SAJsonParser.newObject(new Object[]{
                    "placement", ad.placementId,
                    "line_item", ad.lineItemId,
                    "creative", ad.creative.id,
                    "type", "parentalGateOpen"
            });

            JSONObject pgopenDict2 = SAJsonParser.newObject(new Object[]{
                    "sdkVersion", session.getVersion(),
                    "rnd", session.getCachebuster(),
                    "ct", session.getConnectionType(),
                    "data", SAUtils.encodeDictAsJsonDict(pgopenDict1)
            });

            SATracking parentalGateOpen = new SATracking();
            parentalGateOpen.event = "pg_open";
            parentalGateOpen.URL = session.getBaseUrl() + "/event?" + SAUtils.formGetQueryFromDict(pgopenDict2);


            JSONObject pgfailDict1 = SAJsonParser.newObject(new Object[]{
                    "placement", ad.placementId,
                    "line_item", ad.lineItemId,
                    "creative", ad.creative.id,
                    "type", "parentalGateFail"
            });

            JSONObject pgfailDict2 = SAJsonParser.newObject(new Object[]{
                    "sdkVersion", session.getVersion(),
                    "rnd", session.getCachebuster(),
                    "ct", session.getConnectionType(),
                    "data", SAUtils.encodeDictAsJsonDict(pgfailDict1)
            });

            SATracking parentalGateFail = new SATracking();
            parentalGateFail.event = "pg_fail";
            parentalGateFail.URL = session.getBaseUrl() + "/event?" + SAUtils.formGetQueryFromDict(pgfailDict2);


            JSONObject pgsuccessDict1 = SAJsonParser.newObject(new Object[] {
                    "placement", ad.placementId,
                    "line_item", ad.lineItemId,
                    "creative", ad.creative.id,
                    "type", "parentalGateSuccess"
            });

            JSONObject pgsuccessDict2 = SAJsonParser.newObject(new Object[] {
                    "sdkVersion", session.getVersion(),
                    "rnd", session.getCachebuster(),
                    "ct", session.getConnectionType(),
                    "data", SAUtils.encodeDictAsJsonDict(pgsuccessDict1)
            });

            SATracking parentalGateSuccess = new SATracking();
            parentalGateSuccess.event = "pg_success";
            parentalGateSuccess.URL = session.getBaseUrl() + "/event?" + SAUtils.formGetQueryFromDict(pgsuccessDict2);

            ad.creative.events.add(trackingEvt);
            ad.creative.events.add(viewableImpression);
            ad.creative.events.add(parentalGateClose);
            ad.creative.events.add(parentalGateFail);
            ad.creative.events.add(parentalGateOpen);
            ad.creative.events.add(parentalGateSuccess);
            ad.creative.events.add(saImpressionEvt);

            // add impression
            if (ad.creative.impressionUrl != null) {
                SATracking impr = new SATracking();
                impr.URL = ad.creative.impressionUrl;
                impr.event = "impression";
                ad.creative.events.add(impr);
            }

            // add install
            if (ad.creative.installUrl != null) {
                SATracking inst = new SATracking();
                inst.URL = ad.creative.installUrl;
                inst.event = "install";
                ad.creative.events.add(inst);
            }

            // cdn url
            switch (ad.creative.creativeFormat) {
                case tag:
                case invalid: { break; }
                case gamewall:
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

            // do a validity check
            if (!ad.isValid()) {
                return null;
            }

            // return proper ad
            return ad;
        } catch (Exception e){
            return null;
        }
    }

}
