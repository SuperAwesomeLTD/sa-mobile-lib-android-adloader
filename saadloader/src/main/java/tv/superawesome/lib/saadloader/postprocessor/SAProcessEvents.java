/**
 * @Copyright:   SuperAwesome Trading Limited 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package tv.superawesome.lib.saadloader.postprocessor;

import tv.superawesome.lib.sajsonparser.SAJsonParser;
import tv.superawesome.lib.samodelspace.SAAd;
import tv.superawesome.lib.samodelspace.SACreativeFormat;
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
        clickEvt.event = "sa_tracking";
        clickEvt.URL = session.getBaseUrl() +
                (ad.creative.format == SACreativeFormat.video ? "/video/click/?" : "/click?") +
                SAUtils.formGetQueryFromDict(SAJsonParser.newObject(new Object[]{
                        "placement", ad.placementId,
                        "line_item", ad.lineItemId,
                        "creative", ad.creative.id,
                        "sdkVersion", session.getVersion(),
                        "sourceBundle", session.getPackageName(),
                        "rnd", session.getCachebuster(),
                        "ct", session.getConnectionType()
                }));

        // create an impression event; this should be used by video ads (for the moment), and
        // sometime in the future by display ads
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

        // create a viewable impression event; this is triggered when the ad first shown on screen
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

        // create a parental gate close event;
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


        // create a parental gate open event
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

        // create a parental gate fail event
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

        // create a parentla gate success event
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

        // create an external impression event; this needs to be triggered if the advertiser
        // has added an extra impression tracker to his campaign
        SATracking externalImpression = new SATracking();
        externalImpression.event = "impression";
        externalImpression.URL = ad.creative.impressionUrl;

        // create an external install event; this needs to be triggered if the advertiser has
        // added an extra install event tracker to his campaign
        SATracking externalInstall = new SATracking();
        externalInstall.event = "install";
        externalInstall.URL = ad.creative.installUrl;

        // add events to the ads events array
        ad.creative.events.add(clickEvt);
        ad.creative.events.add(viewableImpression);
        ad.creative.events.add(parentalGateClose);
        ad.creative.events.add(parentalGateFail);
        ad.creative.events.add(parentalGateOpen);
        ad.creative.events.add(parentalGateSuccess);
        ad.creative.events.add(saImpressionEvt);
        ad.creative.events.add(externalImpression);
        ad.creative.events.add(externalInstall);
    }

}
