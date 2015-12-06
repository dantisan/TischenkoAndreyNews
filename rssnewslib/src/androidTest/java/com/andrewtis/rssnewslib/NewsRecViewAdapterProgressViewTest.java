package com.andrewtis.rssnewslib;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import junit.framework.Assert;
import dagger.ObjectGraph;

public class NewsRecViewAdapterProgressViewTest  extends InstrumentationTestCase {

    NewsRecycleViewAdapter testedAdapter ;

    NewsCollectorStub newsCollector;

    protected Context mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getContext();

        RecycleViewAdapterModule module = initDIModule();
        ObjectGraph graph = ObjectGraph.create(module);
        testedAdapter = graph.get(NewsRecycleViewAdapter.class);

        newsCollector = module.getNewsCollectorStub();
        testedAdapter.refreshNews();
        newsCollector.waitAllNewsRefreshed();
    }

    protected RecycleViewAdapterModule initDIModule(){
        RecycleViewAdapterModule module =new RecycleViewAdapterModule(mContext, null);
        return module;
    }

    @SmallTest
    public void testProgressViews(){
        getInstrumentation().waitForIdleSync();
        Assert.assertNull(testedAdapter.progressView);
    }
}
