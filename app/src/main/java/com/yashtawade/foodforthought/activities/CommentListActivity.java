package com.yashtawade.foodforthought.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.yashtawade.foodforthought.Http;
import com.yashtawade.foodforthought.R;
import com.yashtawade.foodforthought.adapters.CommentListAdapter;
import com.yashtawade.foodforthought.constants.FFTConstant;
import com.yashtawade.foodforthought.models.Comment;
import com.yashtawade.foodforthought.models.DataParse;
import com.yashtawade.foodforthought.views.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;

public class CommentListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnRefreshListener{
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadMoreListView loadMoreListView;
    private ArrayList<Comment> items;
    //listview adapter
    private CommentListAdapter adapter;
    //get recipe id through intent
    private static final String EXTRA_RECIPE_ID = "fft_recipe_id";
    private String keyword = "recipe/comment";
    private List<Comment> commentList;
    //get comments before lastCommentDate
    private int lastCommentTime;

    BaseHttpRequestCallback mCallback1 = new BaseHttpRequestCallback(){

        @Override
        public void onResponse(Response httpResponse, String response, Headers headers){
            DataParse dp = JSON.parseObject(response, DataParse.class);

            if(dp.getError() == 0){
                commentList = JSON.parseArray(dp.getData(), Comment.class);
                if(commentList.size() > 0){
                    lastCommentTime = commentList.get(commentList.size() - 1).getTimestamp();
                }
            }else{
                commentList = new ArrayList<>();
            }

            if(adapter == null){
                adapter = new CommentListAdapter(CommentListActivity.this, commentList);
                loadMoreListView.setAdapter(adapter);
            }else{
                adapter.onDateChange(commentList);
            }


        }
    };

    BaseHttpRequestCallback mCallback2 = new BaseHttpRequestCallback(){

        @Override
        public void onResponse(Response httpResponse, String response, Headers headers){
            DataParse dp = JSON.parseObject(response, DataParse.class);

            if(dp.getError() == 0){
                List<Comment> list = JSON.parseArray(dp.getData(), Comment.class);
                if(list.size() == 0){
                    //don't request when all is loaded
                    loadMoreListView.isLoadedAll = true;
                }else{
                    commentList.addAll(list);
                    lastCommentTime = commentList.get(commentList.size() - 1).getTimestamp();
                }

            }

            if(adapter == null){
                adapter = new CommentListAdapter(CommentListActivity.this, commentList);
                loadMoreListView.setAdapter(adapter);
            }else{
                adapter.onDateChange(commentList);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);

        initView();
        initEvent();
        initData();
    }

    /**
     * Initial data
     */
    private void initData() {
        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, 0);
        String url = FFTConstant.APP_BASE_URL + keyword + "?rid=" + recipeId;
        Http httpRequest = new Http();
        httpRequest.get(url, mCallback1);
    }

    /**
     * Set event for widget
     */
    private void initEvent() {
        swipeRefreshLayout.setOnRefreshListener(this);

        //Change color for loading icon
        swipeRefreshLayout.setColorSchemeResources(
                R.color.firstColor,
                R.color.secondColor,
                R.color.thirdColor,
                R.color.forthColor);
    }

    /**
     * Initial view
     */
    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_google_and_loadmore_refresh_layout);
        loadMoreListView = (LoadMoreListView) findViewById(R.id.google_and_loadmore_refresh_listview);
        loadMoreListView.setOnRefreshListener(this);
    }

    /**
     * Invoke pull down refreshing event
     */
    @Override
    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //load new data
                setRefreshData();
                //stop refreshing
                swipeRefreshLayout.setRefreshing(false);
                //reset after refresh
                loadMoreListView.isLoadedAll = false;
            }
        }, 1000);
    }

    private void setRefreshData() {
        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, 0);
        String url = FFTConstant.APP_BASE_URL + keyword + "?rid=" + recipeId;
        Http httpRequest = new Http();
        httpRequest.get(url, mCallback1);
    }

    /**
     * Invoke pull up loading more event
     */
    @Override
    public void onLoadingMore() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //loading more data from database
                setLoadingMoreData();
                //notify data changed and append to the list
                adapter.notifyDataSetChanged();
                //stop loading
                loadMoreListView.loadMoreComplete();
            }
        }, 1000);
    }

    private void setLoadingMoreData(){
        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, 0);
        String url = FFTConstant.APP_BASE_URL + keyword + "?rid=" + recipeId + "&lastCommentTime=" + lastCommentTime;
        Http httpRequest = new Http();
        httpRequest.get(url, mCallback2);
    }

    public static Intent newIntent(Context packageContext, int recipeId) {
        Intent i = new Intent(packageContext, CommentListActivity.class);
        i.putExtra(EXTRA_RECIPE_ID, recipeId);
        return i;
    }
}
