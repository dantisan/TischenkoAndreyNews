package com.andrewtis.rssnewslib.utils;

public class GazetaTestNews extends TestNewsProducer {
    private static final String gazetaXml = "<?xml version=\"1.0\" encoding=\"windows-1251\"?>\n" +
            "<rss version=\"2.0\">\n" +
            "    <channel>\n" +
            "        <title>Газета.Ru - Хроника дня</title>\n" +
            "        <link>http://www.gazeta.ru/news/lenta/</link>\n" +
            "        <description>Новости в режиме он-лайн из всех областей жизни. Собственная информация, а также сообщения крупнейших российских и мировых информационных агентств</description>\n" +
            "        <pubDate>Mon, 16 Nov 2015 11:42:03 +0300</pubDate>\n" +
            "        <language>ru</language>\n" +
            "        <copyright>Gazeta.Ru</copyright>\n" +
            "        <webMaster>webmaster@gazeta.ru</webMaster>\n" +
            "        <ttl>20</ttl>\n" +
            "        <item>\n" +
            "            <title>Дипломаты пока не выяснили судьбу пропавшей в Париже россиянки</title>\n" +
            "            <link>http://www.gazeta.ru/social/news/2015/11/16/n_7893701.shtml</link>\n" +
            "            <author>Газета.Ru</author>\n" +
            "            <pubDate>Mon, 16 Nov 2015 11:41:26 +0300</pubDate>\n" +
            "            <description>Российским дипломатам в Париже пока не удалось выйти на связь с россиянкой Натальей Муравьевой (Булыгиной-Лорэн), пропавшей в день серии терактов во французской столице, передает РИА &quot;Новости&quot;.\n" +
            "\n" +
            "                &quot;Консульский отдел посольства ...</description>\n" +
            "            <guid>http://www.gazeta.ru/social/news/2015/11/16/n_7893701.shtml</guid>\n" +
            "        </item>\n" +
            "        <item>\n" +
            "            <title>В Москве мужчина с ножом пытался ограбить водителя Lexus на $2 миллиона</title>\n" +
            "            <link>http://www.gazeta.ru/auto/news/2015/11/16/n_7893629.shtml</link>\n" +
            "            <author>Газета.Ru</author>\n" +
            "            <pubDate>Mon, 16 Nov 2015 11:31:52 +0300</pubDate>\n" +
            "            <description>Московская полиция задержала грабителя, требовавшего $2 млн у водителя Lexus. Об этом агентству городских новостей &quot;Москва&quot; сообщил источник в правоохранительных органах.\n" +
            "\n" +
            "                Сообщение о разбойном нападении на водителя иномарки на ...</description>\n" +
            "            <guid>http://www.gazeta.ru/auto/news/2015/11/16/n_7893629.shtml</guid>\n" +
            "        </item>             " +
            "           <item>\n" +
            "                   <title>В Ботсване найден самый крупный алмаз за последнее столетие</title>\n" +
            "                   <link>http://www.gazeta.ru/science/news/2015/11/19/n_7907369.shtml</link>\n" +
            "                   <author>Газета.Ru</author>\n" +
            "                           <pubDate>Thu, 19 Nov 2015 14:19:27 +0300</pubDate>\n" +
            "                           <description>Представители корпорации Lucara Diamond сообщают, что драгоценный камень в 1,111 карат был найден в северной части центральной Ботсваны. Это самый большой алмаз подобного рода, найденный за последнее столетие, сообщает The Telegraph. ...</description>\n" +
            "                   <guid>http://www.gazeta.ru/science/news/2015/11/19/n_7907369.shtml</guid>\n" +
            "        </item>" +
            "<rss2lj:owner xmlns:rss2lj=\"http://rss2lj.net/NS\">gazeta_admin</rss2lj:owner>\n" +
            "    </channel>\n" +
            "</rss>";
    private static final String gazetaItem =
            "        <item>\n" +
                    "            <title>Дипломаты пока не выяснили судьбу пропавшей в Париже россиянки</title>\n" +
                    "            <link>http://www.gazeta.ru/social/news/2015/11/16/n_7893701.shtml</link>\n" +
                    "            <author>Газета.Ru</author>\n" +
                    "            <pubDate>Mon, 16 Nov 2015 11:41:26 +0300</pubDate>\n" +
                    "            <description>Российским дипломатам в Париже пока не удалось выйти на связь с россиянкой Натальей Муравьевой (Булыгиной-Лорэн), пропавшей в день серии терактов во французской столице, передает РИА &quot;Новости&quot;.\n" +
                    "                &quot;Консульский отдел посольства ...</description>\n" +
                    "            <guid>http://www.gazeta.ru/social/news/2015/11/16/n_7893701.shtml</guid>\n</item>";

    public GazetaTestNews() {
        super(gazetaXml, gazetaItem);
    }


    @Override
    public int getNewsCount() {
        return 3;
    }


}
