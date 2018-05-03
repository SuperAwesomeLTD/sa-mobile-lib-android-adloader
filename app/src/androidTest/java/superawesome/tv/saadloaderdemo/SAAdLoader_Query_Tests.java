package superawesome.tv.saadloaderdemo;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import org.json.JSONObject;

import tv.superawesome.lib.saadloader.SALoader;
import tv.superawesome.lib.sasession.session.SASession;
import tv.superawesome.lib.sautils.SAUtils;

public class SAAdLoader_Query_Tests extends ApplicationTestCase<Application> {

    public SAAdLoader_Query_Tests() {
        super(Application.class);
    }

    @SmallTest
    public void testGetAwesomeAdsQuery1 () {

        SALoader loader = new SALoader(getContext());

        SASession session = new SASession(getContext());
        session.enableTestMode();

        int expected_keys = 16;
        boolean expected_test = true;
        String expected_sdkVersion = "0.0.0";
        String expected_bundle = "superawesome.tv.saadloaderdemo";
        String expected_name = "SAAdLoaderDemo";
        int expected_ct = SAUtils.SAConnectionType.wifi.ordinal();

        JSONObject query = loader.getAwesomeAdsQuery(session);

        assertNotNull(query);
        assertEquals(expected_keys, query.length());

        assertNotNull(query.opt("test"));
        assertEquals(expected_test, query.opt("test"));

        assertNotNull(query.opt("sdkVersion"));
        assertEquals(expected_sdkVersion, query.opt("sdkVersion"));

        assertNotNull(query.opt("rnd"));

        assertNotNull(query.opt("bundle"));
        assertEquals(expected_bundle, query.opt("bundle"));

        assertNotNull(query.opt("name"));
        assertEquals(expected_name, query.opt("name"));

        assertNotNull(query.opt("dauid"));

        assertNotNull(query.opt("ct"));
        assertEquals(expected_ct, query.opt("ct"));

        assertNotNull(query.opt("lang"));
        assertNotNull(query.opt("device"));
    }

    @SmallTest
    public void testGetAwesomeAdsQuery2 () {

        SALoader loader = new SALoader(getContext());

        SASession session = new SASession(getContext());
        session.disableTestMode();

        int expected_keys = 16;
        boolean expected_test = false;
        String expected_sdkVersion = "0.0.0";
        String expected_bundle = "superawesome.tv.saadloaderdemo";
        String expected_name = "SAAdLoaderDemo";
        int expected_ct = SAUtils.SAConnectionType.wifi.ordinal();

        JSONObject query = loader.getAwesomeAdsQuery(session);

        assertNotNull(query);
        assertEquals(expected_keys, query.length());

        assertNotNull(query.opt("test"));
        assertEquals(expected_test, query.opt("test"));

        assertNotNull(query.opt("sdkVersion"));
        assertEquals(expected_sdkVersion, query.opt("sdkVersion"));

        assertNotNull(query.opt("rnd"));

        assertNotNull(query.opt("bundle"));
        assertEquals(expected_bundle, query.opt("bundle"));

        assertNotNull(query.opt("name"));
        assertEquals(expected_name, query.opt("name"));

        assertNotNull(query.opt("dauid"));

        assertNotNull(query.opt("ct"));
        assertEquals(expected_ct, query.opt("ct"));

        assertNotNull(query.opt("lang"));
        assertNotNull(query.opt("device"));

    }

    @SmallTest
    public void testGetAwesomeAdsQuery3 () {

        SALoader loader = new SALoader(null);
        SASession session = new SASession(null);

        int expected_keys = 16;
        boolean expected_test = false;
        String expected_sdkVersion = "0.0.0";
        String expected_bundle = "unknown";
        String expected_name = "unknown";
        int expected_ct = SAUtils.SAConnectionType.unknown.ordinal();

        JSONObject query = loader.getAwesomeAdsQuery(session);

        assertNotNull(query);
        assertEquals(expected_keys, query.length());

        assertNotNull(query.opt("test"));
        assertEquals(expected_test, query.opt("test"));

        assertNotNull(query.opt("sdkVersion"));
        assertEquals(expected_sdkVersion, query.opt("sdkVersion"));

        assertNotNull(query.opt("rnd"));

        assertNotNull(query.opt("bundle"));
        assertEquals(expected_bundle, query.opt("bundle"));

        assertNotNull(query.opt("name"));
        assertEquals(expected_name, query.opt("name"));

        assertNotNull(query.opt("dauid"));

        assertNotNull(query.opt("ct"));
        assertEquals(expected_ct, query.opt("ct"));

        assertNotNull(query.opt("lang"));
        assertNotNull(query.opt("device"));

    }

    @SmallTest
    public void testGetAwesomeAdsQuery4 () {

        SALoader loader = new SALoader(null);
        SASession session = null;

        int expected_keys = 0;
        JSONObject query = loader.getAwesomeAdsQuery(session);

        assertNotNull(query);
        assertEquals(expected_keys, query.length());

        assertNull(query.opt("test"));
        assertNull(query.opt("sdkVersion"));
        assertNull(query.opt("rnd"));
        assertNull(query.opt("bundle"));
        assertNull(query.opt("name"));
        assertNull(query.opt("dauid"));
        assertNull(query.opt("ct"));
        assertNull(query.opt("lang"));
        assertNull(query.opt("device"));
    }
}
