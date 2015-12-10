package com.andrewtis.rssnewslib.model;

import com.andrewtis.rssnewslib.collectortesttool.CollectorCallbackTester;
import com.andrewtis.rssnewslib.collectortesttool.StubNewsExtractor;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class NewsCasherTester {

    private class SpyedNewsCollector extends NewsCasher implements NewsCollector.NewsRefreshedCallback {

        public SpyedNewsCollector(NewsRefreshedCallback newsRefreshedCallback) {
            super(newsRefreshedCallback);
            super.addNewsRefreshedCallback(this);
        }

        @Override
        public void beforeNewStartRefreshing(String url) {

        }

        @Override
        public void newRefreshedForUrl(String url, List<NewsInfo> refreshedNews) {
            countDownLatch.countDown();
        }

        @Override
        public void errorOnRefreshing(Exception ex, String refreshingUrl) {
            countDownLatch.countDown();
        }
    }

    SpyedNewsCollector testedCollector;
    CollectorCallbackTester collectedCallbackTesters;
    HashMap<String, StubNewsExtractor> stubNewsExtractors;

    public void shutdownCollector() {
            testedCollector.terminateNewsRefreshing();
    }

    CountDownLatch countDownLatch;

    public NewsCasherTester(CollectorCallbackTester collectedCallbackTesters, HashMap<String, StubNewsExtractor> newsExtractors) {
        countDownLatch = new CountDownLatch(newsExtractors.size());
        this.stubNewsExtractors = newsExtractors;
        this.collectedCallbackTesters = collectedCallbackTesters;
        testedCollector = spy(new SpyedNewsCollector(collectedCallbackTesters));
        for (String extractorUrl : newsExtractors.keySet()) {
            doReturn(stubNewsExtractors.get(extractorUrl)).when(testedCollector).getNewsExtractor(extractorUrl);
        }
    }

    public void test(long timeout) throws InterruptedException {
        test(timeout, 0);
    }

    public void testWithShutdown(long timeout, int expextedMissedCallbacks) throws InterruptedException {
        test(timeout, expextedMissedCallbacks);
    }

    private void test(long timeout, int expextedMissedCallbacks) throws InterruptedException {

        Iterable<String> urls = null;
        testedCollector.addCacheUrl(urls);

        testedCollector.addCacheUrl(stubNewsExtractors.keySet());

        testedCollector.refreshCachedNews();

        int downCounts = expextedMissedCallbacks;
        while (downCounts--!=0)
                countDownLatch.countDown();

        countDownLatch.await(timeout, TimeUnit.MILLISECONDS);

        if(expextedMissedCallbacks!=0){
            testedCollector.terminateNewsRefreshing();
        }

        collectedCallbackTesters.checkExpectedCallbacksCount();

        if(expextedMissedCallbacks==0)
            checkNews();
    }

    private void checkNews() {
        int expectedNewsCount = 0;
        for (String newsUrl : stubNewsExtractors.keySet()) {

            List<NewsInfo> newsInfoForUrl = testedCollector.getNewsInfoForUrl(newsUrl);
            StubNewsExtractor extractor = stubNewsExtractors.get(newsUrl);
            if (extractor.isShouldFailNews()) {
                Assert.assertTrue(newsInfoForUrl.isEmpty());
                continue;
            }
            List<NewsInfo> res = new ArrayList<>();
            res.addAll(extractor.getNewsList());
            Collections.sort(res);
            Assert.assertEquals(res, newsInfoForUrl);
            Assert.assertTrue(testedCollector.getAllNewsInfo().containsAll(res));
            expectedNewsCount+=res.size();
        }
        Assert.assertEquals(expectedNewsCount,testedCollector.getNewsCount());
    }
}
