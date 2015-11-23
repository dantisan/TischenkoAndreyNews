package com.andrewtis.rssnewslib.utils;

public abstract class TestNewsProducer implements NewsGetter {

    protected String newsXml;

    protected String newsItemXml;

    public TestNewsProducer(String newsXml, String newsItemXml) {
        this.newsXml = newsXml;
        this.newsItemXml = newsItemXml;
    }


    @Override
    public String getNewsXml() {
        return newsXml;
    }

    @Override
    public String getNewsItem() {
        return newsItemXml;
    }

    @Override
    public String getCorruptedItem() {
        String corruptedItem = newsItemXml.replace("title", "titl");
        corruptedItem = corruptedItem.replace("description", "descr");
        corruptedItem = corruptedItem.replace("pubDate", "dat");
        corruptedItem = corruptedItem.replace("enclosure", "enclos");
        return corruptedItem;
    }

    @Override
    public String getNewsXmlWithIncorrectTags() {

        String corruptedItem = newsItemXml.replace("title", "titl");
        corruptedItem = corruptedItem.replace("description", "descr");
        corruptedItem = corruptedItem.replace("pubDate", "date");
        corruptedItem = corruptedItem.replace("enclosure", "enclos");
        return corruptedItem;
    }

    @Override
    public String getNewsWithIncorrectItemBody() {

        String beginTag = "<item>";
        int indexOfBeginFrstItem = newsXml.indexOf("<item>");

        String endTag = "</item>";
        int indexOfEndFrstItem = newsXml.indexOf(endTag);

        return newsXml.substring(0, indexOfBeginFrstItem) + beginTag + " Incorrect body" + newsXml.substring(indexOfEndFrstItem);
    }

    @Override
    public int getNewsItemsWithRongBodyCount() {
        return 1;
    }

    public enum newsFirm {
        LENTA,
        GAZETA
    }

    public static TestNewsProducer getNewsProducer(newsFirm mediaFirm) {
        switch (mediaFirm) {
            case LENTA:
                return new LentaTestNews();
            case GAZETA:
                //return new GazetaTestNews();
            default:
                return new GazetaTestNews();
        }

    }
}
