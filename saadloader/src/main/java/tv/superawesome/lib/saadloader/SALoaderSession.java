package tv.superawesome.lib.saadloader;

import java.util.HashMap;

import tv.superawesome.lib.saevents.SAEvents;

/**
 * Created by gabriel.coman on 06/06/16.
 */
public class SALoaderSession {

    // constants
    private static final String URL_KEY = "baseURL";
    private static final String DAU_KEY = "dauID";
    private static final String TST_KEY = "testingEnabled";
    private static final String VER_KEY = "version";

    /** The singleton part */
    private static SALoaderSession instance = new SALoaderSession();

    private SALoaderSession() {
        sessionData = new HashMap<>();
        sessionData.put(URL_KEY, "https://ads.superawesome.tv/v2");
        sessionData.put(DAU_KEY, 0);
        sessionData.put(TST_KEY, false);
        sessionData.put(VER_KEY, "0");
    }

    public static SALoaderSession getInstance(){
        return instance;
    }

    /** private vars */
    HashMap<String, Object> sessionData;

    /** Setters & getters */

    public void setBaseUrl(String baseUrl) {
        sessionData.put(URL_KEY, baseUrl);
    }

    public void setDauId(int dauId) {
        sessionData.put(DAU_KEY, dauId);
    }

    public void setTest(boolean test) {
        sessionData.put(TST_KEY, test);
    }

    public void setVersion(String version) {
        sessionData.put(VER_KEY, version);
    }

    public String getBaseUrl () {
        return sessionData.get(URL_KEY).toString();
    }

    public int getDauId () {
        return (int) sessionData.get(DAU_KEY);
    }

    public boolean getTest () {
        return (boolean) sessionData.get(TST_KEY);
    }

    public String getVersion () {
        return sessionData.get(VER_KEY).toString();
    }
}
