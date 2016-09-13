package com.brantyu.byrecyclerview.adapter;

import android.view.View;
/**
 * Created by brantyu on 16/8/23.
 * 加载更多的协议接口
 */
public interface LoadMoreContract {
    /**
     * 添加数据方法,用来更新LoadMoreHelper的状态
     * @param length
     */
    void addData(int length);

    /**
     * 清除所有数据
     */
    void clear();

    /**
     * 开始加载更多
     */
    void startLoadMore();

    /**
     * 停止加载更多
     */
    void stopLoadMore();

    /**
     * 暂停加载更多
     */
    void pauseLoadMore();

    /**
     * 继续加载更多
     */
    void resumeLoadMore();

    /**
     * 设置加载更多的view和回调接口
     * @param view
     * @param listener
     */
    void setMore(View view, OnLoadMoreListener listener);

    /**
     * 设置没有更多时的view
     * @param view
     */
    void setNoMore(View view);

    /**
     * 设置加载更多异常时的view
     * @param view
     */
    void setErrorMore(View view);
}