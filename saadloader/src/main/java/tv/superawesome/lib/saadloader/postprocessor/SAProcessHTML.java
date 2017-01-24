/**
 * @Copyright:   SuperAwesome Trading Limited 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package tv.superawesome.lib.saadloader.postprocessor;

import tv.superawesome.lib.samodelspace.SAAd;
import tv.superawesome.lib.samodelspace.SATracking;
import tv.superawesome.lib.sautils.SAUtils;

/**
 * Class that formats a specific HTML "website" for each type of ad that needs displaying.
 *  - For image ads a document with mostly an <img/> tag
 *  - For rich media ads a document with an <iframe> to load the content in
 *  - For external tags a div to write the tag in
 */
public class SAProcessHTML {

    /**
     * Method that loads a special HTML file and parse & format it so that later
     * on web views will be able to use it to display proper image data
     *
     * @param ad    ad data (as an SAAd object)
     * @return      the formatted HTML string to be used by a WebView
     */
    public static String formatCreativeIntoImageHTML(SAAd ad) {
        String htmlString = "<a href='_HREF_URL_' target='_blank'><img src='_IMAGE_URL_'/></a>_MOAT_";
        String click = ad.creative.clickUrl;

        if (click == null) {
            for (SATracking tracking : ad.creative.events) {
                if (tracking.event.equals("sa_tracking")) {
                    click = tracking.URL;
                }
            }
        }

        htmlString = htmlString.replace("_HREF_URL_", click != null ? click : "");
        htmlString = htmlString.replace("_IMAGE_URL_", ad.creative.details.image);
        return htmlString;
    }

    /**
     * Method that loads a special HTML file and parse & format it so that later
     * on web views will be able to use it to display proper rich media data
     *
     * @param ad    ad data (as an SAAd object)
     * @return      the formatted HTML string to be used by a WebView
     */
    public static String formatCreativeIntoRichMediaHTML(SAAd ad) {
        String htmlString = "<iframe style='padding:0;border:0;' width='100%' height='100%' src='_RICH_MEDIA_URL_'></iframe>";
        String richMediaURL = ad.creative.details.url +
                "?placement=" + ad.placementId +
                "&line_item=" + ad.lineItemId +
                "&creative=" + ad.creative.id +
                "&rnd=" + SAUtils.getCacheBuster();

        return htmlString.replace("_RICH_MEDIA_URL_", richMediaURL);
    }

    /**
     * Method that loads a special HTML file and parse & format it so that later
     * on web views will be able to use it to display proper tag data
     * @param ad    ad data (as an SAAd object)
     * @return      the formatted HTML string to be used by a WebView
     */
    public static String formatCreativeIntoTagHTML(SAAd ad) {
        String htmlString = "_TAGDATA__MOAT_";

        String click = ad.creative.clickUrl;

        if (click == null) {
            for (SATracking tracking : ad.creative.events) {
                if (tracking.event.equals("sa_tracking")) {
                    click = tracking.URL;
                }
            }
        }

        String tagString = ad.creative.details.tag;
        tagString = tagString.replace("[click]", click+ "&redir=");
        tagString = tagString.replace("[click_enc]", SAUtils.encodeURL(click));
        tagString = tagString.replace("[keywords]", "");
        tagString = tagString.replace("[timestamp]", "");
        tagString = tagString.replace("target=\"_blank\"", "");
        tagString = tagString.replace("“", "\"");

        return htmlString.replace("_TAGDATA_", tagString);
    }
}
