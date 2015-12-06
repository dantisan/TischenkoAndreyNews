package com.andrewtis.rssnewslib;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andrewtis.rssnewslib.model.NewsCollector;
import com.andrewtis.rssnewslib.model.NewsInfo;

import java.util.List;

import javax.inject.Inject;

public class NewsRecycleViewAdapter extends RecyclerView.Adapter<NewsRecycleViewAdapter.NewsInfoViewHolder> {

    NewsCollector newsCollector = null;
    private Context context;
    View progressView;


    public NewsRecycleViewAdapter(Context context, List<String> newsRssUrlList, View progressView) {
        this.context = context;
        this.progressView = progressView;
        newsCollector = new NewsCollector(new onNewsRefreshedCallbacks(context), newsRssUrlList);
    }

    @Inject
    NewsRecycleViewAdapter(Context context, NewsCollector collector, View progressView) {
        this.context = context;
        this.progressView = progressView;
        this.newsCollector = collector;
        collector.setNewsRefreshedCallback(new onNewsRefreshedCallbacks(context));
    }

    @Override
    public NewsInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(context).inflate(R.layout.news_info_card, parent, false);
        return new NewsInfoViewHolder(holderView);
    }

    @Override
    public void onBindViewHolder(NewsInfoViewHolder holder, int position) {
        NewsInfo info = newsCollector.getAllNewsInfo().get(position);
        holder.setDescriptionInfo(info);
    }

    @Override
    public int getItemCount() {
        return newsCollector.getAllNewsInfo().size();
    }

    public void refreshNews() {
        newsCollector.refreshCachedNews();
    }

    static class NewsInfoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDescription;
        private TextView tvTitle;
        //TODO Реализовать асинхронную подгрузку картинок
        //private ImageView ivPicture;

        public NewsInfoViewHolder(View itemView) {
            super(itemView);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_newsDescription);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_newsTitle);

        }

        public void setDescriptionInfo(NewsInfo info) {
            tvDescription.setText(info.getDescription());
            tvTitle.setText(info.getTitle());
            //loadImageAsync();
        }

    }

    private class onNewsRefreshedCallbacks implements NewsCollector.NewsRefreshedCallback {
        Handler mainHandler;

        public onNewsRefreshedCallbacks(Context context) {
            mainHandler = new Handler(context.getMainLooper());
        }

        @Override
        public void beforeNewStartRefreshing(String url) {

            changeProgressViewVisibility(View.VISIBLE);
        }

        @Override
        public void newRefreshedForUrl(String url) {
            changeProgressViewVisibility(View.GONE);
            notifyDataSetChanged();
        }

        @Override
        public void errorOnRefreshing(final Exception ex, final String refreshingUrl) {
            changeProgressViewVisibility(View.GONE);
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    String errMessage = context.getResources().getString(R.string.err_url_message) + " " + refreshingUrl;
                    Toast.makeText(context, errMessage, Toast.LENGTH_LONG).show();
                }
            });
        }

        private void changeProgressViewVisibility(final int visibility) {
            if(progressView != null)
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressView.setVisibility(visibility);
                    }
                });
        }
    }

}
