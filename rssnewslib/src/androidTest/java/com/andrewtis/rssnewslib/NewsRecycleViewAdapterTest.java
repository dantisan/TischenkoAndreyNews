package com.andrewtis.rssnewslib;


import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.andrewtis.rssnewslib.model.NewsCollector;

import junit.framework.Assert;

import java.util.concurrent.TimeUnit;

import dagger.ObjectGraph;

public class NewsRecycleViewAdapterTest extends AndroidTestCase {

    NewsRecycleViewAdapter testedAdapter ;
    private final static int urlCount = 3;
    private final static int newsInUrlCount = 10;

    NewsCollector newsCollector = new NewsCollectorStub(null, urlCount,newsInUrlCount);

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        RecycleViewAdapterModule module =new RecycleViewAdapterModule(urlCount, mContext,newsCollector);
        ObjectGraph graph = ObjectGraph.create(module);
        testedAdapter = graph.get(NewsRecycleViewAdapter.class);
        int timeout = 1000;
        testedAdapter.refreshNews();

        module.getNewsRefreshedLatch().await(timeout, TimeUnit.MILLISECONDS);

    }


    @SmallTest
    public void testItemsCount(){
        Assert.assertEquals(newsCollector.getNewsCount(), testedAdapter.getItemCount());
    }

    @SmallTest
    public void testProgressViewsVisibility(){

    }

    @SmallTest
    public void testViewsContent(){

    }

}
