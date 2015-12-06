package com.andrewtis.rssnewslib;

import com.andrewtis.rssnewslib.collectortesttool.StubNewsExtractor;
import com.andrewtis.rssnewslib.model.NewsCollector;
import com.andrewtis.rssnewslib.model.NewsExtractor;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class NewsCollectorStub extends NewsCollector {

    HashMap<String, NewsExtractor> urlExtractors = new HashMap<>();

    public TesterRefreshCallbacks getTestingCallback() {
        return testingCallback;
    }

    TesterRefreshCallbacks testingCallback;

    @Override
    public void setNewsRefreshedCallback(NewsRefreshedCallback newsRefreshedCallback) {
        testingCallback.setAdditionCallback(newsRefreshedCallback);
    }

    private int timesAwaitMillis ;

    public NewsCollectorStub(NewsRefreshedCallback newsRefreshedCallback, int stubUrlsCount, int newsInUrlCount, int timesAwaitMillis) {
        super(null);
        testingCallback = new TesterRefreshCallbacks(stubUrlsCount+1,newsRefreshedCallback);
        super.setNewsRefreshedCallback(testingCallback);

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

    CountDownLatch countDownLatch;

    public void waitAllNewsRefreshed() throws InterruptedException {
        countDownLatch.await(2*(urlExtractors.size()+1)*timesAwaitMillis, TimeUnit.MILLISECONDS);
    }


    class TesterRefreshCallbacks implements NewsCollector.NewsRefreshedCallback{
        NewsCollector.NewsRefreshedCallback additionCallback;

        public void setAdditionCallback(NewsRefreshedCallback additionCallback) {
            this.additionCallback = additionCallback;
        }

        public TesterRefreshCallbacks(int newsShouldRefreshed ,NewsCollector.NewsRefreshedCallback additionCallback){
            countDownLatch = new CountDownLatch(newsShouldRefreshed);
            this.additionCallback = additionCallback;
        }
        @Override
        public void beforeNewStartRefreshing(String url) {
            if(additionCallback!=null)
                additionCallback.beforeNewStartRefreshing(url);
        }

        @Override
        public void newRefreshedForUrl(String url) {
            if(additionCallback!=null)
                additionCallback.newRefreshedForUrl(url);
            countDownLatch.countDown();
        }

        @Override
        public void errorOnRefreshing(Exception ex, String refreshingUrl) {
            if(additionCallback!=null)
                additionCallback.errorOnRefreshing(ex, refreshingUrl);
            countDownLatch.countDown();
        }
    }



}
