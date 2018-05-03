package superawesome.tv.saadloaderdemo;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import org.json.JSONObject;

import tv.superawesome.lib.saadloader.SALoader;
import tv.superawesome.lib.sasession.session.SASession;

public class SAAdLoader_Header_Tests extends ApplicationTestCase<Application> {

    public SAAdLoader_Header_Tests() {
        super(Application.class);
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

        int expected_keys = 0;

        assertNotNull(header);
        assertEquals(expected_keys, header.length());
        assertNull(header.opt("Content-Type"));
        assertNull(header.opt("User-Agent"));
    }
}
