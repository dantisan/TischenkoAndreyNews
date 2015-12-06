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
    private NewsCollectorStub collector = new NewsCollectorStub(null, urlCount,newsInUrlCount, timeout/(newsInUrlCount+2));;
    private View progressView;

    protected final static int urlCount = 3;
    protected final static int newsInUrlCount = 10;
    protected final static int  timeout = 1000;


    public RecycleViewAdapterModule(Context mContext, NewsCollectorStub collector, View progressView){
        this.mContext = mContext;
        this.progressView = progressView;
        this.collector = collector;
    }

    public RecycleViewAdapterModule(Context mContext,  View progressView){
        this.mContext = mContext;
        this.progressView = progressView;

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
        return progressView;
    }

    public NewsCollectorStub getNewsCollectorStub(){
        return collector;
    }

}


