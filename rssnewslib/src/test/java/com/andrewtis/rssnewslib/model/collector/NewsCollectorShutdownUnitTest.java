package com.andrewtis.rssnewslib.model.collector;

import com.andrewtis.rssnewslib.collectortesttool.CollectorCallbackTester;
import com.andrewtis.rssnewslib.collectortesttool.StubNewsExtractor;
import com.andrewtis.rssnewslib.model.NewsCollectorTester;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.RejectedExecutionException;

import static com.andrewtis.rssnewslib.collectortesttool.StubNewsExtractor.getStubNewsExtractor;

public class NewsCollectorShutdownUnitTest {

    @Test (timeout = 1100, expected = RejectedExecutionException.class)
    public void shutdownTest() throws InterruptedException {
        CollectorCallbackTester collectorCallbackTester = new CollectorCallbackTester(-1, 0, 0);
        StubNewsExtractor extractor1 = getStubNewsExtractor(10, new DateTime(), 100);

        HashMap<String, StubNewsExtractor> urlExtractors = new HashMap<>();
        urlExtractors.put("url1", extractor1);

        NewsCollectorTester tester = new NewsCollectorTester(collectorCallbackTester, urlExtractors);
        tester.shutdownCollector();
        tester.test(1000);

    }

    @Test (timeout = 1100)
    public void shutdownWhileCacheNewsTest() throws InterruptedException {
        CollectorCallbackTester collectorCallbackTester = new CollectorCallbackTester(-1, 0, 0);
        StubNewsExtractor extractor1 = getStubNewsExtractor(10, new DateTime(), 100);
        StubNewsExtractor extractor2 = getStubNewsExtractor(10, new DateTime(), 200);

        HashMap<String, StubNewsExtractor> urlExtractors = new HashMap<>();
        urlExtractors.put("url1", extractor1);
        urlExtractors.put("url2", extractor2);

        NewsCollectorTester tester = new NewsCollectorTester(collectorCallbackTester, urlExtractors);

        tester.testWithShutdown(1000, 1);

    }

}
