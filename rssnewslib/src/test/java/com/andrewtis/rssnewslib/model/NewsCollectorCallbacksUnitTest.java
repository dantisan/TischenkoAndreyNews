package com.andrewtis.rssnewslib.model;


import com.andrewtis.rssnewslib.collectortesttool.CollectorCallbackTester;
import com.andrewtis.rssnewslib.collectortesttool.StubNewsExtractor;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import static com.andrewtis.rssnewslib.collectortesttool.StubNewsExtractor.*;

@RunWith(Parameterized.class)
public class NewsCollectorCallbacksUnitTest {


    public NewsCollectorCallbacksUnitTest(CollectorCallbackTester collectorCallbackTester, HashMap<String, StubNewsExtractor> urlExtractors) {
        this.collectorCallbackTester = collectorCallbackTester;
        this.urlExtractors = urlExtractors;
    }

    private CollectorCallbackTester collectorCallbackTester;
    private HashMap<String, StubNewsExtractor> urlExtractors;

    @Test(timeout = 1100)
    public void newsCollectorTest() throws InterruptedException {
        NewsCollectorTester tester = new NewsCollectorTester(collectorCallbackTester, urlExtractors);
        tester.test(1000);

    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        List<Object[]> params = new ArrayList<>();
        params.add(argsOneFailedNewsExtractor());
        params.add(argsTwoNewsExtractorWithSameDelay());
        params.add(argsTwoNewsExtractorWithDifferentDelay());

        return params;
    }

    private static Object[] argsOneFailedNewsExtractor() {
        CollectorCallbackTester collectorCallbackTester = new CollectorCallbackTester(1, 0, 1);
        StubNewsExtractor extractor = StubNewsExtractor.getFailedExtractor();
        HashMap<String, StubNewsExtractor> urlExtractors = new HashMap<>();
        urlExtractors.put("url1", extractor);
        Object[] mas = {collectorCallbackTester, urlExtractors};
        return mas;
    }

    private static Object[] argsTwoNewsExtractorWithSameDelay() {
        CollectorCallbackTester collectorCallbackTester = new CollectorCallbackTester(2, 2, 0);
        StubNewsExtractor extractor1 = getStubNewsExtractor(10, new DateTime(), 100);
        StubNewsExtractor extractor2 = getStubNewsExtractor(20, new DateTime(), 100);

        HashMap<String, StubNewsExtractor> urlExtractors = new HashMap<>();
        urlExtractors.put("url1", extractor1);
        urlExtractors.put("url2", extractor2);

        Object[] mas = {collectorCallbackTester, urlExtractors};
        return mas;
    }

    private static Object[] argsTwoNewsExtractorWithDifferentDelay() {
        CollectorCallbackTester collectorCallbackTester = new CollectorCallbackTester(2, 2, 0);
        StubNewsExtractor extractor1 = getStubNewsExtractor(10, new DateTime(), 100);
        StubNewsExtractor extractor2 = getStubNewsExtractor(10, new DateTime(), 250);

        HashMap<String, StubNewsExtractor> urlExtractors = new HashMap<>();
        urlExtractors.put("url1", extractor1);
        urlExtractors.put("url2", extractor2);

        Object[] mas = {collectorCallbackTester, urlExtractors};
        return mas;
    }

}



