package com.andrewtis.rssnewslib;

import com.andrewtis.rssnewslib.collectortesttool.StubNewsExtractor;
import com.andrewtis.rssnewslib.model.NewsCollector;
import com.andrewtis.rssnewslib.model.NewsExtractor;

import org.joda.time.DateTime;

import java.util.HashMap;

public class NewsCollectorStub extends NewsCollector {

    HashMap<String, NewsExtractor> urlExtractors = new HashMap<>();

    public NewsCollectorStub(NewsRefreshedCallback newsRefreshedCallback, int stubUrlsCount, int newsInUrlCount) {
        super(newsRefreshedCallback);
        for (int i = 0; i < stubUrlsCount; i++) {
            String url = "url_"+i;
            StubNewsExtractor extractorStub = StubNewsExtractor.getStubNewsExtractor(newsInUrlCount, DateTime.now().plusHours(i),100);
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

}
