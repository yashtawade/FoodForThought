package com.yashtawade.foodforthought.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.yashtawade.foodforthought.R;

public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {


    private static final String TAG = "RefreshListView";

    private boolean isScrollToBottom;//determine if scrolled to the bottom
    private View footerView;
    private int footerViewHeight;
    private boolean isLoadingMore = false; //determine if is loading more
    public boolean isLoadedAll = false;

    /**
     * Listening listView
     */
    private OnRefreshListener mOnRefreshListener;


    public LoadMoreListView(Context context) {
        super(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFooterView();
        this.setOnScrollListener(this);
    }


    /**
     * Initial foot view
     */
    private void initFooterView() {
        footerView = View.inflate(getContext(), R.layout.footer_layout, null);
        //set（0，0）to measure footView's width and height
        footerView.measure(0, 0);
        footerViewHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        this.addFooterView(footerView);
    }

    /**
     * Listening to listview state change
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
            if (isScrollToBottom && !isLoadingMore && !isLoadedAll) {
                isLoadingMore = true;
                footerView.setPadding(0, 0, 0, 0);
                this.setSelection(this.getCount());

                if (mOnRefreshListener != null && !isLoadedAll) {
                    mOnRefreshListener.onLoadingMore();
                }
            }
        }
    }

    /**
     * Listening to listview scroll state
     *
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (getLastVisiblePosition() == (totalItemCount - 1)) {
            isScrollToBottom = true;
        } else {
            isScrollToBottom = false;
        }

    }

    /**
     * Set listener
     *
     * @param listener
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }


    /**
     * Hide footerView when finish loading
     */
    public void loadMoreComplete() {
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        isLoadingMore = false;
    }

    /**
     * Listening to listview refresh state
     */
    public interface OnRefreshListener {
        /**
         * pull up to load more
         */
        void onLoadingMore();
    }
}

