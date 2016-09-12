/*
 * Copyright (c) 2015 Hannes Dorfmann.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.brantyu.demo;


import android.app.Activity;

import com.brantyu.byrecyclerview.adapter.BaseDelegateAdapter;
import com.brantyu.byrecyclerview.adapter.DisplayableItem;
import com.brantyu.byrecyclerview.delegate.AdapterDelegatesManager;
import com.brantyu.demo.adapterdelegates.AdvertisementAdapterDelegate;
import com.brantyu.demo.adapterdelegates.CatAdapterDelegate;
import com.brantyu.demo.adapterdelegates.DogAdapterDelegate;
import com.brantyu.demo.adapterdelegates.GeckoAdapterDelegate;
import com.brantyu.demo.adapterdelegates.SnakeListItemAdapterDelegate;

import java.util.List;


public class MainAdapter extends BaseDelegateAdapter<DisplayableItem> {

    public MainAdapter(Activity context) {
        super(context);
    }

    public MainAdapter(Activity context, List<DisplayableItem> list) {
        super(context, list);
    }

    @Override
    public void initDelegatesManager(AdapterDelegatesManager<List<DisplayableItem>> delegatesManager) {
        delegatesManager.addDelegate(new AdvertisementAdapterDelegate(getContext()));
        delegatesManager.addDelegate(new CatAdapterDelegate(getContext()));
        delegatesManager.addDelegate(new DogAdapterDelegate(getContext()));
        delegatesManager.addDelegate(new GeckoAdapterDelegate(getContext()));
        delegatesManager.addDelegate(new SnakeListItemAdapterDelegate(getContext()));
    }
}
