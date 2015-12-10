package com.andrewtis.rssnewslib.model;


import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class NewsExtractor {

    @NonNull String  rssNewsUrl;

    OkHttpClient httpClient = new OkHttpClient();

    public NewsExtractor(@NonNull String rssNewsUrl) {
        this.rssNewsUrl = rssNewsUrl;
    }

    public List<NewsInfo> getNews() throws IOException, SAXException, ParserConfigurationException {
        String newsXml = getUrlContent();
        List<NewsInfo> newsList = new ArrayList<>();

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(newsXml)));
        Element rssElem = doc.getDocumentElement();

        NodeList rssNewsList = rssElem.getElementsByTagName("item");
        for (int i = 0; i < rssNewsList.getLength(); i++) {
            NewsInfo newsItem = null;
            try {
                newsItem = NewsInfo.getNewsFromXml(rssNewsList.item(i));
            }catch ( NewsInfo.NewsInfoXmlException  ex){
                Log.w("NEWS_ITEM_ERROR", "error while trying parse node" + rssNewsList.item(i).getTextContent());
                //ex.printStackTrace();
            }
            if(newsItem !=null )newsList.add(newsItem);
        }

        return newsList;
    }

    @VisibleForTesting
     String getUrlContent() throws IOException {
        Request request = new Request.Builder()
                .url(rssNewsUrl)
                .build();
        Response response = httpClient.newCall(request).execute();

        return response.body().string();

    }
}
