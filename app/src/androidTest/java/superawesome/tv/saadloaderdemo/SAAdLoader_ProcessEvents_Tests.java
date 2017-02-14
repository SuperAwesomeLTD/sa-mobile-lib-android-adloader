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
        ad.creative.clickCounterUrl = "https://superawesome.tv/click_counter";

        SASession session = new SASession(getContext());
        session.setVersion("3.2.1");
        session.setConfigurationProduction();
        session.disableTestMode();

        SAProcessEvents.addAdEvents(ad, session);

        int expected_events = 7;

        int expected_superawesome_click = 1;
        int expected_superawesome_impression = 1;
        int expected_superawesome_viewable_impression = 1;
        int expected_superawesome_pg_close = 1;
        int expected_superawesome_pg_fail = 1;
        int expected_superawesome_pg_open = 1;
        int expected_superawesome_pg_success = 1;

        assertNotNull(ad.creative.events);
        assertEquals(expected_events, ad.creative.events.size());

        List<SATracking> superawesome_click = new ArrayList<>();
        List<SATracking> superawesome_impression = new ArrayList<>();
        List<SATracking> superawesome_viewable_impression = new ArrayList<>();
        List<SATracking> superawesome_pg_close = new ArrayList<>();
        List<SATracking> superawesome_pg_fail = new ArrayList<>();
        List<SATracking> superawesome_pg_open = new ArrayList<>();
        List<SATracking> superawesome_pg_success = new ArrayList<>();

        for (SATracking event : ad.creative.events) {
            if (event.event.equals("superawesome_click")) superawesome_click.add(event);
            if (event.event.equals("superawesome_impression")) superawesome_impression.add(event);
            if (event.event.equals("superawesome_viewable_impression")) superawesome_viewable_impression.add(event);
            if (event.event.equals("superawesome_pg_close")) superawesome_pg_close.add(event);
            if (event.event.equals("superawesome_pg_fail")) superawesome_pg_fail.add(event);
            if (event.event.equals("superawesome_pg_open")) superawesome_pg_open.add(event);
            if (event.event.equals("superawesome_pg_success")) superawesome_pg_success.add(event);
        }

        assertEquals(expected_superawesome_click, superawesome_click.size());
        assertEquals(expected_superawesome_impression, superawesome_impression.size());
        assertEquals(expected_superawesome_viewable_impression, superawesome_viewable_impression.size());
        assertEquals(expected_superawesome_pg_close, superawesome_pg_close.size());
        assertEquals(expected_superawesome_pg_fail, superawesome_pg_fail.size());
        assertEquals(expected_superawesome_pg_open, superawesome_pg_open.size());
        assertEquals(expected_superawesome_pg_success, superawesome_pg_success.size());

        String superawesome_click_url = superawesome_click.get(0).URL;
        String superawesome_impression_url = superawesome_impression.get(0).URL;
        String superawesome_viewable_impression_url = superawesome_viewable_impression.get(0).URL;
        String superawesome_pg_close_url = superawesome_pg_close.get(0).URL;
        String superawesome_pg_fail_url = superawesome_pg_fail.get(0).URL;
        String superawesome_pg_open_url = superawesome_pg_open.get(0).URL;
        String superawesome_pg_success_url = superawesome_pg_success.get(0).URL;

        assertNotNull(superawesome_click_url);
        assertNotNull(superawesome_impression_url);
        assertNotNull(superawesome_viewable_impression_url);
        assertNotNull(superawesome_pg_close_url);
        assertNotNull(superawesome_pg_fail_url);
        assertNotNull(superawesome_pg_open_url);
        assertNotNull(superawesome_pg_success_url);

        assertTrue(superawesome_click_url.contains("https://ads.superawesome.tv/v2/click"));
        assertTrue(superawesome_click_url.contains("placement=4001"));
        assertTrue(superawesome_click_url.contains("bundle=superawesome.tv.saadloaderdemo"));
        assertTrue(superawesome_click_url.contains("creative=1092"));
        assertTrue(superawesome_click_url.contains("line_item=2731"));
        assertTrue(superawesome_click_url.contains("sdkVersion=3.2.1"));

        assertTrue(superawesome_impression_url.contains("https://ads.superawesome.tv/v2/impression"));
        assertTrue(superawesome_impression_url.contains("placement=4001"));
        assertTrue(superawesome_impression_url.contains("bundle=superawesome.tv.saadloaderdemo"));
        assertTrue(superawesome_impression_url.contains("creative=1092"));
        assertTrue(superawesome_impression_url.contains("line_item=2731"));
        assertTrue(superawesome_impression_url.contains("sdkVersion=3.2.1"));

        assertTrue(superawesome_viewable_impression_url.contains("https://ads.superawesome.tv/v2/event"));
        assertTrue(superawesome_viewable_impression_url.contains("bundle=superawesome.tv.saadloaderdemo"));
        assertTrue(superawesome_viewable_impression_url.contains("sdkVersion=3.2.1"));
        assertTrue(superawesome_viewable_impression_url.contains("type%22%3A%22viewable_impression"));
        assertTrue(superawesome_viewable_impression_url.contains("creative%22%3A1092"));
        assertTrue(superawesome_viewable_impression_url.contains("line_item%22%3A2731"));
        assertTrue(superawesome_viewable_impression_url.contains("placement%22%3A4001"));

        assertTrue(superawesome_pg_close_url.contains("https://ads.superawesome.tv/v2/event"));
        assertTrue(superawesome_pg_close_url.contains("bundle=superawesome.tv.saadloaderdemo"));
        assertTrue(superawesome_pg_close_url.contains("sdkVersion=3.2.1"));
        assertTrue(superawesome_pg_close_url.contains("type%22%3A%22parentalGateClose"));
        assertTrue(superawesome_pg_close_url.contains("creative%22%3A1092"));
        assertTrue(superawesome_pg_close_url.contains("line_item%22%3A2731"));
        assertTrue(superawesome_pg_close_url.contains("placement%22%3A4001"));

        assertTrue(superawesome_pg_fail_url.contains("https://ads.superawesome.tv/v2/event"));
        assertTrue(superawesome_pg_fail_url.contains("bundle=superawesome.tv.saadloaderdemo"));
        assertTrue(superawesome_pg_fail_url.contains("sdkVersion=3.2.1"));
        assertTrue(superawesome_pg_fail_url.contains("type%22%3A%22parentalGateFail"));
        assertTrue(superawesome_pg_fail_url.contains("creative%22%3A1092"));
        assertTrue(superawesome_pg_fail_url.contains("line_item%22%3A2731"));
        assertTrue(superawesome_pg_fail_url.contains("placement%22%3A4001"));

        assertTrue(superawesome_pg_open_url.contains("https://ads.superawesome.tv/v2/event"));
        assertTrue(superawesome_pg_open_url.contains("bundle=superawesome.tv.saadloaderdemo"));
        assertTrue(superawesome_pg_open_url.contains("sdkVersion=3.2.1"));
        assertTrue(superawesome_pg_open_url.contains("type%22%3A%22parentalGateOpen"));
        assertTrue(superawesome_pg_open_url.contains("creative%22%3A1092"));
        assertTrue(superawesome_pg_open_url.contains("line_item%22%3A2731"));
        assertTrue(superawesome_pg_open_url.contains("placement%22%3A4001"));

        assertTrue(superawesome_pg_success_url.contains("https://ads.superawesome.tv/v2/event"));
        assertTrue(superawesome_pg_success_url.contains("bundle=superawesome.tv.saadloaderdemo"));
        assertTrue(superawesome_pg_success_url.contains("sdkVersion=3.2.1"));
        assertTrue(superawesome_pg_success_url.contains("type%22%3A%22parentalGateSuccess"));
        assertTrue(superawesome_pg_success_url.contains("creative%22%3A1092"));
        assertTrue(superawesome_pg_success_url.contains("line_item%22%3A2731"));
        assertTrue(superawesome_pg_success_url.contains("placement%22%3A4001"));
    }
}
