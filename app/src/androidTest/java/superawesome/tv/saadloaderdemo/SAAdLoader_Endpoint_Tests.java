package superawesome.tv.saadloaderdemo;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import org.json.JSONObject;

import tv.superawesome.lib.saadloader.SALoader;
import tv.superawesome.lib.sasession.session.SASession;

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
}
