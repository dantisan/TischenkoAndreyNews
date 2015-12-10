package com.andrewtis.rssnewslib.model;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NewsCollector {
    public void addNewsRefreshedCallback(@NonNull NewsRefreshedCallback newsRefreshedCallback) {

        if(newsRefreshedCallback==null){
            return;
        }

        refreshedCallbacks.add(newsRefreshedCallback);

    }

    List<NewsRefreshedCallback> refreshedCallbacks = new ArrayList<>();

    private ExecutorService executorService = Executors.newCachedThreadPool();

    protected NewsCollector(){

    }
    public NewsCollector(NewsRefreshedCallback newsRefreshedCallback) {

        addNewsRefreshedCallback(newsRefreshedCallback);
    }

    public void refreshNewsForUrl(final String newsUrl)  {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                refreshNews(newsUrl);
            }
        });
    }

    public void terminateNewsRefreshing(){
        executorService.shutdown();
    }

    protected NewsExtractor getNewsExtractor(String newsUrl){
        return new NewsExtractor(newsUrl);
    }

    @VisibleForTesting
    void refreshNews(String newsUrl) {
        for (NewsRefreshedCallback callback : refreshedCallbacks)
            callback.beforeNewStartRefreshing(newsUrl);

        NewsExtractor newsExtractor = getNewsExtractor(newsUrl);
        List<NewsInfo> newsList;

        try {
            newsList = newsExtractor.getNews();
            for (NewsRefreshedCallback callback : refreshedCallbacks)
                callback.newRefreshedForUrl(newsUrl, newsList);
        } catch (Exception ex) {
            for (NewsRefreshedCallback callback : refreshedCallbacks)
                callback.errorOnRefreshing(ex, newsUrl);
            //newsCollection.remove(newsUrl);
        }
    }

    public interface NewsRefreshedCallback {
        void beforeNewStartRefreshing(String url);
        void newRefreshedForUrl(String url, List<NewsInfo> refreshedNews);
        void errorOnRefreshing(Exception ex, String refreshingUrl);
    }


}
