package com.andrewtis.rssnewslib.utils;

public interface NewsGetter {
    public String getNewsXml();

    public String getNewsItem();

    public String getCorruptedItem();

    public String getNewsXmlWithIncorrectTags();

    public String getNewsWithIncorrectItemBody();

    public String getHalfOfNewsXml();

    public int getNewsCount();

    public int getNewsItemsWithRongBodyCount();
}
