package superawesome.tv.saadloaderdemo;


import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import tv.superawesome.lib.saadloader.postprocessor.SAProcessHTML;
import tv.superawesome.lib.samodelspace.SAAd;
import tv.superawesome.lib.samodelspace.SACreativeFormat;

public class SAAdLoader_ProcessHTML_Tests extends ApplicationTestCase<Application> {

    public SAAdLoader_ProcessHTML_Tests() {
        super(Application.class);
    }

    @SmallTest
    public void testProcessImage () {

        SAAd ad = new SAAd();
        ad.creative.creativeFormat = SACreativeFormat.image;
        ad.creative.details.image = "https://ads.superawesome.tv/v2/demo_images/320x50.jpg";
        ad.creative.details.media.html = SAProcessHTML.formatCreativeIntoImageHTML(ad);

        assertNotNull(ad.creative.details.media.html);
        assertTrue(ad.creative.details.media.html.contains("img#sa_image"));
        assertTrue(ad.creative.details.media.html.contains("<img id='sa_image' src='https://ads.superawesome.tv/v2/demo_images/320x50.jpg'/>"));
    }

    @SmallTest
    public void testProcessRichMedia () {

        SAAd ad = new SAAd();
        ad.placementId = 4091;
        ad.lineItemId = 2001;
        ad.creative.id = 2081;
        ad.creative.creativeFormat = SACreativeFormat.rich;
        ad.creative.details.url = "https://s3-eu-west-1.amazonaws.com/sb-ads-uploads/rich-media/tNmFLJ7kGQWBbyORkIqTJ4oqykaGPU9w/rich-media/index.html";
        ad.creative.details.media.html = SAProcessHTML.formatCreativeIntoRichMediaHTML(ad);

        Log.d("SuperAwesome", ad.creative.details.media.html);

        assertNotNull(ad.creative.details.media.html);
        assertTrue(ad.creative.details.media.html.contains("iframe { width: _WIDTH_px; height: _HEIGHT_px; border: 0; -webkit-transform-origin: 0 0; -webkit-transform: scale(_SCALE_); position:absolute; }"));
        assertTrue(ad.creative.details.media.html.contains("<iframe src='https://s3-eu-west-1.amazonaws.com/sb-ads-uploads/rich-media/tNmFLJ7kGQWBbyORkIqTJ4oqykaGPU9w/rich-media/index.html?placement=4091&line_item=2001&creative=2081&rnd="));
    }

    @SmallTest
    public void testProcessTag () {

        SAAd ad = new SAAd();
        ad.creative.creativeFormat = SACreativeFormat.tag;
        ad.creative.details.tag = "<!-- Beginning PassBack for Ad unit FK:Site-Skyscraper-Passback ### size: [[120,600]] -->\\\\n\\\\t<script type='text/javascript' src='http://www.googletagservices.com/tag/js/gpt.js'>\\\\n\\\\t\\\\tgoogletag.pubads().definePassback('1002534/FK:Site-Skyscraper-Passback', [[120,600]]).display();\\\\n\\\\t</script>\\\\n<!-- End Passback -->";
        ad.creative.details.media.html = SAProcessHTML.formatCreativeIntoTagHTML(ad);

        assertNotNull(ad.creative.details.media.html);
        assertTrue(ad.creative.details.media.html.contains("div#inner { width: _WIDTH_px; height: _HEIGHT_px; border: 0; -webkit-transform-origin: 0 0; -webkit-transform: scale(_SCALE_); position:absolute; }"));
        assertTrue(ad.creative.details.media.html.contains("<div id=\"inner\"><!-- Beginning PassBack for Ad unit FK:Site-Skyscraper-Passback ### size: [[120,600]] -->\\\\n\\\\t<script type='text/javascript' src='http://www.googletagservices.com/tag/js/gpt.js'>\\\\n\\\\t\\\\tgoogletag.pubads().definePassback('1002534/FK:Site-Skyscraper-Passback', [[120,600]]).display();\\\\n\\\\t</script>\\\\n<!-- End Passback --></div>"));
    }
}
