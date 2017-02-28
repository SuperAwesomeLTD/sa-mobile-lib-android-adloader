package superawesome.tv.saadloaderdemo;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        SAAdLoader_Endpoint_Tests.class,
        SAAdLoader_Header_Tests.class,
        SAAdLoader_Query_Tests.class,
        SAAdLoader_Async_Tests.class,
        SAAdLoader_ProcessHTML_Tests.class
})
public class TestSuite {
}
