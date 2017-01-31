package superawesome.tv.saadloaderdemo;

import android.os.Looper;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.LargeTest;

import tv.superawesome.lib.saadloader.SALoader;
import tv.superawesome.lib.saadloader.SALoaderInterface;
import tv.superawesome.lib.samodelspace.SAAd;
import tv.superawesome.lib.samodelspace.SACampaignType;
import tv.superawesome.lib.samodelspace.SACreativeFormat;
import tv.superawesome.lib.samodelspace.SAResponse;
import tv.superawesome.lib.sasession.SASession;

/**
 * Created by gabriel.coman on 31/01/2017.
 */

public class SAAdLoader_Async_Tests2 extends ActivityInstrumentationTestCase2 {

    private static final int TIMEOUT = 2500;

    public SAAdLoader_Async_Tests2 () {
        super("superawesome.tv.saadloaderdemo", MainActivity.class);
    }

    public SAAdLoader_Async_Tests2(String pkg, Class activityClass) {
        super(pkg, activityClass);
    }

    public SAAdLoader_Async_Tests2(Class activityClass) {
        super(activityClass);
    }

    @LargeTest
    public void testAds1 () throws Throwable {

        final SASession session = new SASession(getActivity());
        session.setConfigurationProduction();
        session.enableTestMode();

        final int placement = 30471;

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {

                SALoader loader = new SALoader(getActivity());
                loader.loadAd(placement, session, new SALoaderInterface() {
                    @Override
                    public void saDidLoadAd(SAResponse response) {

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
                        SACampaignType expected_ad_campaignType = SACampaignType.CPM;

                        int expected_creative_id = -1;
                        String expected_creative_click_url = "http://superawesome.tv";
                        String expected_creative_impression_url = null;
                        String expected_creative_installUrl = null;
                        SACreativeFormat expected_creative_format = SACreativeFormat.image;
                        String expected_creative_bundleId = null;
                        int expected_creative_events = 10;

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

                        assertEquals(expected_creative_id, ad.creative.id);
                        assertEquals(expected_creative_click_url, ad.creative.clickUrl);
                        assertEquals(expected_creative_impression_url, ad.creative.impressionUrl);
                        assertEquals(expected_creative_installUrl, ad.creative.installUrl);
                        assertEquals(expected_creative_format, ad.creative.format);
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

                    }
                });
            }
        });

        sleep(TIMEOUT);
    }

    @LargeTest
    public void testAds2 () throws Throwable {

        final SASession session = new SASession(getActivity());
        session.setConfigurationProduction();
        session.enableTestMode();

        final int placement = 30479;

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {

                SALoader loader = new SALoader(getActivity());
                loader.loadAd(placement, session, new SALoaderInterface() {
                    @Override
                    public void saDidLoadAd(SAResponse response) {

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
                        SACampaignType expected_ad_campaignType = SACampaignType.CPM;

                        int expected_creative_id = -1;
                        String expected_creative_click_url = "http://superawesome.tv";
                        String expected_creative_impression_url = null;
                        String expected_creative_installUrl = null;
                        SACreativeFormat expected_creative_format = SACreativeFormat.video;
                        String expected_creative_bundleId = null;
                        int expected_creative_events = 25;

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

                        assertEquals(expected_creative_id, ad.creative.id);
                        assertEquals(expected_creative_click_url, ad.creative.clickUrl);
                        assertEquals(expected_creative_impression_url, ad.creative.impressionUrl);
                        assertEquals(expected_creative_installUrl, ad.creative.installUrl);
                        assertEquals(expected_creative_format, ad.creative.format);
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

                    }
                });
            }
        });

        sleep(TIMEOUT);

    }

    private void sleep(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            fail("Unexpected Timeout");
        }
    }
}
