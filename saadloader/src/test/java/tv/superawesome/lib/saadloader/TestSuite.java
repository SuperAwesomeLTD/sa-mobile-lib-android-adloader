package tv.superawesome.lib.saadloader;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import tv.superawesome.lib.saadloader.adloader.TestSAAdLoader_LoadAd;
import tv.superawesome.lib.saadloader.postprocessor.TestSAProcessHTML;
import tv.superawesome.lib.saadloader.query.TestSAAdLoader_GetAwesomeAdsEndpoint;
import tv.superawesome.lib.saadloader.query.TestSAAdLoader_GetAwesomeAdsHeader;
import tv.superawesome.lib.saadloader.query.TestSAAdLoader_GetAwesomeAdsQuery;

/**
 * Created by gabriel.coman on 03/05/2018.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestSAProcessHTML.class,
        TestSAAdLoader_GetAwesomeAdsQuery.class,
        TestSAAdLoader_GetAwesomeAdsEndpoint.class,
        TestSAAdLoader_GetAwesomeAdsHeader.class,
        TestSAAdLoader_LoadAd.class
})
public class TestSuite {
}
