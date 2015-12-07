package com.andrewtis.tischenkoandreynews;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

import android.os.Bundle;

import com.andrewtis.rssnewslib.RssNewsFragment;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static String rssFragmentTag = "rssURL";
    private Fragment rssFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> refreshedUrls = new ArrayList<>();
        refreshedUrls.add("http://lenta.ru/rss");
        refreshedUrls.add("http://www.gazeta.ru/export/rss/lenta.xml");

        rssFragment = getFragmentManager().findFragmentByTag(rssFragmentTag);
        if(rssFragment ==null) {
            rssFragment = RssNewsFragment.newInstance(refreshedUrls);

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(android.R.id.content, rssFragment, rssFragmentTag);
            fragmentTransaction.commit();
            getFragmentManager().executePendingTransactions();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //rssFragment.getView().postInvalidate();

    }
}
