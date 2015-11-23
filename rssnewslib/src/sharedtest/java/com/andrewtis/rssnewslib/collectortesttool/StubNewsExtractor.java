package com.andrewtis.rssnewslib.collectortesttool;

import android.support.annotation.NonNull;

import com.andrewtis.rssnewslib.model.NewsExtractor;
import com.andrewtis.rssnewslib.model.NewsInfo;

import org.joda.time.DateTime;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;


public class StubNewsExtractor extends NewsExtractor {

    private List<NewsInfo> newsList = new ArrayList<>();

    public boolean isShouldFailNews() {
        return shouldFailNews;
    }

    private boolean shouldFailNews = false;
    private long delay = 0;

    private StubNewsExtractor() {
        super("");
        shouldFailNews = true;
    }

    public StubNewsExtractor(@NonNull List<NewsInfo> newsList, long delay) {
        super("");
        this.newsList.addAll(newsList);
        shouldFailNews = false;
        this.delay = delay;
    }

    @Override
    public List<NewsInfo> getNews() throws IOException, SAXException, ParserConfigurationException {
        if (delay > 0)
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                System.err.println("Interrupted sleep in emulated getNews");
                e.printStackTrace();
            }

        if (shouldFailNews) throw new IOException("Test fail news exception");
        return newsList;
    }

    public List<NewsInfo> getNewsList() {
        return newsList;
    }


    public static StubNewsExtractor getStubNewsExtractor(int newsInExtractorCount, DateTime beginNewsTime, long extractorDelay) {
        List<NewsInfo> newsList = new ArrayList<>();
        for (Integer i = 0; i < newsInExtractorCount; i++) {
            beginNewsTime.plusSeconds(i);
            String dateStr = beginNewsTime.toString(NewsInfo.getRfc1123DateTimeFormatter());
            NewsInfo newsInfo = new NewsInfo(i.toString(), i.toString(), i.toString(), dateStr);
            newsList.add(newsInfo);
        }
        return new StubNewsExtractor(newsList, extractorDelay);
    }

    public static StubNewsExtractor getFailedExtractor() {
        return new StubNewsExtractor();
    }
}
