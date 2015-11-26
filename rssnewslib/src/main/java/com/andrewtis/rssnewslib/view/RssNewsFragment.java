package com.andrewtis.rssnewslib.view;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.andrewtis.rssnewslib.NewsRecycleViewAdapter;
import com.andrewtis.rssnewslib.R;

import java.util.ArrayList;

public class RssNewsFragment extends Fragment {

    public static final String RSS_URLS = "rssUrls";

    private ArrayList<String> checkedRssUrls;

    public RssNewsFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @param checkedUrls urls, that will be used to check news
     * @return A new instance of fragment RssNewsFragment.
     */

    public static RssNewsFragment newInstance(ArrayList<String> checkedUrls) {
        RssNewsFragment fragment = new RssNewsFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(RSS_URLS, checkedUrls);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            checkedRssUrls = getArguments().getStringArrayList(RSS_URLS);
        }
    }

    public NewsRecycleViewAdapter getRecycleViewAdapter(ArrayList<String> checkedRssUrls, ProgressBar pbNewsRefresh){
        return new NewsRecycleViewAdapter(getActivity(), checkedRssUrls, pbNewsRefresh);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_rss_news, container, false);
        RecyclerView rvNews = (RecyclerView) fragmentView.findViewById(R.id.rv_News);
        ProgressBar pbNewsRefresh = (ProgressBar) fragmentView.findViewById(R.id.pb_NewsRefresh);
        NewsRecycleViewAdapter adapter = getRecycleViewAdapter(checkedRssUrls,pbNewsRefresh);
        rvNews.setAdapter(adapter);
        return fragmentView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(RSS_URLS,checkedRssUrls);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            checkedRssUrls = getArguments().getStringArrayList(RSS_URLS);
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
