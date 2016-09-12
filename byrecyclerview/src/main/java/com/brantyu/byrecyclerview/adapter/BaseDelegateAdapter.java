package com.brantyu.byrecyclerview.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.brantyu.byrecyclerview.BYRecyclerView;
import com.brantyu.byrecyclerview.delegate.AdapterDelegatesManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by brantyu on 16/8/23.
 */
public abstract class BaseDelegateAdapter<T extends DisplayableItem> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> mList;
    private Activity mContext;
    private AdapterDelegatesManager<List<T>> mDelegatesManager;

    private final Object mLock = new Object();

    RecyclerView.AdapterDataObserver mObserver;

    protected LoadMoreContract mEventDelegate;
    protected ArrayList<ItemView> headers = new ArrayList<>();
    protected ArrayList<ItemView> footers = new ArrayList<>();

    protected OnItemClickListener mItemClickListener;
    protected OnItemLongClickListener mItemLongClickListener;


    public BaseDelegateAdapter(Activity context) {
        init(context, new ArrayList<T>());
    }

    public BaseDelegateAdapter(Activity context, T[] list) {
        init(context, Arrays.asList(list));
    }

    public BaseDelegateAdapter(Activity context, List<T> list) {
        init(context, list);
    }

    private void init(Activity context, List<T> list) {
        mContext = context;
        mList = list;
        mDelegatesManager = new AdapterDelegatesManager<>();
        initDelegatesManager(mDelegatesManager);
    }

    public List<T> getData() {
        return mList;
    }

    public Activity getContext() {
        return mContext;
    }

    public void stopMore() {
        if (mEventDelegate == null)
            throw new NullPointerException("You should invoking setLoadMore() first");
        mEventDelegate.stopLoadMore();
    }

    public void pauseMore() {
        if (mEventDelegate == null)
            throw new NullPointerException("You should invoking setLoadMore() first");
        mEventDelegate.pauseLoadMore();
    }

    public void resumeMore() {
        if (mEventDelegate == null)
            throw new NullPointerException("You should invoking setLoadMore() first");
        mEventDelegate.resumeLoadMore();
    }


    public void addHeader(ItemView view) {
        if (view == null) throw new NullPointerException("ItemView can't be null");
        headers.add(view);
        notifyItemInserted(footers.size() - 1);
    }

    public void addFooter(ItemView view) {
        if (view == null) throw new NullPointerException("ItemView can't be null");
        footers.add(view);
        notifyItemInserted(headers.size() + getItemCount() + footers.size() - 1);
    }

    public void removeAllHeader() {
        int count = headers.size();
        headers.clear();
        notifyItemRangeRemoved(0, count);
    }

    public void removeAllFooter() {
        int count = footers.size();
        footers.clear();
        notifyItemRangeRemoved(headers.size() + getItemCount(), count);
    }

    public ItemView getHeader(int index) {
        return headers.get(index);
    }

    public ItemView getFooter(int index) {
        return footers.get(index);
    }

    public int getHeaderCount() {
        return headers.size();
    }

    public int getFooterCount() {
        return footers.size();
    }

    public void removeHeader(ItemView view) {
        int position = headers.indexOf(view);
        headers.remove(view);
        notifyItemRemoved(position);
    }

    public void removeFooter(ItemView view) {
        int position = headers.size() + getItemCount() + footers.indexOf(view);
        footers.remove(view);
        notifyItemRemoved(position);
    }


    LoadMoreContract getEventDelegate() {
        if (mEventDelegate == null) {
            mEventDelegate = new LoadMoreHelper(this);
        }
        return mEventDelegate;
    }

    public View setMore(final int res, final OnLoadMoreListener listener) {
        FrameLayout container = new FrameLayout(getContext());
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(getContext()).inflate(res, container);
        getEventDelegate().setMore(container, listener);
        return container;
    }

    public View setMore(final View view, OnLoadMoreListener listener) {
        getEventDelegate().setMore(view, listener);
        return view;
    }

    public View setNoMore(final int res) {
        FrameLayout container = new FrameLayout(getContext());
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(getContext()).inflate(res, container);
        getEventDelegate().setNoMore(container);
        return container;
    }

    public View setNoMore(final View view) {
        getEventDelegate().setNoMore(view);
        return view;
    }

    public View setError(final int res) {
        FrameLayout container = new FrameLayout(getContext());
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(getContext()).inflate(res, container);
        getEventDelegate().setErrorMore(container);
        return container;
    }

    public View setError(final View view) {
        getEventDelegate().setErrorMore(view);
        return view;
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        if (observer instanceof BYRecyclerView.BYDataObserver) {
            mObserver = observer;
        } else {
            super.registerAdapterDataObserver(observer);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? footers.size() + headers.size() : footers.size() + headers.size() + mList.size();
    }

    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    public void add(T object) {
        if (object != null) {
            if (mEventDelegate != null) {
                mEventDelegate.addData(1);
            }
            synchronized (mLock) {
                mList.add(object);
            }
            if (mObserver != null) {
                mObserver.onItemRangeInserted(getItemCount() + 1, 1);
            }
            notifyItemInserted(getItemCount() + 1);
        }
    }

    public void addAll(T[] objects) {
        if (objects != null) {
            addAll(Arrays.asList(objects));
        }
    }

    public void addAll(Collection<? extends T> collection) {
        if (collection != null && collection.size() > 0) {
            int dataCount = collection.size();
            if (mEventDelegate != null) {
                mEventDelegate.addData(dataCount);
            }
            synchronized (mLock) {
                mList.addAll(collection);
            }
            if (mObserver != null) {
                mObserver.onItemRangeInserted(getItemCount() - dataCount + 1, dataCount);
            }
            notifyItemRangeInserted(getItemCount() - dataCount + 1, dataCount);
        }
    }

    public void insert(T object, int position) {
        if (object != null) {
            synchronized (mLock) {
                mList.add(position, object);
            }
            if (mObserver != null) {
                mObserver.onItemRangeInserted(position + 1, 1);
            }
            notifyItemInserted(position + 1);
        }
    }

    public void insertAll(T[] objects, int position) {
        if (objects != null) {
            insertAll(Arrays.asList(objects), position);
        }
    }

    public void insertAll(Collection<? extends T> objects, int position) {
        if (objects != null && objects.size() > 0) {
            synchronized (mLock) {
                mList.addAll(position, objects);
            }
            int dataCount = objects.size();
            if (mObserver != null) {
                mObserver.onItemRangeInserted(getItemCount() - dataCount + 1, dataCount);
            }
            notifyItemRangeInserted(getItemCount() - dataCount + 1, dataCount);
        }
    }

    public void remove(T object) {
        int position = mList.indexOf(object);
        synchronized (mLock) {
            if (mList.remove(object)) {
                if (mObserver != null) {
                    mObserver.onItemRangeRemoved(position, 1);
                }
                notifyItemRemoved(position);
            }
        }
    }

    public void remove(int position) {
        synchronized (mLock) {
            mList.remove(position);
        }
        if (mObserver != null) {
            mObserver.onItemRangeRemoved(position, 1);
        }
        notifyItemRemoved(position);
    }

    public void clear() {
        int count = getItemCount();
        synchronized (mLock) {
            mList.clear();
        }
        if (mEventDelegate != null) {
            mEventDelegate.clear();
        }
        if (mObserver != null) {
            mObserver.onItemRangeRemoved(0, count);
        }
        notifyItemRangeRemoved(0, count);
    }

    public void sort(Comparator<? super T> comparator) {
        synchronized (mLock) {
            Collections.sort(mList, comparator);
        }
        notifyDataSetChanged();
    }

    public abstract void initDelegatesManager(AdapterDelegatesManager<List<T>> delegatesManager);

    private class StateViewHolder extends RecyclerView.ViewHolder {

        public StateViewHolder(View itemView) {
            super(itemView);
        }
    }

    private View createSpViewByType(ViewGroup parent, int viewType) {
        for (ItemView headerView : headers) {
            if (headerView.hashCode() == viewType) {
                View view = headerView.onCreateView(parent);
                StaggeredGridLayoutManager.LayoutParams layoutParams;
                if (view.getLayoutParams() != null)
                    layoutParams = new StaggeredGridLayoutManager.LayoutParams(view.getLayoutParams());
                else
                    layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setFullSpan(true);
                view.setLayoutParams(layoutParams);
                return view;
            }
        }
        for (ItemView footerView : footers) {
            if (footerView.hashCode() == viewType) {
                View view = footerView.onCreateView(parent);
                StaggeredGridLayoutManager.LayoutParams layoutParams;
                if (view.getLayoutParams() != null)
                    layoutParams = new StaggeredGridLayoutManager.LayoutParams(view.getLayoutParams());
                else
                    layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setFullSpan(true);
                view.setLayoutParams(layoutParams);
                return view;
            }
        }
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = createSpViewByType(parent, viewType);
        if (view != null) {
            return new StateViewHolder(view);
        }

        final RecyclerView.ViewHolder viewHolder = mDelegatesManager.onCreateViewHolder(parent, viewType);

        //itemView 的点击事件
        if (mItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(viewHolder.getAdapterPosition() - headers.size());
                }
            });
        }

        if (mItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mItemLongClickListener.onItemLongClick(viewHolder.getAdapterPosition() - headers.size());
                }
            });
        }
        return viewHolder;
    }


    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setId(position);
        if (headers.size() != 0 && position < headers.size()) {
            headers.get(position).onBindView(holder.itemView);
            return;
        }

        int i = position - headers.size() - mList.size();
        if (footers.size() != 0 && i >= 0) {
            footers.get(i).onBindView(holder.itemView);
            return;
        }
        mDelegatesManager.onBindViewHolder(mList, position, holder);
    }

    @Override
    public final int getItemViewType(int position) {
        if (headers.size() != 0) {
            if (position < headers.size()) return headers.get(position).hashCode();
        }
        if (footers.size() != 0) {
            int i = position - headers.size() - mList.size();
            if (i >= 0) {
                return footers.get(i).hashCode();
            }
        }
        return mDelegatesManager.getItemViewType(mList, position);
    }
}
