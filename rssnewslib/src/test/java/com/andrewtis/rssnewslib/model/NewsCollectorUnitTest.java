package com.andrewtis.rssnewslib.model;

import junit.framework.Assert;

import org.junit.Test;

public class NewsCollectorUnitTest {

    @Test
    public void getExtractorTest(){
        NewsCollector collector = new NewsCollector(null);
        String testingUrl = "myUrl";
        NewsExtractor extractor = collector.getNewsExtractor(testingUrl);
        Assert.assertEquals(testingUrl, extractor.rssNewsUrl);

    }
}
