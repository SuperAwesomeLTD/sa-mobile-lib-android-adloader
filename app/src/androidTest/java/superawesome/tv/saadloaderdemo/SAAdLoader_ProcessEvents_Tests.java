package superawesome.tv.saadloaderdemo;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tv.superawesome.lib.saadloader.postprocessor.SAProcessEvents;
import tv.superawesome.lib.samodelspace.SAAd;
import tv.superawesome.lib.samodelspace.SATracking;
import tv.superawesome.lib.sasession.SASession;

public class SAAdLoader_ProcessEvents_Tests extends ApplicationTestCase<Application> {

    public SAAdLoader_ProcessEvents_Tests() {
        super(Application.class);
    }

    @SmallTest
    public void testEvents () {

        SAAd ad = new SAAd();
        ad.placementId = 4001;
        ad.lineItemId = 2731;
        ad.creative.id = 1092;
        ad.creative.impressionUrl = "https://superawesome.tv/impression";
        ad.creative.installUrl = "https://superawesome.tv/install";
        ad.creative.clickCounterUrl = "https://superawesome.tv/click_counter";

        SASession session = new SASession(getContext());
        session.setVersion("3.2.1");
        session.setConfigurationProduction();
        session.disableTestMode();

        SAProcessEvents.addAdEvents(ad, session);

        int expected_events = 10;

        int expected_sa_tracking = 1;
        int expected_viewable_impr = 1;
        int expected_pg_close = 1;
        int expected_pg_fail = 1;
        int expected_pg_open = 1;
        int expected_pg_success = 1;
        int expected_sa_impr = 1;
        int expected_impression = 1;
        int expected_install = 1;
        int expected_clk_counter = 1;

        assertNotNull(ad.creative.events);
        assertEquals(expected_events, ad.creative.events.size());

        List<SATracking> sa_tracking = new ArrayList<>();
        List<SATracking> viewable_impr = new ArrayList<>();
        List<SATracking> pg_close = new ArrayList<>();
        List<SATracking> pg_fail = new ArrayList<>();
        List<SATracking> pg_open = new ArrayList<>();
        List<SATracking> pg_success = new ArrayList<>();
        List<SATracking> sa_impr = new ArrayList<>();
        List<SATracking> impression = new ArrayList<>();
        List<SATracking> install = new ArrayList<>();
        List<SATracking> clickCounter = new ArrayList<>();

        for (SATracking event : ad.creative.events) {
            if (event.event.equals("sa_tracking")) sa_tracking.add(event);
            if (event.event.equals("viewable_impr")) viewable_impr.add(event);
            if (event.event.equals("pg_close")) pg_close.add(event);
            if (event.event.equals("pg_fail")) pg_fail.add(event);
            if (event.event.equals("pg_open")) pg_open.add(event);
            if (event.event.equals("pg_success")) pg_success.add(event);
            if (event.event.equals("sa_impr")) sa_impr.add(event);
            if (event.event.equals("impression")) impression.add(event);
            if (event.event.equals("install")) install.add(event);
            if (event.event.equals("clk_counter")) clickCounter.add(event);
        }

        assertEquals(expected_sa_tracking, sa_tracking.size());
        assertEquals(expected_viewable_impr, viewable_impr.size());
        assertEquals(expected_pg_close, pg_close.size());
        assertEquals(expected_pg_fail, pg_fail.size());
        assertEquals(expected_pg_open, pg_open.size());
        assertEquals(expected_pg_success, pg_success.size());
        assertEquals(expected_sa_impr, sa_impr.size());
        assertEquals(expected_impression, impression.size());
        assertEquals(expected_install, install.size());
        assertEquals(expected_clk_counter, clickCounter.size());

        String sa_tracking_url = sa_tracking.get(0).URL;
        String viewable_impr_url = viewable_impr.get(0).URL;
        String pg_close_url = pg_close.get(0).URL;
        String pg_fail_url = pg_fail.get(0).URL;
        String pg_open_url = pg_open.get(0).URL;
        String pg_success_url = pg_success.get(0).URL;
        String sa_impr_url = sa_impr.get(0).URL;
        String impression_url = impression.get(0).URL;
        String install_url = install.get(0).URL;
        String click_counter_url = clickCounter.get(0).URL;

        assertNotNull(sa_tracking_url);
        assertNotNull(viewable_impr_url);
        assertNotNull(pg_close_url);
        assertNotNull(pg_fail_url);
        assertNotNull(pg_open_url);
        assertNotNull(pg_success_url);
        assertNotNull(sa_impr_url);
        assertNotNull(impression_url);
        assertNotNull(install_url);
        assertNotNull(click_counter_url);

        assertTrue(sa_tracking_url.contains("https://ads.superawesome.tv/v2/click"));
        assertTrue(sa_tracking_url.contains("placement=4001"));
        assertTrue(sa_tracking_url.contains("sourceBundle=superawesome.tv.saadloaderdemo"));
        assertTrue(sa_tracking_url.contains("creative=1092"));
        assertTrue(sa_tracking_url.contains("line_item=2731"));
        assertTrue(sa_tracking_url.contains("sdkVersion=3.2.1"));

        assertTrue(viewable_impr_url.contains("https://ads.superawesome.tv/v2/event"));
        assertTrue(viewable_impr_url.contains("sourceBundle=superawesome.tv.saadloaderdemo"));
        assertTrue(viewable_impr_url.contains("sdkVersion=3.2.1"));
        assertTrue(viewable_impr_url.contains("type%22%3A%22viewable_impression"));
        assertTrue(viewable_impr_url.contains("creative%22%3A1092"));
        assertTrue(viewable_impr_url.contains("line_item%22%3A2731"));
        assertTrue(viewable_impr_url.contains("placement%22%3A4001"));

        assertTrue(pg_close_url.contains("https://ads.superawesome.tv/v2/event"));
        assertTrue(pg_close_url.contains("sourceBundle=superawesome.tv.saadloaderdemo"));
        assertTrue(pg_close_url.contains("sdkVersion=3.2.1"));
        assertTrue(pg_close_url.contains("type%22%3A%22parentalGateClose"));
        assertTrue(pg_close_url.contains("creative%22%3A1092"));
        assertTrue(pg_close_url.contains("line_item%22%3A2731"));
        assertTrue(pg_close_url.contains("placement%22%3A4001"));

        assertTrue(pg_fail_url.contains("https://ads.superawesome.tv/v2/event"));
        assertTrue(pg_fail_url.contains("sourceBundle=superawesome.tv.saadloaderdemo"));
        assertTrue(pg_fail_url.contains("sdkVersion=3.2.1"));
        assertTrue(pg_fail_url.contains("type%22%3A%22parentalGateFail"));
        assertTrue(pg_fail_url.contains("creative%22%3A1092"));
        assertTrue(pg_fail_url.contains("line_item%22%3A2731"));
        assertTrue(pg_fail_url.contains("placement%22%3A4001"));

        assertTrue(pg_open_url.contains("https://ads.superawesome.tv/v2/event"));
        assertTrue(pg_open_url.contains("sourceBundle=superawesome.tv.saadloaderdemo"));
        assertTrue(pg_open_url.contains("sdkVersion=3.2.1"));
        assertTrue(pg_open_url.contains("type%22%3A%22parentalGateOpen"));
        assertTrue(pg_open_url.contains("creative%22%3A1092"));
        assertTrue(pg_open_url.contains("line_item%22%3A2731"));
        assertTrue(pg_open_url.contains("placement%22%3A4001"));

        assertTrue(pg_success_url.contains("https://ads.superawesome.tv/v2/event"));
        assertTrue(pg_success_url.contains("sourceBundle=superawesome.tv.saadloaderdemo"));
        assertTrue(pg_success_url.contains("sdkVersion=3.2.1"));
        assertTrue(pg_success_url.contains("type%22%3A%22parentalGateSuccess"));
        assertTrue(pg_success_url.contains("creative%22%3A1092"));
        assertTrue(pg_success_url.contains("line_item%22%3A2731"));
        assertTrue(pg_success_url.contains("placement%22%3A4001"));

        assertTrue(sa_impr_url.contains("https://ads.superawesome.tv/v2/impression"));
        assertTrue(sa_impr_url.contains("placement=4001"));
        assertTrue(sa_impr_url.contains("sourceBundle=superawesome.tv.saadloaderdemo"));
        assertTrue(sa_impr_url.contains("creative=1092"));
        assertTrue(sa_impr_url.contains("line_item=2731"));
        assertTrue(sa_impr_url.contains("sdkVersion=3.2.1"));

        assertTrue(impression_url.equals("https://superawesome.tv/impression"));
        assertTrue(install_url.equals("https://superawesome.tv/install"));
        assertTrue(click_counter_url.equals("https://superawesome.tv/click_counter"));
    }

    private boolean getHammingDistance (String expected, String existing, int maxDelta) {
        int cDelta = 0;
        if (existing.length() != expected.length()) return false;

        for (int i = 0; i < expected.length(); i++) {
            char expC = expected.charAt(i);
            char exiC = existing.charAt(i);
            if (expC != exiC) cDelta++;
        }

        return cDelta <= maxDelta;
    }
}
