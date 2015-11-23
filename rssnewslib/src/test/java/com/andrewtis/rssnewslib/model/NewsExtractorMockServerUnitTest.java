package com.andrewtis.rssnewslib.model;

import com.andrewtis.rssnewslib.utils.TestNewsProducer;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.andrewtis.rssnewslib.utils.TestNewsProducer.getNewsProducer;


public class NewsExtractorMockServerUnitTest {

    @Test
    public void test_getUrlContent() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start();

        String shortAnswer = "Testing answer";
        String longAnswer =  getNewsProducer(TestNewsProducer.newsFirm.GAZETA).getNewsXml();

        mockWebServer.enqueue(new MockResponse().setBody(shortAnswer));
        mockWebServer.enqueue(new MockResponse().setBody(longAnswer));

        NewsExtractor extractorBigAnswer = new NewsExtractor(mockWebServer.url("").toString());
        NewsExtractor extractorShortAnswer = new NewsExtractor(mockWebServer.url("").toString());

        String shortContentByUrl = extractorShortAnswer.getUrlContent();
        String longContent = extractorBigAnswer.getUrlContent();

        Assert.assertEquals(shortAnswer, shortContentByUrl);
        Assert.assertEquals(longAnswer,longContent);
        mockWebServer.shutdown();
    }

    @Test(expected = IOException.class)
    public void test_getUrlContentFail() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start();
        final int DELAY=200;
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("error for timeout").setBodyDelay(DELAY, TimeUnit.MILLISECONDS));

        NewsExtractor extractor = new NewsExtractor(mockWebServer.url("").toString());
        extractor.httpClient.setReadTimeout(DELAY / 2, TimeUnit.MILLISECONDS);
        try {
            extractor.getUrlContent();
        }
        finally {
            mockWebServer.shutdown();
        }
    }
}
