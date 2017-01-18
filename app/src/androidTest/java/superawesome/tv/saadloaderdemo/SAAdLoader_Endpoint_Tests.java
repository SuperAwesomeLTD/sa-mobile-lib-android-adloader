package superawesome.tv.saadloaderdemo;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import org.json.JSONObject;

import tv.superawesome.lib.saadloader.SALoader;
import tv.superawesome.lib.sasession.SASession;
import tv.superawesome.lib.sautils.SAUtils;

public class SAAdLoader_Endpoint_Tests extends ApplicationTestCase<Application> {

    public SAAdLoader_Endpoint_Tests() {
        super(Application.class);
    }

    @SmallTest
    public void testGetAwesomeAdsEndpoint1 () {

        SALoader loader = new SALoader(getContext());

        SASession session = new SASession(getContext());
        session.setConfigurationProduction();

        String expected_baseUrl = "https://ads.superawesome.tv/v2/ad/4001";

        String baseUrl = loader.getAwesomeAdsEndpoint(session, 4001);

        assertNotNull(baseUrl);
        assertEquals(expected_baseUrl, baseUrl);

    }

    @SmallTest
    public void testGetAwesomeAdsEndpoint2 () {

        SALoader loader = new SALoader(getContext());

        SASession session = new SASession(getContext());
        session.setConfigurationStaging();

        String expected_baseUrl = "https://ads.staging.superawesome.tv/v2/ad/4001";

        String baseUrl = loader.getAwesomeAdsEndpoint(session, 4001);

        assertNotNull(baseUrl);
        assertEquals(expected_baseUrl, baseUrl);

    }

    @SmallTest
    public void testGetAwesomeAdsEndpoint3 () {

        SALoader loader = new SALoader(null);
        SASession session = new SASession(null);

        String expected_baseUrl = "https://ads.superawesome.tv/v2/ad/4001";

        String baseUrl = loader.getAwesomeAdsEndpoint(session, 4001);

        assertNotNull(baseUrl);
        assertEquals(expected_baseUrl, baseUrl);

    }

    @SmallTest
    public void testGetAwesomeAdsEndpoint4 () {

        SALoader loader = new SALoader(null);
        SASession session = null;

        String baseUrl = loader.getAwesomeAdsEndpoint(session, 4001);

        assertNull(baseUrl);
    }

    @SmallTest
    public void testGetAwesomeAdsQuery1 () {

        SALoader loader = new SALoader(getContext());

        SASession session = new SASession(getContext());
        session.enableTestMode();

        int expected_keys = 9;
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

        int expected_keys = 9;
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

        int expected_keys = 9;
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

        int expected_keys = 9;
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
    public void testGetAwesomeAdsHeader1 () {

        SALoader loader = new SALoader(getContext());

        SASession session = new SASession(getContext());

        JSONObject header = loader.getAwesomeAdsHeader(session);

        int expected_keys = 2;
        String expected_Content_Type = "application/json";
        String expected_User_Agent = session.getUserAgent();

        assertNotNull(header);
        assertEquals(expected_keys, header.length());

        assertNotNull(header.opt("Content-Type"));
        assertEquals(expected_Content_Type, header.opt("Content-Type"));

        assertNotNull(header.opt("User-Agent"));
        assertEquals(expected_User_Agent, header.opt("User-Agent"));
    }

    @SmallTest
    public void testGetAwesomeAdsHeader2 () {

        SALoader loader = new SALoader(null);
        SASession session = new SASession(null);

        JSONObject header = loader.getAwesomeAdsHeader(session);

        int expected_keys = 2;
        String expected_Content_Type = "application/json";
        String expected_User_Agent = session.getUserAgent();

        assertNotNull(header);
        assertEquals(expected_keys, header.length());

        assertNotNull(header.opt("Content-Type"));
        assertEquals(expected_Content_Type, header.opt("Content-Type"));

        assertNotNull(header.opt("User-Agent"));
        assertEquals(expected_User_Agent, header.opt("User-Agent"));
    }

    @SmallTest
    public void testGetAwesomeAdsHeader3 () {

        SALoader loader = new SALoader(null);
        SASession session = null;

        JSONObject header = loader.getAwesomeAdsHeader(session);

        int expected_keys = 2;
        String expected_Content_Type = "application/json";
        String expected_User_Agent = "";

        assertNotNull(header);
        assertEquals(expected_keys, header.length());

        assertNotNull(header.opt("Content-Type"));
        assertEquals(expected_Content_Type, header.opt("Content-Type"));

        assertNotNull(header.opt("User-Agent"));
        assertEquals(expected_User_Agent, header.opt("User-Agent"));
    }
}
