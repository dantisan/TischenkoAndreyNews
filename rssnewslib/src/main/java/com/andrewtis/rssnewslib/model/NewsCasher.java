package com.andrewtis.rssnewslib.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NewsCasher extends NewsCollector {

    private Map<String, List<NewsInfo>> newsCollection = new ConcurrentHashMap<>();
    private List<NewsInfo> sortedNews = Collections.synchronizedList(new ArrayList<NewsInfo>());
    private NewsCashedOnCallback refreshingCallback = new NewsCashedOnCallback();

    protected NewsCasher(){
        super();
        addNewsRefreshedCallback(refreshingCallback);
    }

    public NewsCasher(NewsRefreshedCallback newsRefreshedCallback) {
        this();
        addNewsRefreshedCallback(newsRefreshedCallback);
    }

    public NewsCasher(NewsRefreshedCallback newsRefreshedCallback, Iterable<String> newsUrls) {
        this(newsRefreshedCallback);
        addCacheUrl(newsUrls);
    }

    public int getNewsCount(){
        return sortedNews.size();
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
        for (NewsInfo cur : news) {
            if(sortedNews!=cur)
                sortedNews.add(cur);
        }
        //sortedNews.addAll(news);
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


    private class NewsCashedOnCallback implements NewsCollector.NewsRefreshedCallback{

        @Override
        public void beforeNewStartRefreshing(String url) {

        }

        @Override
        public void newRefreshedForUrl(String url, List<NewsInfo> refreshedNews) {
            newsCollection.put(url, refreshedNews);
            addUrlNews(refreshedNews);
        }

        @Override
        public void errorOnRefreshing(Exception ex, String refreshingUrl) {
            if(newsCollection.get(refreshingUrl)==null)
                newsCollection.put(refreshingUrl, new ArrayList<NewsInfo>());
        }
    }



}
