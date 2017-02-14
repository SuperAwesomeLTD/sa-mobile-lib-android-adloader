/**
 * @Copyright:   SuperAwesome Trading Limited 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package tv.superawesome.lib.saadloader.postprocessor;

import tv.superawesome.lib.sajsonparser.SAJsonParser;
import tv.superawesome.lib.samodelspace.SAAd;
import tv.superawesome.lib.samodelspace.SACreativeFormat;
import tv.superawesome.lib.samodelspace.SAReferralData;
import tv.superawesome.lib.samodelspace.SATracking;
import tv.superawesome.lib.sasession.SASession;
import tv.superawesome.lib.sautils.SAUtils;

/**
 * Class that, based on a given type of session (a SASession object) generates a number of custom
 * events that need to be added to an ad object (a SAAd object passed as parameter)
 */
public class SAProcessEvents {

    /**
     * Static method that looks at the current session and generates a number of additional
     * events that will be used by the SDK
     *
     * @param ad        reference to the Ad that the events should be appended to
     * @param session   the current session
     */
    public static void addAdEvents (SAAd ad, SASession session) {

        // create a new tracking event (click event), to be used if the ad's click will not
        // contain a SuperAwesome url at all
        SATracking clickEvt = new SATracking();
        clickEvt.event = "superawesome_click";
        clickEvt.URL = session.getBaseUrl() +
                (ad.creative.format == SACreativeFormat.video ? "/video/click/?" : "/click?") +
                SAUtils.formGetQueryFromDict(SAJsonParser.newObject(new Object[]{
                        "placement", ad.placementId,
                        "bundle", session.getPackageName(),
                        "creative", ad.creative.id,
                        "line_item", ad.lineItemId,
                        "ct", session.getConnectionType(),
                        "sdkVersion", session.getVersion(),
                        "rnd", session.getCachebuster()
                }));

        // create an impression event; this should be used by video ads (for the moment), and
        // sometime in the future by display ads
        SATracking saImpressionEvt = new SATracking();
        saImpressionEvt.event = "superawesome_impression";
        saImpressionEvt.URL = session.getBaseUrl() +
                "/impression?" +
                SAUtils.formGetQueryFromDict(SAJsonParser.newObject(new Object[] {
                        "placement", ad.placementId,
                        "creative", ad.creative.id,
                        "line_item", ad.lineItemId,
                        "sdkVersion", session.getVersion(),
                        "bundle", session.getPackageName(),
                        "ct", session.getConnectionType(),
                        "no_image", true,
                        "rnd", session.getCachebuster()
                }));

        // create a viewable impression event; this is triggered when the ad first shown on screen
        SATracking viewableImpression = new SATracking();
        viewableImpression.event = "superawesome_viewable_impression";
        viewableImpression.URL = session.getBaseUrl() +
                "/event?" +
                SAUtils.formGetQueryFromDict(SAJsonParser.newObject(new Object[]{
                        "sdkVersion", session.getVersion(),
                        "ct", session.getConnectionType(),
                        "bundle", session.getPackageName(),
                        "data", SAUtils.encodeDictAsJsonDict(SAJsonParser.newObject(new Object[] {
                            "placement", ad.placementId,
                            "line_item", ad.lineItemId,
                            "creative", ad.creative.id,
                            "type", "viewable_impression"
                        })),
                        "rnd", session.getCachebuster()
                }));

        // create a parental gate close event;
        SATracking parentalGateClose = new SATracking();
        parentalGateClose.event = "superawesome_pg_close";
        parentalGateClose.URL = session.getBaseUrl() +
                "/event?" +
                SAUtils.formGetQueryFromDict(SAJsonParser.newObject(new Object[]{
                        "sdkVersion", session.getVersion(),
                        "ct", session.getConnectionType(),
                        "bundle", session.getPackageName(),
                        "data", SAUtils.encodeDictAsJsonDict(SAJsonParser.newObject(new Object[]{
                            "placement", ad.placementId,
                            "line_item", ad.lineItemId,
                            "creative", ad.creative.id,
                            "type", "parentalGateClose",
                        })),
                        "rnd", session.getCachebuster()
                }));


        // create a parental gate open event
        SATracking parentalGateOpen = new SATracking();
        parentalGateOpen.event = "superawesome_pg_open";
        parentalGateOpen.URL = session.getBaseUrl() +
                "/event?" +
                SAUtils.formGetQueryFromDict(SAJsonParser.newObject(new Object[]{
                        "sdkVersion", session.getVersion(),
                        "ct", session.getConnectionType(),
                        "bundle", session.getPackageName(),
                        "data", SAUtils.encodeDictAsJsonDict(SAJsonParser.newObject(new Object[] {
                            "placement", ad.placementId,
                            "line_item", ad.lineItemId,
                            "creative", ad.creative.id,
                            "type", "parentalGateOpen"
                        })),
                        "rnd", session.getCachebuster()
                }));

        // create a parental gate fail event
        SATracking parentalGateFail = new SATracking();
        parentalGateFail.event = "superawesome_pg_fail";
        parentalGateFail.URL = session.getBaseUrl() +
                "/event?" +
                SAUtils.formGetQueryFromDict(SAJsonParser.newObject(new Object[]{
                        "sdkVersion", session.getVersion(),
                        "ct", session.getConnectionType(),
                        "bundle", session.getPackageName(),
                        "data", SAUtils.encodeDictAsJsonDict(SAJsonParser.newObject(new Object[]{
                            "placement", ad.placementId,
                            "line_item", ad.lineItemId,
                            "creative", ad.creative.id,
                            "type", "parentalGateFail"
                        })),
                        "rnd", session.getCachebuster()
                }));

        // create a parentla gate success event
        SATracking parentalGateSuccess = new SATracking();
        parentalGateSuccess.event = "superawesome_pg_success";
        parentalGateSuccess.URL = session.getBaseUrl() +
                "/event?" +
                SAUtils.formGetQueryFromDict(SAJsonParser.newObject(new Object[] {
                        "sdkVersion", session.getVersion(),
                        "ct", session.getConnectionType(),
                        "bundle", session.getPackageName(),
                        "data", SAUtils.encodeDictAsJsonDict(SAJsonParser.newObject(new Object[] {
                            "placement", ad.placementId,
                            "line_item", ad.lineItemId,
                            "creative", ad.creative.id,
                            "type", "parentalGateSuccess"
                        })),
                        "rnd", session.getCachebuster()
                }));

        // add events to the ads events array
        ad.creative.events.add(clickEvt);
        ad.creative.events.add(viewableImpression);
        ad.creative.events.add(parentalGateClose);
        ad.creative.events.add(parentalGateFail);
        ad.creative.events.add(parentalGateOpen);
        ad.creative.events.add(parentalGateSuccess);
        ad.creative.events.add(saImpressionEvt);
    }

    /**
     * Method that ads new referral data
     *
     * @param ad        the current ad
     * @param session   the current session
     */
    public static void addReferralSendData (SAAd ad, SASession session) {
        ad.creative.referralData = new SAReferralData(
                session.getConfiguration().ordinal(),
                ad.campaignId,
                ad.lineItemId,
                ad.creative.id,
                ad.placementId
        );
    }
}
