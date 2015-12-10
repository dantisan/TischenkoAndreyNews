package com.andrewtis.rssnewslib.model;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class NewsInfo implements Comparable<NewsInfo> {

    private static final DateTimeFormatter RFC1123_DATE_TIME_FORMATTER =
            DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss Z")
                    .withZoneUTC().withLocale(Locale.US);
    private String title;
    private String description;
    private String imgUrl;
    private String newsDate;

    private long dateMillis = 0;

    public boolean equals(NewsInfo cmpNews) {
        if(cmpNews==null)
            return false;
        if(cmpNews==this)
            return true;

        boolean eq = cmpNews.title.equals(title);
        eq &= cmpNews.description.equals(description);
        eq &= cmpNews.imgUrl.equals(imgUrl);
        eq &= cmpNews.newsDate.equals(newsDate);
        eq &= cmpNews.dateMillis == dateMillis;
        return eq;
    }

    public NewsInfo(String title, String description, String imgUrl, String newsDate) {
        this.description = noNullString(description);
        this.imgUrl = noNullString(imgUrl);
        this.title = noNullString(title);

        this.newsDate = noNullString(newsDate);

        DateTime date = getNewsDate();
        dateMillis = date == null ? 0 : date.getMillis();
    }

    private String noNullString(String str) {
        return str == null ? "" : str;
    }

    public String getDescription() {
        return description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getNewsDateString() {
        return newsDate;
    }

    /**
     * Метод парсит строку с датой новости в формате RFC1123 в joda.time.DateTime
     * @return joda.time.DateTime в случае успеха, null иначе
     */
    @Nullable
    public DateTime getNewsDate() {
        DateTime date = null;
        try {
            date = RFC1123_DATE_TIME_FORMATTER.parseDateTime(newsDate);
        } catch (Exception ex) {
            Log.w("DATE_PARSE", "Error in trying parse to date string '" + newsDate + "'");
            //ex.printStackTrace();
        }
        return date;
    }

    @Override
    public int compareTo(@NonNull NewsInfo another) {
        long dif = this.dateMillis - another.dateMillis;

        if (dif > 0) {
            return 1;
        } else if (dif < 0) {
            return -1;
        }
        return 0;
    }

    /**
     * Фабричный метод для получения экземпляра NewsInfo по его Node
     * Предполагаемая структура соответствует элементу item из http://www.gazeta.ru/export/rss/lenta.xml и http://lenta.ru/rss
     *
     * @param newsItemNode узнл item для новости
     * @return экземпляр NewsInfo по xml
     * @throws NewsInfoXmlException         Возникает при неверном фомате xml, переданного на вход (отсутсвует элемент item, или в статье нет заголовка и описания)
     */
    public static NewsInfo getNewsFromXml(Node newsItemNode) throws NewsInfoXmlException {
        if (newsItemNode == null || newsItemNode.getNodeType() != Node.ELEMENT_NODE) {
            throw new NewsInfoXmlException("newsItemNode has no element node");
        }

        Element itemElement = (Element) newsItemNode;

        Node titleNode = itemElement.getElementsByTagName("title").item(0);
        Node descriptionNode = itemElement.getElementsByTagName("description").item(0);
        Node dateNode = itemElement.getElementsByTagName("pubDate").item(0);
        Node enclosureNode = itemElement.getElementsByTagName("enclosure").item(0);

        if(titleNode==null && descriptionNode==null){
            throw new NewsInfoXmlException("New has now title and description");
        }

        String title = getNodeText(titleNode);
        String description = getNodeText(descriptionNode);
        String pubDate = getNodeText(dateNode);
        String imgUrl = "";
        if (enclosureNode != null) {
            Node urlNode = enclosureNode.getAttributes().getNamedItem("url");
            imgUrl = getNodeText(urlNode);
        }

        return new NewsInfo(title, description, imgUrl, pubDate);
    }
    /**
     * Фабричный метод для получения экземпляра NewsInfo по его xml
     * Предполагаемая структура соответствует элементу item из http://www.gazeta.ru/export/rss/lenta.xml и http://lenta.ru/rss
     *
     * @param newsXml строка с xml для узла item из новости
     * @return экземпляр NewsInfo по xml
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws NewsInfoXmlException         при неверном фомате xml, переданного на вход
     * (отсутсвует элемент item  или он не соответствует ожидаемому формату)
     */
    public static NewsInfo getNewsFromXml(String newsXml) throws ParserConfigurationException, IOException, SAXException, NewsInfoXmlException {

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(newsXml)));

        Node itemNode = doc.getElementsByTagName("item").item(0);
        return getNewsFromXml(itemNode);
    }

    private static String getNodeText(Node node) {
        if (node == null)
            return "";
        return node.getTextContent();
    }

    public static class NewsInfoXmlException extends Exception {
        public NewsInfoXmlException(String detailMessage) {
            super(detailMessage);
        }
    }

    public
    static DateTimeFormatter getRfc1123DateTimeFormatter() {
        return RFC1123_DATE_TIME_FORMATTER;
    }
}
