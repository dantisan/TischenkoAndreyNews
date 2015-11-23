package com.andrewtis.rssnewslib.model;

import junit.framework.Assert;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;

import static com.andrewtis.rssnewslib.utils.TestNewsProducer.getNewsProducer;
import static com.andrewtis.rssnewslib.utils.TestNewsProducer.newsFirm;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


public class NewsInfoUnitTest {

    @Test
    public void checkFields() throws ParseException {
        NewsInfo newsInfo = new NewsInfo("title", "description", null, "Wed, 18 Nov 2015 00:49:00 +0300");
        assertNotNull(newsInfo.getImgUrl());
        assertNotNull(newsInfo.getTitle());
        assertNotNull(newsInfo.getDescription());
        assertNotNull(newsInfo.getNewsDate());

        assertEquals(newsInfo.getTitle(), "title");
        assertEquals(newsInfo.getDescription(), "description");
        assertEquals(newsInfo.getImgUrl(), "");
        assertEquals(newsInfo.getNewsDateString(), "Wed, 18 Nov 2015 00:49:00 +0300");
    }

    @Test
    public void checkNullFieldsToNPE() {
        NewsInfo newsInfo = new NewsInfo(null, null, null, null);
        assertNotNull(newsInfo.getImgUrl());
        assertNotNull(newsInfo.getTitle());
        assertNotNull(newsInfo.getDescription());
        assertNotNull(newsInfo.getNewsDateString());
        assertNull(newsInfo.getNewsDate());
    }

    @Test
    public void checkTimeNotFullConversion() {
        NewsInfo newsInfoNotFullDate = new NewsInfo("description", "", "title", "Wed, 18 Nov 2015");
        assertNull(newsInfoNotFullDate.getNewsDate());
    }

    @Test
    public void checkWrongTimeConversion() {
        NewsInfo newsInfoNotFullDate = new NewsInfo("description", "", "title", "ErrorDay, 18 Nov 2015 00:49:00 +0300");
        assertNull(newsInfoNotFullDate.getNewsDate());
    }

    @Test
    public void checkNullTimeConversion() {
        NewsInfo newsInfoNotFullDate = new NewsInfo("description", "", "title", null);
        assertNull(newsInfoNotFullDate.getNewsDate());
    }

    @Test
    public void checkTimeConversion() {
        NewsInfo newsInfoNotFullDate = new NewsInfo("description", "", "title", "Wed, 18 Nov 2015 00:49:00 +0300");
        newsInfoNotFullDate.getNewsDate();
    }


    @Test
    public void checkTimeCompare() {
        NewsInfo newsBiggerDate = new NewsInfo("description", "", "title", "Wed, 18 Nov 2015 00:49:00 +0300");
        NewsInfo newsSmallerDate = new NewsInfo("description", "", "title", "Wed, 18 Nov 2015 00:47:00 +0300");
        assertEquals(newsBiggerDate.compareTo(newsSmallerDate), 1);
        assertEquals(newsBiggerDate.compareTo(newsBiggerDate), 0);
        assertEquals(newsSmallerDate.compareTo(newsSmallerDate), 0);
        assertEquals(newsSmallerDate.compareTo(newsBiggerDate), -1);

    }

    @Test
    public void checkNewsInfoFromXml() throws ParserConfigurationException, SAXException, IOException, NewsInfo.NewsInfoXmlException {
        NewsInfo gazetaInfo = NewsInfo.getNewsFromXml(getNewsProducer(newsFirm.GAZETA).getNewsItem());
        NewsInfo lentaInfo = NewsInfo.getNewsFromXml(getNewsProducer(newsFirm.LENTA).getNewsItem());
        Assert.assertNotNull(gazetaInfo);
        Assert.assertNotNull(lentaInfo);


    }

    @Test(expected = SAXException.class)
    public void checkNewsInfoFromXmlWithoutItem() throws ParserConfigurationException, SAXException, NewsInfo.NewsInfoXmlException, IOException {
        String itemNewsString = getNewsProducer(newsFirm.GAZETA).getNewsItem();
        String corruptedItem = itemNewsString.replace("<item>", "").replace("</item>", "");
        NewsInfo corruptedInfo = NewsInfo.getNewsFromXml(corruptedItem);
        Assert.fail("corrupted new passed");
    }

    @Test(expected = NewsInfo.NewsInfoXmlException.class)
    public void checkNewsInfoFromXmlWithWrongItem() throws ParserConfigurationException, SAXException, NewsInfo.NewsInfoXmlException, IOException {
        String itemNewsString = getNewsProducer(newsFirm.GAZETA).getNewsItem();
        String corruptedItem = itemNewsString.replace("<item>", "<new>").replace("</item>", "</new>");
        NewsInfo corruptedInfo = NewsInfo.getNewsFromXml(corruptedItem);
        Assert.fail("corrupted xml passed");
    }

    @Test(expected = NewsInfo.NewsInfoXmlException.class)
    public void checkNewsInfoFromXmlWithoutNeedItems() throws ParserConfigurationException, SAXException, NewsInfo.NewsInfoXmlException, IOException {
        String corruptedItem = getNewsProducer(newsFirm.GAZETA).getCorruptedItem();
        NewsInfo corruptedInfo = NewsInfo.getNewsFromXml(corruptedItem);
        Assert.fail("corrupted new info without necessary fields passed");
    }

    @Test
    public void checkNewsInfoFromXmlWithoutUnnecessaryItems() throws ParserConfigurationException, SAXException, NewsInfo.NewsInfoXmlException, IOException {
        String item = getNewsProducer(newsFirm.GAZETA).getNewsItem();
        item = item.replace("pubDate", "dat");
        item = item.replace("enclosure", "enclos");

        NewsInfo corruptedInfo = NewsInfo.getNewsFromXml(item);
        assertEquals("", corruptedInfo.getImgUrl());
        assertEquals("", corruptedInfo.getNewsDateString());
        assertNull(corruptedInfo.getNewsDate());
    }

    @Test
    public void checkEquals(){
        NewsInfo info = new NewsInfo("t1","d1","","Wed, 18 Nov 2015 00:49:00 +0300");
        NewsInfo copyInfo = new NewsInfo("t1","d1","","Wed, 18 Nov 2015 00:49:00 +0300");
        NewsInfo diffDateInfo = new NewsInfo("t1","d1","","Wed, 18 Nov 2015 00:50:00 +0300");
        NewsInfo diffTitleInfo = new NewsInfo("t2","d1","","Wed, 18 Nov 2015 00:49:00 +0300");
        NewsInfo diffDescrInfo = new NewsInfo("t1","d2","","Wed, 18 Nov 2015 00:49:00 +0300");
        NewsInfo diffImgUrlInfo = new NewsInfo("t1","d1","Url","Wed, 18 Nov 2015 00:49:00 +0300");

        Assert.assertTrue(info.equals(info));
        Assert.assertFalse(info.equals(null));
        Assert.assertTrue(info.equals(copyInfo));
        Assert.assertFalse(info.equals(diffDateInfo));
        Assert.assertFalse(info.equals(diffTitleInfo));
        Assert.assertFalse(info.equals(diffDescrInfo));
        Assert.assertFalse(info.equals(diffImgUrlInfo));

    }
}