package com.andrewtis.rssnewslib;

import android.content.Context;
import android.view.View;

import com.andrewtis.rssnewslib.model.NewsCollector;

import java.util.concurrent.CountDownLatch;

import dagger.Module;
import dagger.Provides;

@Module(injects = NewsRecycleViewAdapter.class)
public class RecycleViewAdapterModule{

    private Context mContext;
    private NewsCollector collector;

    private TesterRefreshCallbacks testerRefreshCallbacks;
    public RecycleViewAdapterModule(int newsShouldRefreshedCount, Context mContext, NewsCollector collector){
        testerRefreshCallbacks = new TesterRefreshCallbacks(newsShouldRefreshedCount);
        this.mContext = mContext;
        this.collector = collector;
    }

    public CountDownLatch getNewsRefreshedLatch(){
        return testerRefreshCallbacks.getLatch();
    }
    @Provides
    Context getContext(){
        return mContext;
    }

    @Provides
    NewsCollector getCollector(){
        return collector;
    }

    @Provides
    View getProgressView(){
        return new View(mContext);
    }

    @Provides
    NewsCollector.NewsRefreshedCallback getCallback(){
        return testerRefreshCallbacks;
    }

    class TesterRefreshCallbacks implements NewsCollector.NewsRefreshedCallback{
        public CountDownLatch getLatch() {
            return latch;
        }

        CountDownLatch latch;
        public TesterRefreshCallbacks(int newsShouldRefreshed){
            latch = new CountDownLatch(newsShouldRefreshed);
        }
        @Override
        public void beforeNewStartRefreshing(String url) {

        }

        @Override
        public void newRefreshedForUrl(String url) {
            latch.countDown();
        }

        @Override
        public void errorOnRefreshing(Exception ex, String refreshingUrl) {
            latch.countDown();
        }
    }

}


