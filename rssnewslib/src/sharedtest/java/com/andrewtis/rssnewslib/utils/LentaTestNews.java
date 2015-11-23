package com.andrewtis.rssnewslib.utils;


public class LentaTestNews extends TestNewsProducer {

    private static final String lentaXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<rss version=\"2.0\" xmlns:atom=\"http://www.w3.org/2005/Atom\">\n" +
            "    <channel>\n" +
            "        <language>ru</language>\n" +
            "        <title>Lenta.ru : Новости</title>\n" +
            "        <description>Новости, статьи, фотографии, видео. Семь дней в неделю, 24 часа в сутки</description>\n" +
            "        <link>http://lenta.ru</link>\n" +
            "        <image>\n" +
            "            <url>http://assets.lenta.ru/small_logo.png</url>\n" +
            "            <title>Lenta.ru</title>\n" +
            "            <link>http://lenta.ru</link>\n" +
            "            <width>134</width>\n" +
            "            <height>22</height>\n" +
            "        </image>\n" +
            "        <atom:link rel=\"self\" type=\"application/rss+xml\" href=\"http://lenta.ru/rss\"/>\n" +
            "        <item>\n" +
            "            <guid>http://lenta.ru/news/2015/11/16/ator/</guid>\n" +
            "            <title>В АТОР предупредили о новых проблемах турбизнеса из-за терактов в Париже</title>\n" +
            "            <link>http://lenta.ru/news/2015/11/16/ator/</link>\n" +
            "            <description>\n" +
            "                <![CDATA[Спрос туристов на поездки во Францию может снизиться на 5-10 процентов из-за терактов в Париже 13 ноября. Такой прогноз сделала Ассоциация туроператоров России (АТОР). Продажи туров в ближайшие неделю-две могут и вовсе остановиться. После чего спрос, вероятно, начнет восстанавливаться.]]>\n" +
            "            </description>\n" +
            "            <pubDate>Mon, 16 Nov 2015 11:46:28 +0300</pubDate>\n" +
            "            <enclosure url=\"http://icdn.lenta.ru/images/2015/11/16/11/20151116110306915/pic_cf1177c09c2fc19554b46153dd0b4b9e.jpg\" length=\"48185\" type=\"image/jpeg\"/>\n" +
            "            <category>Путешествия</category>\n" +
            "        </item>\n" +
            "        <item>\n" +
            "            <guid>http://lenta.ru/news/2015/11/16/guccialessandro/</guid>\n" +
            "            <title>Креативный директор Gucci получил международную премию в области дизайна</title>\n" +
            "            <link>http://lenta.ru/news/2015/11/16/guccialessandro/</link>\n" +
            "            <description>\n" +
            "                <![CDATA[Дизайнер Алессандро Микеле стал креативным директором дома Gucci в январе 2015 года, после внезапной отставки его предшественницы Фриды Джаннини, и авторитетная организация в области моды Британский модный совет сочла его достижения за 9 месяцев на новом посту достаточными для присуждения международной награды.]]>\n" +
            "            </description>\n" +
            "            <pubDate>Mon, 16 Nov 2015 11:39:50 +0300</pubDate>\n" +
            "            <enclosure url=\"http://icdn.lenta.ru/images/2015/11/16/10/20151116105901223/pic_4c6ae8f680f5041fc4db57b2c3e655f3.jpg\" length=\"50600\" type=\"image/jpeg\"/>\n" +
            "            <category>Ценности</category>\n" +
            "        </item>\n" +
            "        <item>\n" +
            "            <guid>http://lenta.ru/news/2015/11/16/lyon/</guid>\n" +
            "            <title>У задержанных в Лионе изъяли автомат Калашникова и гранатомет</title>\n" +
            "            <link>http://lenta.ru/news/2015/11/16/lyon/</link>\n" +
            "            <description>\n" +
            "                <![CDATA[Французские спецслужбы в ходе масштабной антитеррористической операции задержали пятерых жителей Лиона. У них изъято оружие, в том числе автомат Калашникова и гранатомет. В квартирах задержанных были также обнаружены множество пистолетов и бронежилеты. Всего в Лионе прошли 13 обысков.]]>\n" +
            "            </description>\n" +
            "            <pubDate>Mon, 16 Nov 2015 11:33:00 +0300</pubDate>\n" +
            "            <enclosure url=\"http://icdn.lenta.ru/images/2015/11/16/11/20151116112844550/pic_e386af4d701292465f966cb89af1c169.jpg\" length=\"55036\" type=\"image/jpeg\"/>\n" +
            "            <category>Мир</category>\n" +
            "        </item>    " +
            "</channel>\n" +
            "</rss>";

    private static final String lentaItem =
            "        <item>\n" +
                    "            <guid>http://lenta.ru/news/2015/11/16/guccialessandro/</guid>\n" +
                    "            <title>Креативный директор Gucci получил международную премию в области дизайна</title>\n" +
                    "            <link>http://lenta.ru/news/2015/11/16/guccialessandro/</link>\n" +
                    "            <description>\n" +
                    "                <![CDATA[Дизайнер Алессандро Микеле стал креативным директором дома Gucci в январе 2015 года, после внезапной отставки его предшественницы Фриды Джаннини, и авторитетная организация в области моды Британский модный совет сочла его достижения за 9 месяцев на новом посту достаточными для присуждения международной награды.]]>\n" +
                    "            </description>\n" +
                    "            <pubDate>Mon, 16 Nov 2015 11:39:50 +0300</pubDate>\n" +
                    "            <enclosure url=\"http://icdn.lenta.ru/images/2015/11/16/10/20151116105901223/pic_4c6ae8f680f5041fc4db57b2c3e655f3.jpg\" length=\"50600\" type=\"image/jpeg\"/>\n" +
                    "            <category>Ценности</category>\n" +
                    "        </item>\n";

    public LentaTestNews() {
        super(lentaXml, lentaItem);
    }

    @Override
    public int getNewsCount() {
        return 3;
    }

}
