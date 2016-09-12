package com.brantyu.byrecyclerview.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by brantyu on 16/8/23.
 * 各种修饰项(headers,footers)的接口
 */
public interface ItemView {
    View onCreateView(ViewGroup parent);

    void onBindView(View headerView);
}