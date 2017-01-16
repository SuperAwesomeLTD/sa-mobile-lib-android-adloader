package superawesome.tv.saadloaderdemo;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

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

        SASession session = new SASession(getContext());
        session.setConfigurationProduction();
        session.disableTestMode();

        SAProcessEvents.addAdEvents(ad, session);

        int expected_events = 9;

        int expected_sa_tracking = 1;
        int expected_viewable_impr = 1;
        int expected_pg_close = 1;
        int expected_pg_fail = 1;
        int expected_pg_open = 1;
        int expected_pg_success = 1;
        int expected_sa_impr = 1;
        int expected_impression = 1;
        int expected_install = 1;

        String expected_sa_tracking_url = "https://ads.superawesome.tv/v2/click?placement=4001&rnd=1026797&sourceBundle=superawesome.tv.saadloaderdemo&creative=1092&line_item=2731&ct=wifi&sdkVersion=0.0.0";
        String expected_viewable_impr_url = "https://ads.superawesome.tv/v2/event?data=%7B%22type%22%3A%22viewable_impression%22%2C%22creative%22%3A1092%2C%22line_item%22%3A2731%2C%22placement%22%3A4001%7D&sourceBundle=superawesome.tv.saadloaderdemo&rnd=1115679&ct=wifi&sdkVersion=0.0.0";
        String expected_pg_close_url = "https://ads.superawesome.tv/v2/event?data=%7B%22type%22%3A%22parentalGateClose%22%2C%22creative%22%3A1092%2C%22line_item%22%3A2731%2C%22placement%22%3A4001%7D&sourceBundle=superawesome.tv.saadloaderdemo&rnd=1123630&ct=wifi&sdkVersion=0.0.0";
        String expected_pg_fail_url = "https://ads.superawesome.tv/v2/event?data=%7B%22type%22%3A%22parentalGateFail%22%2C%22creative%22%3A1092%2C%22line_item%22%3A2731%2C%22placement%22%3A4001%7D&sourceBundle=superawesome.tv.saadloaderdemo&rnd=1468009&ct=wifi&sdkVersion=0.0.0";
        String expected_pg_open_url = "https://ads.superawesome.tv/v2/event?data=%7B%22type%22%3A%22parentalGateOpen%22%2C%22creative%22%3A1092%2C%22line_item%22%3A2731%2C%22placement%22%3A4001%7D&sourceBundle=superawesome.tv.saadloaderdemo&rnd=1043005&ct=wifi&sdkVersion=0.0.0";
        String expected_pg_success_url = "https://ads.superawesome.tv/v2/event?data=%7B%22type%22%3A%22parentalGateSuccess%22%2C%22creative%22%3A1092%2C%22line_item%22%3A2731%2C%22placement%22%3A4001%7D&sourceBundle=superawesome.tv.saadloaderdemo&rnd=1487496&ct=wifi&sdkVersion=0.0.0";
        String expected_sa_impr_url = "https://ads.superawesome.tv/v2/impression?placement=4001&rnd=1095998&sourceBundle=superawesome.tv.saadloaderdemo&no_image=true&line_item=2731&creative=1092&sdkVersion=0.0.0";
        String expected_impression_url = "https://superawesome.tv/impression";
        String expected_install_url = "https://superawesome.tv/install";

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

        String sa_tracking_url = sa_tracking.get(0).URL;
        String viewable_impr_url = viewable_impr.get(0).URL;
        String pg_close_url = pg_close.get(0).URL;
        String pg_fail_url = pg_fail.get(0).URL;
        String pg_open_url = pg_open.get(0).URL;
        String pg_success_url = pg_success.get(0).URL;
        String sa_impr_url = sa_impr.get(0).URL;
        String impression_url = impression.get(0).URL;
        String install_url = install.get(0).URL;

        assertNotNull(sa_tracking_url);
        assertNotNull(viewable_impr_url);
        assertNotNull(pg_close_url);
        assertNotNull(pg_fail_url);
        assertNotNull(pg_open_url);
        assertNotNull(pg_success_url);
        assertNotNull(sa_impr_url);
        assertNotNull(impression_url);
        assertNotNull(install_url);

        boolean sa_tracking_ok = getHammingDistance(expected_sa_tracking_url, sa_tracking_url, 7);
        boolean viewable_impr_ok = getHammingDistance(expected_viewable_impr_url, viewable_impr_url, 7);
        boolean pg_close_ok = getHammingDistance(expected_pg_close_url, pg_close_url, 7);
        boolean pg_fail_ok = getHammingDistance(expected_pg_fail_url, pg_fail_url, 7);
        boolean pg_open_ok = getHammingDistance(expected_pg_open_url, pg_open_url, 7);
        boolean pg_success_ok = getHammingDistance(expected_pg_success_url, pg_success_url, 7);
        boolean sa_impr_ok = getHammingDistance(expected_sa_impr_url, sa_impr_url, 7);
        boolean impression_ok = getHammingDistance(expected_impression_url, impression_url, 7);
        boolean install_ok = getHammingDistance(expected_install_url, install_url, 7);

        assertTrue(sa_tracking_ok);
        assertTrue(viewable_impr_ok);
        assertTrue(pg_close_ok);
        assertTrue(pg_fail_ok);
        assertTrue(pg_open_ok);
        assertTrue(pg_success_ok);
        assertTrue(sa_impr_ok);
        assertTrue(impression_ok);
        assertTrue(install_ok);

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
