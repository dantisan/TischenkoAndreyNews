package com.andrewtis.rssnewslib.model;

import com.andrewtis.rssnewslib.utils.TestNewsProducer;

import junit.framework.Assert;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NewsExtractorUnitTest {

    TestNewsProducer gazetaNews = TestNewsProducer.getNewsProducer(TestNewsProducer.newsFirm.GAZETA);
    TestNewsProducer lentaNews = TestNewsProducer.getNewsProducer(TestNewsProducer.newsFirm.LENTA);
    @Test
    public void checkNewsInfoCreation() throws NewsInfo.NewsInfoXmlException, IOException, ParserConfigurationException, SAXException {
        NewsExtractor extractor = mock(NewsExtractor.class);
        when(extractor.getNews()).thenCallRealMethod();
        assertNewsCount(extractor, gazetaNews.getNewsXml(),gazetaNews.getNewsCount());
        assertNewsCount(extractor, lentaNews.getNewsXml(),lentaNews.getNewsCount());
    }


    @Test
    public void checkNewsInfoCreationWithCorrupted() throws NewsInfo.NewsInfoXmlException, IOException, ParserConfigurationException, SAXException {

        NewsExtractor extractor = mock(NewsExtractor.class);
        when(extractor.getNews()).thenCallRealMethod();

        assertNewsCount(extractor, gazetaNews.getNewsWithIncorrectItemBody(), gazetaNews.getNewsCount() - gazetaNews.getNewsItemsWithRongBodyCount());
        assertNewsCount(extractor, lentaNews.getNewsWithIncorrectItemBody(), lentaNews.getNewsCount() - lentaNews.getNewsItemsWithRongBodyCount());

    }

    private void assertNewsCount(NewsExtractor mockedExtractor, String inXmlNewsString, int expectedCount) throws IOException, ParserConfigurationException, SAXException {
        doReturn(inXmlNewsString).when(mockedExtractor).getUrlContent();
        List<NewsInfo> newsList = mockedExtractor.getNews();
        Assert.assertEquals(expectedCount, newsList.size());
    }

}
