package com.andrewtis.rssnewslib.model.casher;

import com.andrewtis.rssnewslib.NewsCollectorStub;
import com.andrewtis.rssnewslib.model.NewsInfo;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class NewsCasherUnitTest {

    @Test(timeout = 1000)
    public void newsDuplicatingTest() throws InterruptedException {
        NewsCollectorStub stub = new NewsCollectorStub(2,20,200);
        stub.refreshCachedNews();
        stub.waitAllNewsRefreshed();

        stub.reinitLatch();
        stub.refreshCachedNews();
        stub.waitAllNewsRefreshed();

        Assert.assertEquals(40,stub.getNewsCount());
    }




}
