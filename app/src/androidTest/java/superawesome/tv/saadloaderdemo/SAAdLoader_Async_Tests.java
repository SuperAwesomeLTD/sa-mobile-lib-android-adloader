package superawesome.tv.saadloaderdemo;

import android.app.Application;
import android.os.Looper;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;

import tv.superawesome.lib.saadloader.SALoader;
import tv.superawesome.lib.saadloader.SALoaderInterface;
import tv.superawesome.lib.samodelspace.SAAd;
import tv.superawesome.lib.samodelspace.SACampaignType;
import tv.superawesome.lib.samodelspace.SACreativeFormat;
import tv.superawesome.lib.samodelspace.SAResponse;
import tv.superawesome.lib.sasession.SASession;

public class SAAdLoader_Async_Tests extends ApplicationTestCase<Application> {

    public SAAdLoader_Async_Tests() {
        super(Application.class);
    }

    // number of async tests to run!!!
    private final static int TESTS = 9;

    @SmallTest
    public void testAds () {

        // prepare the looper
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }

        // have a count down latch with the nr of tests
        final CountDownLatch latch = new CountDownLatch(TESTS);

        SASession session = new SASession(getContext());
        session.setConfigurationProduction();
        session.enableTestMode();

        int placement1 = 30471;
        int placement2 = 30479;
        int placement3 = 20;

        SALoader loader = new SALoader(getContext());
        loader.loadAd(placement1, session, new SALoaderInterface() {
            @Override
            public void didLoadAd(SAResponse response) {

                // only test for relevant things
                int expected_ads = 1;

                int expected_placementId = 30471;
                int expected_status = 200;
                SACreativeFormat expected_format = SACreativeFormat.image;

                int expected_ad_placementId = 30471;
                int expected_ad_lineItemId = -1;
                int expected_ad_campaignId = 0;
                int expected_ad_advertiserId = 0;
                boolean expected_ad_isFallback = false;
                boolean expected_ad_isHouse = false;
                boolean expected_ad_safeAdApproved = false;
                boolean expected_ad_show_padlock = true;
                int expected_ad_campaignType = 0;
                SACampaignType expected_ad_saCampaignType = SACampaignType.CPM;

                int expected_creative_id = -1;
                String expected_creative_click_url = "http://superawesome.tv";
                String expected_creative_impression_url = null;
                String expected_creative_installUrl = null;
                String expected_creative_format = "image_with_link";
                SACreativeFormat expected_creative_creativeFormat = SACreativeFormat.image;
                String expected_creative_bundleId = null;
                int expected_creative_events = 9;

                String expected_details_image = "https://ads.superawesome.tv/v2/demo_images/320x50.jpg";
                String expected_details_url = "https://ads.superawesome.tv/v2/demo_images/320x50.jpg";
                String expected_details_video = "https://ads.superawesome.tv/v2/demo_images/320x50.jpg";
                String expected_details_cdnUrl = "https://ads.superawesome.tv/v2/demo_images/";
                String expected_details_tag = null;
                String expected_details_vast = null;

                String expected_media_playableDiskUrl = null;
                String expected_media_playableMediaUrl = null;
                boolean expected_media_isOnDisk = false;

                assertNotNull(response);
                assertTrue(response.isValid());
                assertNotNull(response.ads);
                assertEquals(expected_ads, response.ads.size());

                SAAd ad = response.ads.get(0);
                assertNotNull(ad);
                assertTrue(ad.isValid());
                assertNotNull(ad.creative);
                assertNotNull(ad.creative.details);
                assertNotNull(ad.creative.details.media);

                assertEquals(expected_placementId, response.placementId);
                assertEquals(expected_status, response.status);
                assertEquals(expected_format, response.format);

                assertEquals(expected_ad_placementId, ad.placementId);
                assertEquals(expected_ad_lineItemId, ad.lineItemId);
                assertEquals(expected_ad_campaignId, ad.campaignId);
                assertEquals(expected_ad_advertiserId, ad.advertiserId);
                assertEquals(expected_ad_isFallback, ad.isFallback);
                assertEquals(expected_ad_isHouse, ad.isHouse);
                assertEquals(expected_ad_safeAdApproved, ad.safeAdApproved);
                assertEquals(expected_ad_show_padlock, ad.showPadlock);
                assertEquals(expected_ad_campaignType, ad.campaignType);
                assertEquals(expected_ad_saCampaignType, ad.saCampaignType);

                assertEquals(expected_creative_id, ad.creative.id);
                assertEquals(expected_creative_click_url, ad.creative.clickUrl);
                assertEquals(expected_creative_impression_url, ad.creative.impressionUrl);
                assertEquals(expected_creative_installUrl, ad.creative.installUrl);
                assertEquals(expected_creative_format, ad.creative.format);
                assertEquals(expected_creative_creativeFormat, ad.creative.creativeFormat);
                assertEquals(expected_creative_bundleId, ad.creative.bundleId);
                assertEquals(expected_creative_events, ad.creative.events.size());

                assertEquals(expected_details_image, ad.creative.details.image);
                assertEquals(expected_details_url, ad.creative.details.url);
                assertEquals(expected_details_video, ad.creative.details.video);
                assertEquals(expected_details_cdnUrl, ad.creative.details.cdnUrl);
                assertEquals(expected_details_tag, ad.creative.details.tag);
                assertEquals(expected_details_vast, ad.creative.details.vast);

                assertNotNull(ad.creative.details.media.html);
                assertEquals(expected_media_playableDiskUrl, ad.creative.details.media.playableDiskUrl);
                assertEquals(expected_media_playableMediaUrl, ad.creative.details.media.playableMediaUrl);
                assertEquals(expected_media_isOnDisk, ad.creative.details.media.isOnDisk);

                latch.countDown();
                if (latch.getCount() == 0) {
                    Looper.myLooper().quit();
                }
            }
        });

        loader.loadAd(placement2, session, new SALoaderInterface() {
            @Override
            public void didLoadAd(SAResponse response) {

                Log.d("SuperAwesome", "Resp " + response.writeToJson().toString());

                // only test for relevant things
                int expected_ads = 1;

                int expected_placementId = 30479;
                int expected_status = 200;
                SACreativeFormat expected_format = SACreativeFormat.video;

                int expected_ad_placementId = 30479;
                int expected_ad_lineItemId = -1;
                int expected_ad_campaignId = 0;
                int expected_ad_advertiserId = 0;
                boolean expected_ad_isFallback = false;
                boolean expected_ad_isHouse = false;
                boolean expected_ad_safeAdApproved = false;
                boolean expected_ad_show_padlock = true;
                int expected_ad_campaignType = 0;
                SACampaignType expected_ad_saCampaignType = SACampaignType.CPM;

                int expected_creative_id = -1;
                String expected_creative_click_url = "http://superawesome.tv";
                String expected_creative_impression_url = null;
                String expected_creative_installUrl = null;
                String expected_creative_format = "video";
                SACreativeFormat expected_creative_creativeFormat = SACreativeFormat.video;
                String expected_creative_bundleId = null;
                int expected_creative_events = 24;

                String expected_details_image = "https://ads.superawesome.tv/v2/demo_images/video.mp4";
                String expected_details_url = "https://ads.superawesome.tv/v2/demo_images/video.mp4";
                String expected_details_video = "https://ads.superawesome.tv/v2/demo_images/video.mp4";
                String expected_details_cdnUrl = "https://ads.superawesome.tv/v2/demo_images/";
                String expected_details_tag = null;
                String expected_details_vast = "https://ads.superawesome.tv/v2/video/vast/30479/-1/-1/?sdkVersion=0.0.0&rnd=";

                String expected_media_playableDiskUrl = "samov_";
                String expected_media_playableMediaUrl = "https://ads.superawesome.tv/v2/demo_images/video.mp4";
                boolean expected_media_isOnDisk = true;

                assertNotNull(response);
                assertTrue(response.isValid());
                assertNotNull(response.ads);
                assertEquals(expected_ads, response.ads.size());

                SAAd ad = response.ads.get(0);
                assertNotNull(ad);
                assertTrue(ad.isValid());
                assertNotNull(ad.creative);
                assertNotNull(ad.creative.details);
                assertNotNull(ad.creative.details.media);

                assertEquals(expected_placementId, response.placementId);
                assertEquals(expected_status, response.status);
                assertEquals(expected_format, response.format);

                assertEquals(expected_ad_placementId, ad.placementId);
                assertEquals(expected_ad_lineItemId, ad.lineItemId);
                assertEquals(expected_ad_campaignId, ad.campaignId);
                assertEquals(expected_ad_advertiserId, ad.advertiserId);
                assertEquals(expected_ad_isFallback, ad.isFallback);
                assertEquals(expected_ad_isHouse, ad.isHouse);
                assertEquals(expected_ad_safeAdApproved, ad.safeAdApproved);
                assertEquals(expected_ad_show_padlock, ad.showPadlock);
                assertEquals(expected_ad_campaignType, ad.campaignType);
                assertEquals(expected_ad_saCampaignType, ad.saCampaignType);

                assertEquals(expected_creative_id, ad.creative.id);
                assertEquals(expected_creative_click_url, ad.creative.clickUrl);
                assertEquals(expected_creative_impression_url, ad.creative.impressionUrl);
                assertEquals(expected_creative_installUrl, ad.creative.installUrl);
                assertEquals(expected_creative_format, ad.creative.format);
                assertEquals(expected_creative_creativeFormat, ad.creative.creativeFormat);
                assertEquals(expected_creative_bundleId, ad.creative.bundleId);
                assertEquals(expected_creative_events, ad.creative.events.size());

                assertEquals(expected_details_image, ad.creative.details.image);
                assertEquals(expected_details_url, ad.creative.details.url);
                assertEquals(expected_details_video, ad.creative.details.video);
                assertEquals(expected_details_cdnUrl, ad.creative.details.cdnUrl);
                assertEquals(expected_details_tag, ad.creative.details.tag);
                assertTrue(ad.creative.details.vast.contains(expected_details_vast));

                assertNull(ad.creative.details.media.html);
                assertTrue(ad.creative.details.media.playableDiskUrl.contains(expected_media_playableDiskUrl));
                assertEquals(expected_media_playableMediaUrl, ad.creative.details.media.playableMediaUrl);
                assertEquals(expected_media_isOnDisk, ad.creative.details.media.isOnDisk);

                latch.countDown();
                if (latch.getCount() == 0) {
                    Looper.myLooper().quit();
                }
            }
        });

        loader.loadAd(placement3, session, new SALoaderInterface() {
            @Override
            public void didLoadAd(SAResponse response) {

                // only test for relevant things
                int expected_ads = 1;

                int expected_placementId = 20;
                int expected_status = 404;
                SACreativeFormat expected_format = SACreativeFormat.invalid;

                int expected_ad_placementId = 20;
                int expected_ad_lineItemId = 0;
                int expected_ad_campaignId = 0;
                int expected_ad_advertiserId = 0;
                boolean expected_ad_isFallback = false;
                boolean expected_ad_isHouse = false;
                boolean expected_ad_safeAdApproved = false;
                boolean expected_ad_show_padlock = false;
                int expected_ad_campaignType = 0;
                SACampaignType expected_ad_saCampaignType = SACampaignType.CPM;

                int expected_creative_id = 0;
                String expected_creative_click_url = null;
                String expected_creative_impression_url = null;
                String expected_creative_installUrl = null;
                String expected_creative_format = null;
                SACreativeFormat expected_creative_creativeFormat = SACreativeFormat.invalid;
                String expected_creative_bundleId = null;
                int expected_creative_events = 9;

                String expected_details_image = null;
                String expected_details_url = null;
                String expected_details_video = null;
                String expected_details_cdnUrl = null;
                String expected_details_tag = null;
                String expected_details_vast = null;

                String expected_media_playableDiskUrl = null;
                String expected_media_playableMediaUrl = null;
                boolean expected_media_isOnDisk = false;

                assertNotNull(response);
                assertFalse(response.isValid());
                assertNotNull(response.ads);
                assertEquals(expected_ads, response.ads.size());

                SAAd ad = response.ads.get(0);
                assertNotNull(ad);
                assertFalse(ad.isValid());
                assertNotNull(ad.creative);
                assertNotNull(ad.creative.details);
                assertNotNull(ad.creative.details.media);

                assertEquals(expected_placementId, response.placementId);
                assertEquals(expected_status, response.status);
                assertEquals(expected_format, response.format);

                assertEquals(expected_ad_placementId, ad.placementId);
                assertEquals(expected_ad_lineItemId, ad.lineItemId);
                assertEquals(expected_ad_campaignId, ad.campaignId);
                assertEquals(expected_ad_advertiserId, ad.advertiserId);
                assertEquals(expected_ad_isFallback, ad.isFallback);
                assertEquals(expected_ad_isHouse, ad.isHouse);
                assertEquals(expected_ad_safeAdApproved, ad.safeAdApproved);
                assertEquals(expected_ad_show_padlock, ad.showPadlock);
                assertEquals(expected_ad_campaignType, ad.campaignType);
                assertEquals(expected_ad_saCampaignType, ad.saCampaignType);

                assertEquals(expected_creative_id, ad.creative.id);
                assertEquals(expected_creative_click_url, ad.creative.clickUrl);
                assertEquals(expected_creative_impression_url, ad.creative.impressionUrl);
                assertEquals(expected_creative_installUrl, ad.creative.installUrl);
                assertEquals(expected_creative_format, ad.creative.format);
                assertEquals(expected_creative_creativeFormat, ad.creative.creativeFormat);
                assertEquals(expected_creative_bundleId, ad.creative.bundleId);
                assertEquals(expected_creative_events, ad.creative.events.size());

                assertEquals(expected_details_image, ad.creative.details.image);
                assertEquals(expected_details_url, ad.creative.details.url);
                assertEquals(expected_details_video, ad.creative.details.video);
                assertEquals(expected_details_cdnUrl, ad.creative.details.cdnUrl);
                assertEquals(expected_details_tag, ad.creative.details.tag);
                assertEquals(expected_details_vast, ad.creative.details.vast);

                assertNull(ad.creative.details.media.html);
                assertEquals(expected_media_playableDiskUrl, ad.creative.details.media.playableDiskUrl);
                assertEquals(expected_media_playableMediaUrl, ad.creative.details.media.playableMediaUrl);
                assertEquals(expected_media_isOnDisk, ad.creative.details.media.isOnDisk);

                latch.countDown();
                if (latch.getCount() == 0) {
                    Looper.myLooper().quit();
                }
            }
        });

        String adurl1 = "https://raw.githubusercontent.com/SuperAwesomeLTD/sa-mobile-lib-android-adloader/master/samples/imagead.json";
        String adurl2 = "https://raw.githubusercontent.com/SuperAwesomeLTD/sa-mobile-lib-android-adloader/master/samples/richmediaad.json";
        String adurl3 = "https://raw.githubusercontent.com/SuperAwesomeLTD/sa-mobile-lib-android-adloader/master/samples/tagad.json";
        String adurl4 = "https://raw.githubusercontent.com/SuperAwesomeLTD/sa-mobile-lib-android-adloader/master/samples/videoad.json";
        String adurl5 = "https://raw.githubusercontent.com/SuperAwesomeLTD/sa-mobile-lib-android-adloader/master/samples/appwallad.json";
        String adurl6 = "https://raw.githubusercontent.com/SuperAwesomeLTD/sa-mobile-lib-android-adloader/master/samples/videoad2.json";

        loader.loadAd(adurl1, new JSONObject(), new JSONObject(), 100, session, new SALoaderInterface() {
            @Override
            public void didLoadAd(SAResponse response) {

                // only test for relevant things
                int expected_ads = 1;

                int expected_placementId = 100;
                int expected_status = 200;
                SACreativeFormat expected_format = SACreativeFormat.image;

                int expected_ad_placementId = 100;
                int expected_ad_lineItemId = 1052;
                int expected_ad_campaignId = 1209;
                int expected_ad_advertiserId = 1;
                boolean expected_ad_isFallback = false;
                boolean expected_ad_isHouse = false;
                boolean expected_ad_safeAdApproved = true;
                boolean expected_ad_show_padlock = true;
                int expected_ad_campaignType = 0;
                SACampaignType expected_ad_saCampaignType = SACampaignType.CPM;

                int expected_creative_id = 5768;
                String expected_creative_click_url = "https://superawesome.tv";
                String expected_creative_impression_url = null;
                String expected_creative_installUrl = null;
                String expected_creative_format = "image_with_link";
                SACreativeFormat expected_creative_creativeFormat = SACreativeFormat.image;
                String expected_creative_bundleId = null;
                int expected_creative_events = 9;

                String expected_details_image = "https://s3-eu-west-1.amazonaws.com/sb-ads-uploads/images/9Q4SVblKKIWDBJm537HFrqI6rBxjCdb9.jpg";
                String expected_details_url = "https://s3-eu-west-1.amazonaws.com/sb-ads-uploads/images/9Q4SVblKKIWDBJm537HFrqI6rBxjCdb9.jpg";
                String expected_details_video = "https://s3-eu-west-1.amazonaws.com/sb-ads-uploads/images/9Q4SVblKKIWDBJm537HFrqI6rBxjCdb9.jpg";
                String expected_details_cdnUrl = "https://s3-eu-west-1.amazonaws.com/sb-ads-uploads/images/";
                String expected_details_tag = null;
                String expected_details_vast = null;

                String expected_media_playableDiskUrl = null;
                String expected_media_playableMediaUrl = null;
                boolean expected_media_isOnDisk = false;

                assertNotNull(response);
                assertTrue(response.isValid());
                assertNotNull(response.ads);
                assertEquals(expected_ads, response.ads.size());

                SAAd ad = response.ads.get(0);
                assertNotNull(ad);
                assertTrue(ad.isValid());
                assertNotNull(ad.creative);
                assertNotNull(ad.creative.details);
                assertNotNull(ad.creative.details.media);

                assertEquals(expected_placementId, response.placementId);
                assertEquals(expected_status, response.status);
                assertEquals(expected_format, response.format);

                assertEquals(expected_ad_placementId, ad.placementId);
                assertEquals(expected_ad_lineItemId, ad.lineItemId);
                assertEquals(expected_ad_campaignId, ad.campaignId);
                assertEquals(expected_ad_advertiserId, ad.advertiserId);
                assertEquals(expected_ad_isFallback, ad.isFallback);
                assertEquals(expected_ad_isHouse, ad.isHouse);
                assertEquals(expected_ad_safeAdApproved, ad.safeAdApproved);
                assertEquals(expected_ad_show_padlock, ad.showPadlock);
                assertEquals(expected_ad_campaignType, ad.campaignType);
                assertEquals(expected_ad_saCampaignType, ad.saCampaignType);

                assertEquals(expected_creative_id, ad.creative.id);
                assertEquals(expected_creative_click_url, ad.creative.clickUrl);
                assertEquals(expected_creative_impression_url, ad.creative.impressionUrl);
                assertEquals(expected_creative_installUrl, ad.creative.installUrl);
                assertEquals(expected_creative_format, ad.creative.format);
                assertEquals(expected_creative_creativeFormat, ad.creative.creativeFormat);
                assertEquals(expected_creative_bundleId, ad.creative.bundleId);
                assertEquals(expected_creative_events, ad.creative.events.size());

                assertEquals(expected_details_image, ad.creative.details.image);
                assertEquals(expected_details_url, ad.creative.details.url);
                assertEquals(expected_details_video, ad.creative.details.video);
                assertEquals(expected_details_cdnUrl, ad.creative.details.cdnUrl);
                assertEquals(expected_details_tag, ad.creative.details.tag);
                assertEquals(expected_details_vast, ad.creative.details.vast);

                assertNotNull(ad.creative.details.media.html);
                assertEquals(expected_media_playableDiskUrl, ad.creative.details.media.playableDiskUrl);
                assertEquals(expected_media_playableMediaUrl, ad.creative.details.media.playableMediaUrl);
                assertEquals(expected_media_isOnDisk, ad.creative.details.media.isOnDisk);

                latch.countDown();
                if (latch.getCount() == 0) {
                    Looper.myLooper().quit();
                }
            }
        });

        loader.loadAd(adurl2, new JSONObject(), new JSONObject(), 100, session, new SALoaderInterface() {
            @Override
            public void didLoadAd(SAResponse response) {

                // only test for relevant things
                int expected_ads = 1;

                int expected_placementId = 100;
                int expected_status = 200;
                SACreativeFormat expected_format = SACreativeFormat.rich;

                int expected_ad_placementId = 100;
                int expected_ad_lineItemId = 9021;
                int expected_ad_campaignId = 2921;
                int expected_ad_advertiserId = 23;
                boolean expected_ad_isFallback = false;
                boolean expected_ad_isHouse = false;
                boolean expected_ad_safeAdApproved = true;
                boolean expected_ad_show_padlock = true;
                int expected_ad_campaignType = 0;
                SACampaignType expected_ad_saCampaignType = SACampaignType.CPM;

                int expected_creative_id = 8902;
                String expected_creative_click_url = "https://superawesome.tv";
                String expected_creative_impression_url = "https://superawesome.tv/v2/ad/ext_impression";
                String expected_creative_installUrl = null;
                String expected_creative_format = "rich_media";
                SACreativeFormat expected_creative_creativeFormat = SACreativeFormat.rich;
                String expected_creative_bundleId = null;
                int expected_creative_events = 9;

                String expected_details_image = "https://s3-eu-west-1.amazonaws.com/sb-ads-uploads/rich-media/tNmFLJ7kGQWBbyORkIqTJ4oqykaGPU9w/rich-media/index.html";
                String expected_details_url = "https://s3-eu-west-1.amazonaws.com/sb-ads-uploads/rich-media/tNmFLJ7kGQWBbyORkIqTJ4oqykaGPU9w/rich-media/index.html";
                String expected_details_video = "https://s3-eu-west-1.amazonaws.com/sb-ads-uploads/rich-media/tNmFLJ7kGQWBbyORkIqTJ4oqykaGPU9w/rich-media/index.html";
                String expected_details_cdnUrl = "https://s3-eu-west-1.amazonaws.com/sb-ads-uploads/rich-media/tNmFLJ7kGQWBbyORkIqTJ4oqykaGPU9w/rich-media/";
                String expected_details_tag = null;
                String expected_details_vast = null;

                String expected_media_playableDiskUrl = null;
                String expected_media_playableMediaUrl = null;
                boolean expected_media_isOnDisk = false;

                assertNotNull(response);
                assertTrue(response.isValid());
                assertNotNull(response.ads);
                assertEquals(expected_ads, response.ads.size());

                SAAd ad = response.ads.get(0);
                assertNotNull(ad);
                assertTrue(ad.isValid());
                assertNotNull(ad.creative);
                assertNotNull(ad.creative.details);
                assertNotNull(ad.creative.details.media);

                assertEquals(expected_placementId, response.placementId);
                assertEquals(expected_status, response.status);
                assertEquals(expected_format, response.format);

                assertEquals(expected_ad_placementId, ad.placementId);
                assertEquals(expected_ad_lineItemId, ad.lineItemId);
                assertEquals(expected_ad_campaignId, ad.campaignId);
                assertEquals(expected_ad_advertiserId, ad.advertiserId);
                assertEquals(expected_ad_isFallback, ad.isFallback);
                assertEquals(expected_ad_isHouse, ad.isHouse);
                assertEquals(expected_ad_safeAdApproved, ad.safeAdApproved);
                assertEquals(expected_ad_show_padlock, ad.showPadlock);
                assertEquals(expected_ad_campaignType, ad.campaignType);
                assertEquals(expected_ad_saCampaignType, ad.saCampaignType);

                assertEquals(expected_creative_id, ad.creative.id);
                assertEquals(expected_creative_click_url, ad.creative.clickUrl);
                assertEquals(expected_creative_impression_url, ad.creative.impressionUrl);
                assertEquals(expected_creative_installUrl, ad.creative.installUrl);
                assertEquals(expected_creative_format, ad.creative.format);
                assertEquals(expected_creative_creativeFormat, ad.creative.creativeFormat);
                assertEquals(expected_creative_bundleId, ad.creative.bundleId);
                assertEquals(expected_creative_events, ad.creative.events.size());

                assertEquals(expected_details_image, ad.creative.details.image);
                assertEquals(expected_details_url, ad.creative.details.url);
                assertEquals(expected_details_video, ad.creative.details.video);
                assertEquals(expected_details_cdnUrl, ad.creative.details.cdnUrl);
                assertEquals(expected_details_tag, ad.creative.details.tag);
                assertEquals(expected_details_vast, ad.creative.details.vast);

                assertNotNull(ad.creative.details.media.html);
                assertEquals(expected_media_playableDiskUrl, ad.creative.details.media.playableDiskUrl);
                assertEquals(expected_media_playableMediaUrl, ad.creative.details.media.playableMediaUrl);
                assertEquals(expected_media_isOnDisk, ad.creative.details.media.isOnDisk);

                latch.countDown();
                if (latch.getCount() == 0) {
                    Looper.myLooper().quit();
                }
            }
        });

        loader.loadAd(adurl3, new JSONObject(), new JSONObject(), 100, session, new SALoaderInterface() {
            @Override
            public void didLoadAd(SAResponse response) {

                Log.d("SuperAwesome", "resp" + response.writeToJson().toString());

                // only test for relevant things
                int expected_ads = 1;

                int expected_placementId = 100;
                int expected_status = 200;
                SACreativeFormat expected_format = SACreativeFormat.tag;

                int expected_ad_placementId = 100;
                int expected_ad_lineItemId = 8929;
                int expected_ad_campaignId = 2213;
                int expected_ad_advertiserId = 22;
                boolean expected_ad_isFallback = false;
                boolean expected_ad_isHouse = false;
                boolean expected_ad_safeAdApproved = true;
                boolean expected_ad_show_padlock = false;
                int expected_ad_campaignType = 0;
                SACampaignType expected_ad_saCampaignType = SACampaignType.CPM;

                int expected_creative_id = 21029;
                String expected_creative_click_url = "https://superawesome.tv";
                String expected_creative_impression_url = "https://superawesome.tv/v2/ad/ext_impression";
                String expected_creative_installUrl = null;
                String expected_creative_format = "tag";
                SACreativeFormat expected_creative_creativeFormat = SACreativeFormat.tag;
                String expected_creative_bundleId = null;
                int expected_creative_events = 9;

                String expected_details_image = null;
                String expected_details_url = null;
                String expected_details_video = null;
                String expected_details_cdnUrl = null;
                // String expected_details_tag = "<!-- Beginning PassBack for Ad unit FK:Site-Skyscraper-Passback ### size: [[120,600]] -->\\\\n\\\\t<script type='text/javascript' src='http://www.googletagservices.com/tag/js/gpt.js'>\\\\n\\\\t\\\\tgoogletag.pubads().definePassback('1002534/FK:Site-Skyscraper-Passback', [[120,600]]).display();\\\\n\\\\t</script>\\\\n<!-- End Passback -->";
                String expected_details_vast = null;

                String expected_media_playableDiskUrl = null;
                String expected_media_playableMediaUrl = null;
                boolean expected_media_isOnDisk = false;

                assertNotNull(response);
                assertTrue(response.isValid());
                assertNotNull(response.ads);
                assertEquals(expected_ads, response.ads.size());

                SAAd ad = response.ads.get(0);
                assertNotNull(ad);
                assertTrue(ad.isValid());
                assertNotNull(ad.creative);
                assertNotNull(ad.creative.details);
                assertNotNull(ad.creative.details.media);

                assertEquals(expected_placementId, response.placementId);
                assertEquals(expected_status, response.status);
                assertEquals(expected_format, response.format);

                assertEquals(expected_ad_placementId, ad.placementId);
                assertEquals(expected_ad_lineItemId, ad.lineItemId);
                assertEquals(expected_ad_campaignId, ad.campaignId);
                assertEquals(expected_ad_advertiserId, ad.advertiserId);
                assertEquals(expected_ad_isFallback, ad.isFallback);
                assertEquals(expected_ad_isHouse, ad.isHouse);
                assertEquals(expected_ad_safeAdApproved, ad.safeAdApproved);
                assertEquals(expected_ad_show_padlock, ad.showPadlock);
                assertEquals(expected_ad_campaignType, ad.campaignType);
                assertEquals(expected_ad_saCampaignType, ad.saCampaignType);

                assertEquals(expected_creative_id, ad.creative.id);
                assertEquals(expected_creative_click_url, ad.creative.clickUrl);
                assertEquals(expected_creative_impression_url, ad.creative.impressionUrl);
                assertEquals(expected_creative_installUrl, ad.creative.installUrl);
                assertEquals(expected_creative_format, ad.creative.format);
                assertEquals(expected_creative_creativeFormat, ad.creative.creativeFormat);
                assertEquals(expected_creative_bundleId, ad.creative.bundleId);
                assertEquals(expected_creative_events, ad.creative.events.size());

                assertEquals(expected_details_image, ad.creative.details.image);
                assertEquals(expected_details_url, ad.creative.details.url);
                assertEquals(expected_details_video, ad.creative.details.video);
                assertEquals(expected_details_cdnUrl, ad.creative.details.cdnUrl);
                assertNotNull(ad.creative.details.tag);
                assertEquals(expected_details_vast, ad.creative.details.vast);

                assertNotNull(ad.creative.details.media.html);
                assertEquals(expected_media_playableDiskUrl, ad.creative.details.media.playableDiskUrl);
                assertEquals(expected_media_playableMediaUrl, ad.creative.details.media.playableMediaUrl);
                assertEquals(expected_media_isOnDisk, ad.creative.details.media.isOnDisk);

                latch.countDown();
                if (latch.getCount() == 0) {
                    Looper.myLooper().quit();
                }
            }
        });

        loader.loadAd(adurl4, new JSONObject(), new JSONObject(), 100, session, new SALoaderInterface() {
            @Override
            public void didLoadAd(SAResponse response) {

                // only test for relevant things
                int expected_ads = 1;

                int expected_placementId = 100;
                int expected_status = 200;
                SACreativeFormat expected_format = SACreativeFormat.video;

                int expected_ad_placementId = 100;
                int expected_ad_lineItemId = 1054;
                int expected_ad_campaignId = 1209;
                int expected_ad_advertiserId = 1;
                boolean expected_ad_isFallback = false;
                boolean expected_ad_isHouse = false;
                boolean expected_ad_safeAdApproved = true;
                boolean expected_ad_show_padlock = true;
                int expected_ad_campaignType = 0;
                SACampaignType expected_ad_saCampaignType = SACampaignType.CPM;

                int expected_creative_id = 5770;
                String expected_creative_click_url = "https://superawesome.tv";
                String expected_creative_impression_url = null;
                String expected_creative_installUrl = null;
                String expected_creative_format = "video";
                SACreativeFormat expected_creative_creativeFormat = SACreativeFormat.video;
                String expected_creative_bundleId = null;
                int expected_creative_events = 49;

                String expected_details_image = "https://s3-eu-west-1.amazonaws.com/sb-ads-video-transcoded/yqbZXLY8b7p8dyIekHAnzySMwqOwA0HE.mp4";
                String expected_details_url = "https://s3-eu-west-1.amazonaws.com/sb-ads-video-transcoded/yqbZXLY8b7p8dyIekHAnzySMwqOwA0HE.mp4";
                String expected_details_video = "https://s3-eu-west-1.amazonaws.com/sb-ads-video-transcoded/yqbZXLY8b7p8dyIekHAnzySMwqOwA0HE.mp4";
                String expected_details_cdnUrl = "https://s3-eu-west-1.amazonaws.com/sb-ads-video-transcoded/";
                String expected_details_tag = null;
                String expected_details_vast = "https://raw.githubusercontent.com/SuperAwesomeLTD/sa-mobile-lib-android-vastparser/master/samples/VAST2.0.xml";

                String expected_media_playableDiskUrl = "samov_";
                String expected_media_playableMediaUrl = "https://ads.superawesome.tv/v2/demo_images/video.mp4";
                boolean expected_media_isOnDisk = true;

                assertNotNull(response);
                assertTrue(response.isValid());
                assertNotNull(response.ads);
                assertEquals(expected_ads, response.ads.size());

                SAAd ad = response.ads.get(0);
                assertNotNull(ad);
                assertTrue(ad.isValid());
                assertNotNull(ad.creative);
                assertNotNull(ad.creative.details);
                assertNotNull(ad.creative.details.media);

                assertEquals(expected_placementId, response.placementId);
                assertEquals(expected_status, response.status);
                assertEquals(expected_format, response.format);

                assertEquals(expected_ad_placementId, ad.placementId);
                assertEquals(expected_ad_lineItemId, ad.lineItemId);
                assertEquals(expected_ad_campaignId, ad.campaignId);
                assertEquals(expected_ad_advertiserId, ad.advertiserId);
                assertEquals(expected_ad_isFallback, ad.isFallback);
                assertEquals(expected_ad_isHouse, ad.isHouse);
                assertEquals(expected_ad_safeAdApproved, ad.safeAdApproved);
                assertEquals(expected_ad_show_padlock, ad.showPadlock);
                assertEquals(expected_ad_campaignType, ad.campaignType);
                assertEquals(expected_ad_saCampaignType, ad.saCampaignType);

                assertEquals(expected_creative_id, ad.creative.id);
                assertEquals(expected_creative_click_url, ad.creative.clickUrl);
                assertEquals(expected_creative_impression_url, ad.creative.impressionUrl);
                assertEquals(expected_creative_installUrl, ad.creative.installUrl);
                assertEquals(expected_creative_format, ad.creative.format);
                assertEquals(expected_creative_creativeFormat, ad.creative.creativeFormat);
                assertEquals(expected_creative_bundleId, ad.creative.bundleId);
                assertEquals(expected_creative_events, ad.creative.events.size());

                assertEquals(expected_details_image, ad.creative.details.image);
                assertEquals(expected_details_url, ad.creative.details.url);
                assertEquals(expected_details_video, ad.creative.details.video);
                assertEquals(expected_details_cdnUrl, ad.creative.details.cdnUrl);
                assertEquals(expected_details_tag, ad.creative.details.tag);
                assertEquals(expected_details_vast, ad.creative.details.vast);


                assertNull(ad.creative.details.media.html);
                assertEquals(expected_media_playableMediaUrl, ad.creative.details.media.playableMediaUrl);
                assertTrue(ad.creative.details.media.playableDiskUrl.contains(expected_media_playableDiskUrl));
                assertEquals(expected_media_isOnDisk, ad.creative.details.media.isOnDisk);

                latch.countDown();
                if (latch.getCount() == 0) {
                    Looper.myLooper().quit();
                }
            }
        });

        loader.loadAd(adurl5, new JSONObject(), new JSONObject(), 100, session, new SALoaderInterface() {
            @Override
            public void didLoadAd(SAResponse response) {

                // only test for relevant things
                int expected_ads = 2;

                int expected_placementId = 100;
                int expected_status = 200;
                SACreativeFormat expected_format = SACreativeFormat.appwall;

                int[] expected_ad_placementId = {100, 100};
                int[] expected_ad_lineItemId = {1075, 1076};
                int[] expected_ad_campaignId = {1227, 1227};
                int[] expected_ad_advertiserId = {1, 1};
                boolean[] expected_ad_isFallback = {false, false};
                boolean[] expected_ad_isHouse = {false, false};
                boolean[] expected_ad_safeAdApproved = {true, true};
                boolean[] expected_ad_show_padlock = {true, true};
                int[] expected_ad_campaignType = {1, 1};
                SACampaignType[] expected_ad_saCampaignType = {SACampaignType.CPI, SACampaignType.CPI};

                int[] expected_creative_id = {5792, 5793};
                String[] expected_creative_click_url = {"http://superawesome.tv", "http://www.superawesome.tv/en/kws"};
                String[] expected_creative_impression_url = {null, null};
                String[] expected_creative_installUrl = {"https://ads.superawesome.tv/install_1", null};
                String[] expected_creative_format = {"image_with_link", "image_with_link"};
                SACreativeFormat[] expected_creative_creativeFormat = {SACreativeFormat.appwall, SACreativeFormat.appwall};
                String[] expected_creative_bundleId = {"tv.superawesome.demoapp", "tv.superawesome.demoapp"};
                int[] expected_creative_events = {9, 9};

                String[] expected_details_image = {
                        "https://s3-eu-west-1.amazonaws.com/sb-ads-uploads/images/2ODwlbp3NJxnsmgROrdzXrxIUcD87h5y.png",
                        "https://s3-eu-west-1.amazonaws.com/sb-ads-uploads/images/TMRQ0iNyFEinXx2BQhkSONtEvCES7rsr.png"
                };
                String[] expected_details_url = {
                        "https://s3-eu-west-1.amazonaws.com/sb-ads-uploads/images/2ODwlbp3NJxnsmgROrdzXrxIUcD87h5y.png",
                        "https://s3-eu-west-1.amazonaws.com/sb-ads-uploads/images/TMRQ0iNyFEinXx2BQhkSONtEvCES7rsr.png"
                };
                String[] expected_details_video = {
                        "https://s3-eu-west-1.amazonaws.com/sb-ads-uploads/images/2ODwlbp3NJxnsmgROrdzXrxIUcD87h5y.png",
                        "https://s3-eu-west-1.amazonaws.com/sb-ads-uploads/images/TMRQ0iNyFEinXx2BQhkSONtEvCES7rsr.png"
                };
                String[] expected_details_cdnUrl = {
                        "https://s3-eu-west-1.amazonaws.com/sb-ads-uploads/images/",
                        "https://s3-eu-west-1.amazonaws.com/sb-ads-uploads/images/"
                };
                String[] expected_details_tag = {null, null};
                String[] expected_details_vast = {null, null};

                String[] expected_media_playableDiskUrl = {"samov_", "samov_"};
                String[] expected_media_playableMediaUrl = {
                        "https://s3-eu-west-1.amazonaws.com/sb-ads-uploads/images/2ODwlbp3NJxnsmgROrdzXrxIUcD87h5y.png",
                        "https://s3-eu-west-1.amazonaws.com/sb-ads-uploads/images/TMRQ0iNyFEinXx2BQhkSONtEvCES7rsr.png"
                };;
                boolean[] expected_media_isOnDisk = {true, true};

                assertNotNull(response);
                assertTrue(response.isValid());
                assertNotNull(response.ads);
                assertEquals(expected_ads, response.ads.size());

                for (int i = 0; i < response.ads.size(); i++) {
                    SAAd ad = response.ads.get(i);

                    assertNotNull(ad);
                    assertTrue(ad.isValid());
                    assertNotNull(ad.creative);
                    assertNotNull(ad.creative.details);
                    assertNotNull(ad.creative.details.media);

                    assertEquals(expected_placementId, response.placementId);
                    assertEquals(expected_status, response.status);
                    assertEquals(expected_format, response.format);

                    assertEquals(expected_ad_placementId[i], ad.placementId);
                    assertEquals(expected_ad_lineItemId[i], ad.lineItemId);
                    assertEquals(expected_ad_campaignId[i], ad.campaignId);
                    assertEquals(expected_ad_advertiserId[i], ad.advertiserId);
                    assertEquals(expected_ad_isFallback[i], ad.isFallback);
                    assertEquals(expected_ad_isHouse[i], ad.isHouse);
                    assertEquals(expected_ad_safeAdApproved[i], ad.safeAdApproved);
                    assertEquals(expected_ad_show_padlock[i], ad.showPadlock);
                    assertEquals(expected_ad_campaignType[i], ad.campaignType);
                    assertEquals(expected_ad_saCampaignType[i], ad.saCampaignType);

                    assertEquals(expected_creative_id[i], ad.creative.id);
                    assertEquals(expected_creative_click_url[i], ad.creative.clickUrl);
                    assertEquals(expected_creative_impression_url[i], ad.creative.impressionUrl);
                    assertEquals(expected_creative_installUrl[i], ad.creative.installUrl);
                    assertEquals(expected_creative_format[i], ad.creative.format);
                    assertEquals(expected_creative_creativeFormat[i], ad.creative.creativeFormat);
                    assertEquals(expected_creative_bundleId[i], ad.creative.bundleId);
                    assertEquals(expected_creative_events[i], ad.creative.events.size());

                    assertEquals(expected_details_image[i], ad.creative.details.image);
                    assertEquals(expected_details_url[i], ad.creative.details.url);
                    assertEquals(expected_details_video[i], ad.creative.details.video);
                    assertEquals(expected_details_cdnUrl[i], ad.creative.details.cdnUrl);
                    assertEquals(expected_details_tag[i], ad.creative.details.tag);
                    assertEquals(expected_details_vast[i], ad.creative.details.vast);

                    assertNull(ad.creative.details.media.html);
                    assertTrue(ad.creative.details.media.playableDiskUrl.contains(expected_media_playableDiskUrl[i]));
                    assertEquals(expected_media_playableMediaUrl[i], ad.creative.details.media.playableMediaUrl);
                    assertEquals(expected_media_isOnDisk[i], ad.creative.details.media.isOnDisk);
                }

                latch.countDown();
                if (latch.getCount() == 0) {
                    Looper.myLooper().quit();
                }
            }
        });

        loader.loadAd(adurl6, new JSONObject(), new JSONObject(), 100, session, new SALoaderInterface() {
            @Override
            public void didLoadAd(SAResponse response) {

                // only test for relevant things
                int expected_ads = 1;

                int expected_placementId = 100;
                int expected_status = 200;
                SACreativeFormat expected_format = SACreativeFormat.video;

                int expected_ad_placementId = 100;
                int expected_ad_lineItemId = 1110;
                int expected_ad_campaignId = 1420;
                int expected_ad_advertiserId = 1;
                boolean expected_ad_isFallback = false;
                boolean expected_ad_isHouse = false;
                boolean expected_ad_safeAdApproved = true;
                boolean expected_ad_show_padlock = true;
                int expected_ad_campaignType = 0;
                SACampaignType expected_ad_saCampaignType = SACampaignType.CPM;

                int expected_creative_id = 3330;
                String expected_creative_click_url = "https://superawesome.tv";
                String expected_creative_impression_url = null;
                String expected_creative_installUrl = null;
                String expected_creative_format = "video";
                SACreativeFormat expected_creative_creativeFormat = SACreativeFormat.video;
                String expected_creative_bundleId = null;
                int expected_creative_events = 39;

                String expected_details_image = "https://ads.superawesome.tv/v2/demo_images/video.mp4";
                String expected_details_url = "https://ads.superawesome.tv/v2/demo_images/video.mp4";
                String expected_details_video = "https://ads.superawesome.tv/v2/demo_images/video.mp4";
                String expected_details_cdnUrl = "https://ads.superawesome.tv/v2/demo_images/";
                String expected_details_tag = null;
                String expected_details_vast = "https://raw.githubusercontent.com/SuperAwesomeLTD/sa-mobile-lib-android-vastparser/master/samples/VAST5.0.xml";

                String expected_media_playableDiskUrl = "samov_";
                String expected_media_playableMediaUrl = "https://ads.superawesome.tv/v2/demo_images/video.mp4";
                boolean expected_media_isOnDisk = true;

                assertNotNull(response);
                assertTrue(response.isValid());
                assertNotNull(response.ads);
                assertEquals(expected_ads, response.ads.size());

                SAAd ad = response.ads.get(0);
                assertNotNull(ad);
                assertTrue(ad.isValid());
                assertNotNull(ad.creative);
                assertNotNull(ad.creative.details);
                assertNotNull(ad.creative.details.media);

                assertEquals(expected_placementId, response.placementId);
                assertEquals(expected_status, response.status);
                assertEquals(expected_format, response.format);

                assertEquals(expected_ad_placementId, ad.placementId);
                assertEquals(expected_ad_lineItemId, ad.lineItemId);
                assertEquals(expected_ad_campaignId, ad.campaignId);
                assertEquals(expected_ad_advertiserId, ad.advertiserId);
                assertEquals(expected_ad_isFallback, ad.isFallback);
                assertEquals(expected_ad_isHouse, ad.isHouse);
                assertEquals(expected_ad_safeAdApproved, ad.safeAdApproved);
                assertEquals(expected_ad_show_padlock, ad.showPadlock);
                assertEquals(expected_ad_campaignType, ad.campaignType);
                assertEquals(expected_ad_saCampaignType, ad.saCampaignType);

                assertEquals(expected_creative_id, ad.creative.id);
                assertEquals(expected_creative_click_url, ad.creative.clickUrl);
                assertEquals(expected_creative_impression_url, ad.creative.impressionUrl);
                assertEquals(expected_creative_installUrl, ad.creative.installUrl);
                assertEquals(expected_creative_format, ad.creative.format);
                assertEquals(expected_creative_creativeFormat, ad.creative.creativeFormat);
                assertEquals(expected_creative_bundleId, ad.creative.bundleId);
                assertEquals(expected_creative_events, ad.creative.events.size());

                assertEquals(expected_details_image, ad.creative.details.image);
                assertEquals(expected_details_url, ad.creative.details.url);
                assertEquals(expected_details_video, ad.creative.details.video);
                assertEquals(expected_details_cdnUrl, ad.creative.details.cdnUrl);
                assertEquals(expected_details_tag, ad.creative.details.tag);
                assertEquals(expected_details_vast, ad.creative.details.vast);


                assertNull(ad.creative.details.media.html);
                assertEquals(expected_media_playableMediaUrl, ad.creative.details.media.playableMediaUrl);
                assertTrue(ad.creative.details.media.playableDiskUrl.contains(expected_media_playableDiskUrl));
                assertEquals(expected_media_isOnDisk, ad.creative.details.media.isOnDisk);

                latch.countDown();
                if (latch.getCount() == 0) {
                    Looper.myLooper().quit();
                }
            }
        });


        // loopy
        Looper.loop();

    }

}