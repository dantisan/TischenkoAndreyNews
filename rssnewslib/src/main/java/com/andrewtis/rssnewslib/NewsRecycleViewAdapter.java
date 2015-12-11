package com.andrewtis.rssnewslib;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andrewtis.rssnewslib.model.NewsCasher;
import com.andrewtis.rssnewslib.model.NewsCollector;
import com.andrewtis.rssnewslib.model.NewsInfo;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class NewsRecycleViewAdapter extends RecyclerView.Adapter<NewsRecycleViewAdapter.NewsInfoViewHolder> {

    NewsCasher newsCasher = null;
    private Context context;
    View progressView;
    Map<NewsInfo, Boolean> expandedNews = new HashMap<>();

    public NewsRecycleViewAdapter(Context context, List<String> newsRssUrlList, View progressView) {
        this.context = context;
        this.progressView = progressView;
        newsCasher = new NewsCasher(new onNewsRefreshedCallbacks(context), newsRssUrlList);
    }

    @Inject
    NewsRecycleViewAdapter(Context context, NewsCasher collector, View progressView) {
        this.context = context;
        this.progressView = progressView;
        this.newsCasher = collector;
        collector.addNewsRefreshedCallback(new onNewsRefreshedCallbacks(context));
    }

    @Override
    public NewsInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(context).inflate(R.layout.news_info_card, parent, false);
        return new NewsInfoViewHolder(holderView);
    }

    @Override
    public void onBindViewHolder(NewsInfoViewHolder holder, int position) {
        NewsInfo info = newsCasher.getAllNewsInfo().get(position);
        holder.setDescriptionInfo(info, position);
    }

    @Override
    public int getItemCount() {
        return newsCasher.getAllNewsInfo().size();
    }

    public void refreshNews() {
        newsCasher.refreshCachedNews();
    }

    class NewsInfoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDescription;
        private TextView tvTitle;
        private ImageView ivPicture;
        private View cvTitle;

        public NewsInfoViewHolder(View itemView) {
            super(itemView);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_newsDescription);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_newsTitle);
            ivPicture = (ImageView) itemView.findViewById(R.id.iv_newsImage);
            cvTitle = itemView.findViewById(R.id.cv_title);

        }


        public void setDescriptionInfo(final NewsInfo info, final int position) {
            tvDescription.setText(info.getDescription());
            tvTitle.setText(info.getTitle());

            String imgUrl = info.getImgUrl();
            if (!imgUrl.isEmpty())
                Picasso.with(context)
                        .load(imgUrl)
                        .into(ivPicture);
            else Picasso.with(context)
                    .load(R.drawable.no_image_available)
                    .into(ivPicture);

            expandNewInit(info, position);
        }

        private void expandNewInit(final NewsInfo info, final int position) {
            if (expandedNews.get(info) == null)
                expandedNews.put(info, false);
            expandNew(info);

            cvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean shouldShow = expandedNews.get(info);
                    expandedNews.put(info, !shouldShow);
                    expandNew(info);
                    NewsRecycleViewAdapter.this.notifyItemChanged(position);
                }

            });
        }

        public void expandNew(final NewsInfo info) {
            Boolean shouldShow = expandedNews.get(info);

            if (shouldShow)
                tvDescription.setVisibility(View.VISIBLE);
            else
                tvDescription.setVisibility(View.GONE);
            return;
        }

    }

    private class onNewsRefreshedCallbacks implements NewsCollector.NewsRefreshedCallback {
        Handler mainHandler;

        public onNewsRefreshedCallbacks(Context context) {
            mainHandler = new Handler(context.getMainLooper());
        }

        @Override
        public void beforeNewStartRefreshing(String url) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    changeProgressViewVisibility(View.VISIBLE);
                }
            });

        }

        @Override
        public void newRefreshedForUrl(String url, List<NewsInfo> info) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    changeProgressViewVisibility(View.GONE);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public void errorOnRefreshing(final Exception ex, final String refreshingUrl) {

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    changeProgressViewVisibility(View.GONE);
                    String errMessage = context.getResources().getString(R.string.err_url_message) + " " + refreshingUrl;
                    Toast.makeText(context, errMessage, Toast.LENGTH_LONG).show();
                }
            });
        }

        private void changeProgressViewVisibility(final int visibility) {
            if (progressView != null)
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressView.setVisibility(visibility);
                    }
                });
        }
    }

}
