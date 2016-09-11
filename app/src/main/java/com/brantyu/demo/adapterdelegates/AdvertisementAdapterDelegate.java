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

package com.brantyu.demo.adapterdelegates;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brantyu.byrecyclerview.adapter.DisplayableItem;
import com.brantyu.byrecyclerview.delegate.AdapterDelegate;
import com.brantyu.demo.R;
import com.brantyu.demo.model.Advertisement;

import java.util.List;


/**
 * @author Hannes Dorfmann
 */
public class AdvertisementAdapterDelegate implements AdapterDelegate<List<DisplayableItem>> {

  LayoutInflater inflater;

  public AdvertisementAdapterDelegate(Activity activity) {
    inflater = activity.getLayoutInflater();
  }

  @Override public boolean isForViewType(@NonNull List<DisplayableItem> items, int position) {
    return items.get(position) instanceof Advertisement;
  }

  @NonNull @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
    return new AdvertisementViewHolder(inflater.inflate(R.layout.item_advertisement, parent, false));
  }

  @Override public void onBindViewHolder(@NonNull List<DisplayableItem> items, int position,
                                         @NonNull RecyclerView.ViewHolder holder) {
      // Notihing to bind in this example
  }

  /**
   * The ViewHolder
   */
  static class AdvertisementViewHolder extends RecyclerView.ViewHolder{
    public AdvertisementViewHolder(View itemView) {
      super(itemView);
    }
  }
}
