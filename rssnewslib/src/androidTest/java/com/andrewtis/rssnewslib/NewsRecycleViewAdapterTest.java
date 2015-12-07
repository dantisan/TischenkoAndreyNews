package com.andrewtis.rssnewslib;


import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andrewtis.rssnewslib.model.NewsInfo;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

public class NewsRecycleViewAdapterTest extends NewsRecViewAdapterProgressViewTest {

    @Override
    protected RecycleViewAdapterModule initDIModule(){
        RecycleViewAdapterModule module =new RecycleViewAdapterModule(mContext, new View(mContext));
        return module;
    }

    @SmallTest
    @Override
    public void testProgressViews(){
        getInstrumentation().waitForIdleSync();
        Assert.assertEquals(testedAdapter.progressView.getVisibility(),View.GONE);
    }

    @SmallTest
    public void testItemsCount(){
        Assert.assertEquals(newsCollector.getNewsCount(), testedAdapter.getItemCount());
    }

    @SmallTest
    public void testViews(){
        final ViewGroup stub = new LinearLayout(mContext);
        final List<NewsInfo> newsInfoList = newsCollector.getAllNewsInfo();
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                checkViewsContent(stub, newsInfoList);
            }
        });
        getInstrumentation().waitForIdleSync();

    }

    private void checkViewsContent(ViewGroup stub, List<NewsInfo> newsInfoList) {
        for (int i = 0; i < testedAdapter.getItemCount(); i++) {
            NewsRecycleViewAdapter.NewsInfoViewHolder holder = testedAdapter.createViewHolder(stub,0);
            testedAdapter.bindViewHolder(holder,i);
            NewsInfo itemInfo = newsInfoList.get(i);

            TextView tvDescription = (TextView)holder.itemView.findViewById(R.id.tv_newsDescription);
            Assert.assertEquals(itemInfo.getDescription(), tvDescription.getText().toString());
            TextView tvTitle = (TextView)holder.itemView.findViewById(R.id.tv_newsTitle);
            Assert.assertEquals(itemInfo.getTitle(), tvTitle.getText().toString());
        }
    }

    @SmallTest
    public void testConstructor(){
        View progressView = new View(mContext);
        NewsRecycleViewAdapter adapter = new NewsRecycleViewAdapter(mContext, new ArrayList<String>(), progressView);
        Assert.assertEquals(View.VISIBLE, progressView.getVisibility());
        Assert.assertEquals(0, adapter.getItemCount());
    }
}
