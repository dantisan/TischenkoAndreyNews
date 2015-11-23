package com.andrewtis.rssnewslib.utils;

public interface NewsGetter {
    String getNewsXml();

    String getNewsItem();

    String getCorruptedItem();

    String getNewsXmlWithIncorrectTags();

    String getNewsWithIncorrectItemBody();

    int getNewsCount();

    int getNewsItemsWithRongBodyCount();
}
