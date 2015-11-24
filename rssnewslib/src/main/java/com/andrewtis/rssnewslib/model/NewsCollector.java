package com.andrewtis.rssnewslib.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NewsCollector {
    private Map<String, List<NewsInfo>> newsCollection = new ConcurrentHashMap<>();

    private volatile int newsRefreshing = 0;

    private NewsRefreshedCallback newsRefreshedCallback = null;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public int getNewsCount(){
        int count = 0;
        for (List<NewsInfo> news : newsCollection.values()) {
            count+=news.size();
        }
        return count;
    }

    public NewsCollector(NewsRefreshedCallback newsRefreshedCallback) {
        this.newsRefreshedCallback = newsRefreshedCallback;
    }

    public List<NewsInfo> getNewsInfoForUrl(String url){

        List<NewsInfo> newsInfos = newsCollection.get(url);
        if(newsInfos==null)
            return null;

        List<NewsInfo> res = new ArrayList<>();
        res.addAll(newsInfos);
        Collections.sort(res);
        return res;
    }

    public List<NewsInfo> getAllNewsInfo(){
        List<NewsInfo> res = new ArrayList<>();
        for (List<NewsInfo> curList : newsCollection.values()) {
            if(curList!=null)
                res.addAll(curList);
        }
        Collections.sort(res);
        return res;
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

    NewsExtractor getNewsExtractor(String newsUrl){
        return new NewsExtractor(newsUrl);
    }

    void refreshNews(String newsUrl) {
        boolean callbackExists = newsRefreshedCallback != null;
        if(callbackExists)
            newsRefreshedCallback.beforeNewStartRefreshing(newsUrl);

        NewsExtractor newsExtractor = getNewsExtractor(newsUrl);
        List<NewsInfo> info = null;

        newsRefreshing++;
        try {
            info = newsExtractor.getNews();
            newsCollection.put(newsUrl, info);
            if(callbackExists) {
                newsRefreshedCallback.newRefreshedForUrl(newsUrl);
            }
        } catch (Exception ex) {
            if (callbackExists)
                newsRefreshedCallback.errorOnRefreshing(ex, newsUrl);
            //newsCollection.remove(newsUrl);
        } finally {
            newsRefreshing--;
            if (newsRefreshing == 0 && callbackExists) {
                newsRefreshedCallback.allNewsInPullRefreshed();
            }
        }
    }

    public interface NewsRefreshedCallback {
        public void beforeNewStartRefreshing(String url);
        public void newRefreshedForUrl(String url);

        public void allNewsInPullRefreshed();

        public void errorOnRefreshing(Exception ex, String refreshingUrl);
    }


}
