package com.andrewtis.rssnewslib.model;

import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NewsCollector {
    protected Map<String, List<NewsInfo>> newsCollection = new ConcurrentHashMap<>();
    private List<NewsInfo> sortedNews = Collections.synchronizedList(new ArrayList<NewsInfo>());

    public void setNewsRefreshedCallback(NewsRefreshedCallback newsRefreshedCallback) {
        this.newsRefreshedCallback = newsRefreshedCallback;
    }

    private NewsRefreshedCallback newsRefreshedCallback = null;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public NewsCollector(NewsRefreshedCallback newsRefreshedCallback) {
        this.newsRefreshedCallback = newsRefreshedCallback;
    }

    public NewsCollector(NewsRefreshedCallback newsRefreshedCallback, Iterable<String> newsUrls) {
        this.newsRefreshedCallback = newsRefreshedCallback;
        addCacheUrl(newsUrls);
    }

    public int getNewsCount(){
        int count = 0;
        for (List<NewsInfo> news : newsCollection.values()) {
            count+=news.size();
        }
        return count;
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

    private void addUrlNews(List<NewsInfo> news){
        sortedNews.addAll(news);
        Collections.sort(sortedNews);
    }

    public List<NewsInfo> getAllNewsInfo(){
        List<NewsInfo> res = new ArrayList<>();
        res.addAll(sortedNews);
        return res;
    }

    //Возможно некорректное поведение при возврате колбэка для удаленной новости
    public void removeCacheUrl(String url){
        List<NewsInfo> removedNews = newsCollection.remove(url);
        sortedNews.removeAll(removedNews);
    }

    public void addCacheUrl(Iterable<String> urlCollection){
        if(urlCollection == null)
            return;

        for (String url : urlCollection) {
            addCacheUrl(url);
        }
    }

    public void addCacheUrl(String url){
        if(newsCollection.get(url)==null){
            newsCollection.put(url, new ArrayList<NewsInfo>());
        }
    }

    public void refreshCachedNews(){
        refreshNewsForUrl(newsCollection.keySet());
    }

    public void refreshNewsForUrl(Iterable<String> urlList){
        for (String url : urlList) {
            refreshNewsForUrl(url);
        }
    }

    public Set<String> getCachedUrls(){
        return newsCollection.keySet();
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
        boolean callbackExists = newsRefreshedCallback != null;
        if(callbackExists)
            newsRefreshedCallback.beforeNewStartRefreshing(newsUrl);

        NewsExtractor newsExtractor = getNewsExtractor(newsUrl);
        List<NewsInfo> newsList = null;

        try {
            newsList = newsExtractor.getNews();
            newsCollection.put(newsUrl, newsList);
            if(callbackExists) {
                addUrlNews(newsList);
                newsRefreshedCallback.newRefreshedForUrl(newsUrl);
            }
        } catch (Exception ex) {
            if(newsCollection.get(newsUrl)==null)
                newsCollection.put(newsUrl, new ArrayList<NewsInfo>());

            if (callbackExists)
                newsRefreshedCallback.errorOnRefreshing(ex, newsUrl);
            //newsCollection.remove(newsUrl);
        }
    }

    public interface NewsRefreshedCallback {
        public void beforeNewStartRefreshing(String url);
        public void newRefreshedForUrl(String url);
        public void errorOnRefreshing(Exception ex, String refreshingUrl);
    }


}
