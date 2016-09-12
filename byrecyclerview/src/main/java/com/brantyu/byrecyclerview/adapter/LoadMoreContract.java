package com.brantyu.byrecyclerview.adapter;

import android.view.View;
/**
 * Created by brantyu on 16/8/23.
 * 加载更多的协议接口
 */
public interface LoadMoreContract {
    void addData(int length);

    void clear();

    void startLoadMore();

    void stopLoadMore();

    void pauseLoadMore();

    void resumeLoadMore();

    void setMore(View view, OnLoadMoreListener listener);

    void setNoMore(View view);

    void setErrorMore(View view);
}