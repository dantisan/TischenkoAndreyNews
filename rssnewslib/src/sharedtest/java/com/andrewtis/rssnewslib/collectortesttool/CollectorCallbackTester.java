package com.andrewtis.rssnewslib.collectortesttool;

import com.andrewtis.rssnewslib.model.NewsCollector;

import junit.framework.Assert;

public class CollectorCallbackTester implements NewsCollector.NewsRefreshedCallback {

    private int expectedBeforeRefreshingCount = -1;
    private int expectedRefreshedForUrlCount = -1;
    private int expectedErrorsCount = -1;


    private volatile int realBeforeRefreshingCount = 0;
    private volatile int realRefreshedForUrlCount = 0;
    private volatile int realErrorsCount = 0;

    public int getRealAllRefreshedCount() {
        return realAllRefreshedCount;
    }

    private volatile int realAllRefreshedCount = 0;


    public CollectorCallbackTester(int expectedBeforeRefreshingCount, int expectedRefreshedForUrlCount, int expectedErrorsCount) {
        this.expectedBeforeRefreshingCount = expectedBeforeRefreshingCount;
        this.expectedRefreshedForUrlCount = expectedRefreshedForUrlCount;
        this.expectedErrorsCount = expectedErrorsCount;

    }

    @Override
    public void beforeNewStartRefreshing(String url) {
        realBeforeRefreshingCount++;
    }

    @Override
    public void newRefreshedForUrl(String url) {
        realRefreshedForUrlCount++;
    }

    @Override
    public void allNewsInPullRefreshed() {
        realAllRefreshedCount++;
    }

    @Override
    public void errorOnRefreshing(Exception ex, String refreshingUrl) {
        realErrorsCount++;
    }

    public void checkExpectedCallbacksCount() {
        checkValue(expectedBeforeRefreshingCount, realBeforeRefreshingCount);
        checkValue(expectedErrorsCount, realErrorsCount);
        checkValue(expectedRefreshedForUrlCount, realRefreshedForUrlCount);
    }

    private void checkValue(int expected, int realVal) {
        if (expected > 0)
            Assert.assertEquals(expected, realVal);
    }
}
