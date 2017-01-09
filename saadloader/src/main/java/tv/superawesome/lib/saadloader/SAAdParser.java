package tv.superawesome.lib.saadloader;

import org.json.JSONObject;

import tv.superawesome.lib.sajsonparser.SAJsonParser;
import tv.superawesome.lib.samodelspace.SAAd;
import tv.superawesome.lib.samodelspace.SACampaignType;
import tv.superawesome.lib.samodelspace.SACreativeFormat;
import tv.superawesome.lib.samodelspace.SATracking;
import tv.superawesome.lib.sasession.SASession;
import tv.superawesome.lib.sautils.SAUtils;

public class SAAdParser {

    /**
     * Parses a dictionary received from the server into a valid Ad object
     * @param dict - the dictionary to parse
     * @param session - the current session
     * @param placementId = the placement id - just used because it's not returned by the ad server
     */
    public SAAd parseInitialAdDataFromNetwork(JSONObject dict, SASession session, int placementId) {

        try {

            // create an Ad
            SAAd ad = new SAAd(dict);

            // add the placement Id
            ad.placementId = placementId;

            // get all events

            SATracking clickEvt = new SATracking();
            clickEvt.event = "sa_tracking";
            clickEvt.URL = session.getBaseUrl() +
                    (ad.creative.creativeFormat == SACreativeFormat.video ? "/video/click/?" : "/click?") +
                    SAUtils.formGetQueryFromDict(SAJsonParser.newObject(new Object[]{
                            "placement", ad.placementId,
                            "line_item", ad.lineItemId,
                            "creative", ad.creative.id,
                            "sdkVersion", session.getVersion(),
                            "sourceBundle", session.getPackageName(),
                            "rnd", session.getCachebuster(),
                            "ct", session.getConnectionType()
                    }));

            SATracking saImpressionEvt = new SATracking();
            saImpressionEvt.event = "sa_impr";
            saImpressionEvt.URL = session.getBaseUrl() +
                    "/impression?" +
                    SAUtils.formGetQueryFromDict(SAJsonParser.newObject(new Object[] {
                            "placement", ad.placementId,
                            "creative", ad.creative.id,
                            "line_item", ad.lineItemId,
                            "sdkVersion", session.getVersion(),
                            "sourceBundle", session.getPackageName(),
                            "rnd", session.getCachebuster(),
                            "no_image", true
                    }));

            SATracking viewableImpression = new SATracking();
            viewableImpression.event = "viewable_impr";
            viewableImpression.URL = session.getBaseUrl() +
                    "/event?" +
                    SAUtils.formGetQueryFromDict(SAJsonParser.newObject(new Object[]{
                            "sdkVersion", session.getVersion(),
                            "rnd", session.getCachebuster(),
                            "ct", session.getConnectionType(),
                            "sourceBundle", session.getPackageName(),
                            "data", SAUtils.encodeDictAsJsonDict(SAJsonParser.newObject(new Object[] {
                                "placement", ad.placementId,
                                "line_item", ad.lineItemId,
                                "creative", ad.creative.id,
                                "type", "viewable_impression"
                            }))
                    }));

            SATracking parentalGateClose = new SATracking();
            parentalGateClose.event = "pg_close";
            parentalGateClose.URL = session.getBaseUrl() +
                    "/event?" +
                    SAUtils.formGetQueryFromDict(SAJsonParser.newObject(new Object[]{
                            "sdkVersion", session.getVersion(),
                            "rnd", session.getCachebuster(),
                            "ct", session.getConnectionType(),
                            "sourceBundle", session.getPackageName(),
                            "data", SAUtils.encodeDictAsJsonDict(SAJsonParser.newObject(new Object[]{
                                "placement", ad.placementId,
                                "line_item", ad.lineItemId,
                                "creative", ad.creative.id,
                                "type", "parentalGateClose"
                            }))
                    }));


            SATracking parentalGateOpen = new SATracking();
            parentalGateOpen.event = "pg_open";
            parentalGateOpen.URL = session.getBaseUrl() +
                    "/event?" +
                    SAUtils.formGetQueryFromDict(SAJsonParser.newObject(new Object[]{
                            "sdkVersion", session.getVersion(),
                            "rnd", session.getCachebuster(),
                            "ct", session.getConnectionType(),
                            "sourceBundle", session.getPackageName(),
                            "data", SAUtils.encodeDictAsJsonDict(SAJsonParser.newObject(new Object[] {
                                "placement", ad.placementId,
                                "line_item", ad.lineItemId,
                                "creative", ad.creative.id,
                                "type", "parentalGateOpen"
                            }))
                    }));

            SATracking parentalGateFail = new SATracking();
            parentalGateFail.event = "pg_fail";
            parentalGateFail.URL = session.getBaseUrl() +
                    "/event?" +
                    SAUtils.formGetQueryFromDict(SAJsonParser.newObject(new Object[]{
                            "sdkVersion", session.getVersion(),
                            "rnd", session.getCachebuster(),
                            "ct", session.getConnectionType(),
                            "sourceBundle", session.getPackageName(),
                            "data", SAUtils.encodeDictAsJsonDict(SAJsonParser.newObject(new Object[]{
                                "placement", ad.placementId,
                                "line_item", ad.lineItemId,
                                "creative", ad.creative.id,
                                "type", "parentalGateFail"
                            }))
                    }));

            SATracking parentalGateSuccess = new SATracking();
            parentalGateSuccess.event = "pg_success";
            parentalGateSuccess.URL = session.getBaseUrl() +
                    "/event?" +
                    SAUtils.formGetQueryFromDict(SAJsonParser.newObject(new Object[] {
                            "sdkVersion", session.getVersion(),
                            "rnd", session.getCachebuster(),
                            "ct", session.getConnectionType(),
                            "sourceBundle", session.getPackageName(),
                            "data", SAUtils.encodeDictAsJsonDict(SAJsonParser.newObject(new Object[] {
                                "placement", ad.placementId,
                                "line_item", ad.lineItemId,
                                "creative", ad.creative.id,
                                "type", "parentalGateSuccess"
                            }))
                    }));

            SATracking externalImpression = new SATracking();
            externalImpression.event = "impression";
            externalImpression.URL = ad.creative.impressionUrl;

            SATracking externalInstall = new SATracking();
            externalInstall.event = "install";
            externalInstall.URL = ad.creative.installUrl;

            // add events to the events array

            ad.creative.events.add(clickEvt);
            ad.creative.events.add(viewableImpression);
            ad.creative.events.add(parentalGateClose);
            ad.creative.events.add(parentalGateFail);
            ad.creative.events.add(parentalGateOpen);
            ad.creative.events.add(parentalGateSuccess);
            ad.creative.events.add(saImpressionEvt);
            if (ad.creative.impressionUrl != null) {
                ad.creative.events.add(externalImpression);
            }
            if (ad.creative.installUrl != null) {
                ad.creative.events.add(externalInstall);
            }

            // cdn url
            switch (ad.creative.creativeFormat) {
                case tag:
                case invalid: { break; }
                case appwall:
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

        } catch (Exception e) {
            return null;
        }
    }

}
