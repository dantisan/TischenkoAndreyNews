package com.andrewtis.rssnewslib;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.ProgressBar;

import junit.framework.Assert;

import java.util.ArrayList;

import dagger.ObjectGraph;

public class RssFragmentTest extends ActivityUnitTestCase<Activity> {

    static NewsRecycleViewAdapter testedAdapter ;
    private NewsCollectorStub newsCollector;

    public RssFragmentTest(){
        super(Activity.class);
    }


    @SmallTest
    public void testFragmentInstance(){
        ArrayList<String> urls = new ArrayList<>();
        urls.add("url1");
        urls.add("url2");

        RssNewsFragment fragment = RssNewsFragment.newInstance(urls);
        ArrayList<String> savedUrls = fragment.getArguments().getStringArrayList(RssNewsFragment.RSS_URLS);
        boolean collectionsSame = urls.containsAll(savedUrls) && savedUrls.containsAll(urls);
        Assert.assertTrue(collectionsSame);

    }

    @MediumTest
    public void testFragment(){
        startActivity(new Intent(Intent.ACTION_MAIN), null, null);

        RecycleViewAdapterModule module =new RecycleViewAdapterModule(super.getActivity(), null);
        testedAdapter = ObjectGraph.create(module).get(NewsRecycleViewAdapter.class);
        newsCollector = module.getNewsCollectorStub();

        RssNewsFragment fragment = getTestingFragment();
        startFragment(fragment);

        Bundle savedBundle = new Bundle();
        fragment.onSaveInstanceState(savedBundle);
        ArrayList<String> urls = new ArrayList<>();
        urls.addAll(newsCollector.getCachedUrls());
        checkFragmentSaveRestore(urls, savedBundle);

        fragment.onViewStateRestored(savedBundle);
        assertTrue(fragment.checkedRssUrls.containsAll(urls) && urls.containsAll(fragment.checkedRssUrls));
    }

    private void checkFragmentSaveRestore(ArrayList<String> urls, Bundle savedBundle) {
        ArrayList<String> savedUrls;
        boolean collectionsSame;
        savedUrls = savedBundle.getStringArrayList(RssNewsFragment.RSS_URLS);
        collectionsSame = urls.containsAll(savedUrls) && savedUrls.containsAll(urls);
        Assert.assertTrue(collectionsSame);
    }

    private Fragment startFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
        transaction.add(fragment, "testRssFragment");
        transaction.commit();
        getActivity().getFragmentManager().executePendingTransactions();
        getInstrumentation().waitForIdleSync();
        Fragment frag = getActivity().getFragmentManager().findFragmentByTag("testRssFragment");
        return frag;

    }
    private RssNewsFragment getTestingFragment(){
        RssNewsFragment fragment = new RssNewsFragment() {
            @Override
            protected NewsRecycleViewAdapter getRecycleViewAdapter(ArrayList<String> checkedRssUrls, ProgressBar pbNewsRefresh) {
                return testedAdapter;
            }
        };

        Bundle args = new Bundle();
        ArrayList<String> urls = new ArrayList<>();
        urls.addAll(newsCollector.getCachedUrls());
        args.putStringArrayList(RssNewsFragment.RSS_URLS, urls);
        fragment.setArguments(args);
        return fragment;
    }
}
