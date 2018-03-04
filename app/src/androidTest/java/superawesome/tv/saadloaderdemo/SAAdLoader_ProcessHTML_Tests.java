package superawesome.tv.saadloaderdemo;


import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import tv.superawesome.lib.saadloader.postprocessor.SAProcessHTML;
import tv.superawesome.lib.samodelspace.saad.SAAd;
import tv.superawesome.lib.samodelspace.saad.SACreativeFormat;

public class SAAdLoader_ProcessHTML_Tests extends ApplicationTestCase<Application> {

    public SAAdLoader_ProcessHTML_Tests() {
        super(Application.class);
    }

    @SmallTest
    public void testProcessImageWithClick () {

        SAAd ad = new SAAd();
        ad.creative.format = SACreativeFormat.image;
        ad.creative.details.image = "https://ads.superawesome.tv/v2/demo_images/320x50.jpg";
        ad.creative.clickUrl = "http://hotnews.ro";
        ad.creative.details.media.html = SAProcessHTML.formatCreativeIntoImageHTML(ad);

        assertNotNull(ad.creative.details.media.html);
        assertTrue(ad.creative.details.media.html.contains("<img src='https://ads.superawesome.tv/v2/demo_images/320x50.jpg' width='100%' height='100%'/>"));
        assertTrue(ad.creative.details.media.html.contains("<a href='http://hotnews.ro' target='_blank'>"));
        assertTrue(ad.creative.details.media.html.contains("</a>"));
        assertTrue(ad.creative.details.media.html.equals("<a href='http://hotnews.ro' target='_blank'><img src='https://ads.superawesome.tv/v2/demo_images/320x50.jpg' width='100%' height='100%'/></a>_MOAT_"));
    }

    @SmallTest
    public void testProcessImageWithNoClick () {

        SAAd ad = new SAAd();
        ad.creative.format = SACreativeFormat.image;
        ad.creative.details.image = "https://ads.superawesome.tv/v2/demo_images/320x50.jpg";
        ad.creative.details.media.html = SAProcessHTML.formatCreativeIntoImageHTML(ad);

        assertNotNull(ad.creative.details.media.html);
        assertTrue(ad.creative.details.media.html.contains("<img src='https://ads.superawesome.tv/v2/demo_images/320x50.jpg' width='100%' height='100%'/>"));
        assertFalse(ad.creative.details.media.html.contains("<a href='http://hotnews.ro' target='_blank'>"));
        assertFalse(ad.creative.details.media.html.contains("</a>"));
        assertTrue(ad.creative.details.media.html.equals("<img src='https://ads.superawesome.tv/v2/demo_images/320x50.jpg' width='100%' height='100%'/>_MOAT_"));

    }

    @SmallTest
    public void testProcessRichMedia () {

        SAAd ad = new SAAd();
        ad.placementId = 4091;
        ad.lineItemId = 2001;
        ad.creative.id = 2081;
        ad.creative.format = SACreativeFormat.rich;
        ad.creative.details.url = "https://s3-eu-west-1.amazonaws.com/sb-ads-uploads/rich-media/tNmFLJ7kGQWBbyORkIqTJ4oqykaGPU9w/rich-media/index.html";
        ad.creative.details.media.html = SAProcessHTML.formatCreativeIntoRichMediaHTML(ad);

        Log.d("SuperAwesome", ad.creative.details.media.html);

        assertNotNull(ad.creative.details.media.html);
        assertTrue(ad.creative.details.media.html.contains("<iframe style='padding:0;border:0;' width='100%' height='100%' src='https://s3-eu-west-1.amazonaws.com/sb-ads-uploads/rich-media/tNmFLJ7kGQWBbyORkIqTJ4oqykaGPU9w/rich-media/index.html?placement=4091&line_item=2001&creative=2081&rnd="));
    }

    @SmallTest
    public void testProcessTag1 () {

        SAAd ad = new SAAd();
        ad.creative.format = SACreativeFormat.tag;
        ad.creative.details.tag = "<!-- Beginning PassBack for Ad unit FK:Site-Skyscraper-Passback ### size: [[120,600]] -->\\\\n\\\\t<script type='text/javascript' src='http://www.googletagservices.com/tag/js/gpt.js'>\\\\n\\\\t\\\\tgoogletag.pubads().definePassback('1002534/FK:Site-Skyscraper-Passback', [[120,600]]).display();\\\\n\\\\t</script>\\\\n<!-- End Passback -->";
        ad.creative.details.media.html = SAProcessHTML.formatCreativeIntoTagHTML(ad);

        assertNotNull(ad.creative.details.media.html);
        assertTrue(ad.creative.details.media.html.contains("<!-- Beginning PassBack for Ad unit FK:Site-Skyscraper-Passback ### size: [[120,600]] -->\\\\n\\\\t<script type='text/javascript' src='http://www.googletagservices.com/tag/js/gpt.js'>\\\\n\\\\t\\\\tgoogletag.pubads().definePassback('1002534/FK:Site-Skyscraper-Passback', [[120,600]]).display();\\\\n\\\\t</script>\\\\n<!-- End Passback -->"));
    }

    @SmallTest
    public void testProcessTag2 () {

        SAAd ad = new SAAd();
        ad.creative.format = SACreativeFormat.tag;
        ad.creative.details.tag = "<A HREF=\"[click]https://ad.doubleclick.net/ddm/jump/N304202.1915243SUPERAWESOME.TV/B10773905.144955457;sz=480x320;ord=1486394166729?\"><IMG SRC=\"https://ad.doubleclick.net/ddm/ad/N304202.1915243SUPERAWESOME.TV/B10773905.144955457;sz=480x320;ord=1486394166729;dc_lat=;dc_rdid=;tag_for_child_directed_treatment=?\" BORDER=0 WIDTH=480 HEIGHT=320 ALT=\"Advertisement\"></A>";
        ad.creative.details.media.html = SAProcessHTML.formatCreativeIntoTagHTML(ad);

        assertNotNull(ad.creative.details.media.html);
        assertTrue(ad.creative.details.media.html.contains("<A HREF=\"https://ad.doubleclick.net/ddm/jump/N304202.1915243SUPERAWESOME.TV/B10773905.144955457;sz=480x320;ord=1486394166729?\"><IMG SRC=\"https://ad.doubleclick.net/ddm/ad/N304202.1915243SUPERAWESOME.TV/B10773905.144955457;sz=480x320;ord=1486394166729;dc_lat=;dc_rdid=;tag_for_child_directed_treatment=?\" BORDER=0 WIDTH=480 HEIGHT=320 ALT=\"Advertisement\"></A>_MOAT_"));
    }

    @SmallTest
    public void testProcessTag3 () {

        SAAd ad = new SAAd();
        ad.creative.format = SACreativeFormat.tag;
        ad.creative.details.tag = "<A HREF=\"[click]https://ad.doubleclick.net/ddm/jump/N304202.1915243SUPERAWESOME.TV/B10773905.144955457;sz=480x320;ord=1486394166729?\"><IMG SRC=\"https://ad.doubleclick.net/ddm/ad/N304202.1915243SUPERAWESOME.TV/B10773905.144955457;sz=480x320;ord=1486394166729;dc_lat=;dc_rdid=;tag_for_child_directed_treatment=?\" BORDER=0 WIDTH=480 HEIGHT=320 ALT=\"Advertisement\"></A>";
        ad.creative.clickUrl = "http://hotnews.ro";
        ad.creative.details.media.html = SAProcessHTML.formatCreativeIntoTagHTML(ad);

        assertNotNull(ad.creative.details.media.html);
        assertTrue(ad.creative.details.media.html.contains("<A HREF=\"http://hotnews.ro&redir=https://ad.doubleclick.net/ddm/jump/N304202.1915243SUPERAWESOME.TV/B10773905.144955457;sz=480x320;ord=1486394166729?\"><IMG SRC=\"https://ad.doubleclick.net/ddm/ad/N304202.1915243SUPERAWESOME.TV/B10773905.144955457;sz=480x320;ord=1486394166729;dc_lat=;dc_rdid=;tag_for_child_directed_treatment=?\" BORDER=0 WIDTH=480 HEIGHT=320 ALT=\"Advertisement\"></A>_MOAT_"));
    }
}
