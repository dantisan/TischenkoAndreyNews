package com.andrewtis.rssnewslib;

import com.andrewtis.rssnewslib.collectortesttool.StubNewsExtractor;
import com.andrewtis.rssnewslib.model.NewsCasher;
import com.andrewtis.rssnewslib.model.NewsCollector;
import com.andrewtis.rssnewslib.model.NewsExtractor;
import com.andrewtis.rssnewslib.model.NewsInfo;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class NewsCollectorStub extends NewsCasher {
    private int stubUrlsCount;

    public HashMap<String, StubNewsExtractor> getUrlExtractors() {
        return urlExtractors;
    }

    HashMap<String, StubNewsExtractor> urlExtractors = new HashMap<>();

    public TesterRefreshCallbacks getTestingCallback() {
        return testingCallback;
    }

    TesterRefreshCallbacks testingCallback;

    private int timesAwaitMillis ;

    public NewsCollectorStub( int stubUrlsCount, int newsInUrlCount, int timesAwaitMillis) {
        super();
        this.stubUrlsCount = stubUrlsCount;
        testingCallback = new TesterRefreshCallbacks();
        super.addNewsRefreshedCallback(testingCallback);

        this.timesAwaitMillis = timesAwaitMillis;
        for (int i = 0; i < stubUrlsCount; i++) {
            String url = "url_"+i;
            StubNewsExtractor extractorStub = StubNewsExtractor.getStubNewsExtractor(newsInUrlCount, DateTime.now().plusHours(i),timesAwaitMillis);
            urlExtractors.put(url,extractorStub);
            addCacheUrl(url);

        }
        urlExtractors.put("url_" + stubUrlsCount, StubNewsExtractor.getFailedExtractor());
        addCacheUrl("url_" + stubUrlsCount);
    }

    @Override
    protected NewsExtractor getNewsExtractor(String newsUrl){
        return urlExtractors.get(newsUrl);
    }

    public void reinitLatch() {
        countDownLatch = new CountDownLatch(stubUrlsCount+1);
    }
    CountDownLatch countDownLatch;

    public void waitAllNewsRefreshed() throws InterruptedException {
        countDownLatch.await(2*(urlExtractors.size()+1)*timesAwaitMillis, TimeUnit.MILLISECONDS);
    }


    class TesterRefreshCallbacks implements NewsCollector.NewsRefreshedCallback{

        public TesterRefreshCallbacks(){
            reinitLatch();
        }



        @Override
        public void beforeNewStartRefreshing(String url) {
        }

        @Override
        public void newRefreshedForUrl(String url, List<NewsInfo> infoList) {
            countDownLatch.countDown();
        }

        @Override
        public void errorOnRefreshing(Exception ex, String refreshingUrl) {
            countDownLatch.countDown();
        }
    }



}
