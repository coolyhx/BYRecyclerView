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
 *
 * delegateAdapter的基类,继承之后调用只需实现initDelegatesManager方法,
 * 加入这个adapter所需展示的delegate加入到AdapterDelegatesManager就可以使用了
 */
public abstract class BaseDelegateAdapter<T extends DisplayableItem> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> mList;
    private Activity mContext;
    private AdapterDelegatesManager<List<T>> mDelegatesManager;

    private final Object mLock = new Object();

    RecyclerView.AdapterDataObserver mObserver;

    protected LoadMoreContract mLoadMoreContract;
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

    /**
     * 初始化必要的成员变量
     * @param context
     * @param list
     */
    private void init(Activity context, List<T> list) {
        mContext = context;
        mList = list;
        mDelegatesManager = new AdapterDelegatesManager<>();
        initDelegatesManager(mDelegatesManager);
    }

    /**
     * 获取这个adapter的全部数据
     * @return
     */
    public List<T> getData() {
        return mList;
    }

    /**
     * 获取adapter的上下文对象
     * @return
     */
    public Activity getContext() {
        return mContext;
    }

    /**
     * 停止加载更多
     */
    public void stopMore() {
        if (mLoadMoreContract == null)
            throw new NullPointerException("You should invoking setLoadMore() first");
        mLoadMoreContract.stopLoadMore();
    }

    /**
     * 暂停加载更多
     */
    public void pauseMore() {
        if (mLoadMoreContract == null)
            throw new NullPointerException("You should invoking setLoadMore() first");
        mLoadMoreContract.pauseLoadMore();
    }

    /**
     * 继续加载更多
     */
    public void resumeMore() {
        if (mLoadMoreContract == null)
            throw new NullPointerException("You should invoking setLoadMore() first");
        mLoadMoreContract.resumeLoadMore();
    }

    /**
     * 添加头部view
     * @param view
     */
    public void addHeader(ItemView view) {
        if (view == null) throw new NullPointerException("ItemView can't be null");
        headers.add(view);
        notifyItemInserted(footers.size() - 1);
    }

    /**
     * 添加脚部view
     * @param view
     */
    public void addFooter(ItemView view) {
        if (view == null) throw new NullPointerException("ItemView can't be null");
        footers.add(view);
        notifyItemInserted(headers.size() + getItemCount() + footers.size() - 1);
    }

    /**
     * 移除所有的头部view
     */
    public void removeAllHeader() {
        int count = headers.size();
        headers.clear();
        notifyItemRangeRemoved(0, count);
    }

    /**
     * 移除所有的尾部view
     */
    public void removeAllFooter() {
        int count = footers.size();
        footers.clear();
        notifyItemRangeRemoved(headers.size() + getItemCount(), count);
    }

    /**
     * 获取index位置的头部view
     * @param index
     * @return
     */
    public ItemView getHeader(int index) {
        return headers.get(index);
    }

    /**
     * 获取index位置的脚部view
     * @param index
     * @return
     */
    public ItemView getFooter(int index) {
        return footers.get(index);
    }

    /**
     * 获取头部view的数量
     * @return
     */
    public int getHeaderCount() {
        return headers.size();
    }

    /**
     * 获取脚部view的数量
     * @return
     */
    public int getFooterCount() {
        return footers.size();
    }

    /**
     * 移除指定头部view
     * @param view
     */
    public void removeHeader(ItemView view) {
        int position = headers.indexOf(view);
        headers.remove(view);
        notifyItemRemoved(position);
    }

    /**
     * 移除指定脚部view
     * @param view
     */
    public void removeFooter(ItemView view) {
        int position = headers.size() + getItemCount() + footers.indexOf(view);
        footers.remove(view);
        notifyItemRemoved(position);
    }

    /**
     * 获取加载更多的代理类
     * @return
     */
    LoadMoreContract getLoadMoreContract() {
        if (mLoadMoreContract == null) {
            mLoadMoreContract = new LoadMoreHelper(this);
        }
        return mLoadMoreContract;
    }

    /**
     * 设置加载更多的资源view和回调接口
     * @param res
     * @param listener
     * @return
     */
    public View setMore(final int res, final OnLoadMoreListener listener) {
        FrameLayout container = new FrameLayout(getContext());
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(getContext()).inflate(res, container);
        getLoadMoreContract().setMore(container, listener);
        return container;
    }

    /**
     * 设置加载更多的view和回调接口
     * @param view
     * @param listener
     * @return
     */
    public View setMore(final View view, OnLoadMoreListener listener) {
        getLoadMoreContract().setMore(view, listener);
        return view;
    }

    /**
     * 设置没有更多时的资源view
     * @param res
     * @return
     */
    public View setNoMore(final int res) {
        FrameLayout container = new FrameLayout(getContext());
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(getContext()).inflate(res, container);
        getLoadMoreContract().setNoMore(container);
        return container;
    }

    /**
     * 设置没有更多时的view
     * @param view
     * @return
     */
    public View setNoMore(final View view) {
        getLoadMoreContract().setNoMore(view);
        return view;
    }

    /**
     * 设置加载更多异常时的资源view
     * @param res
     * @return
     */
    public View setError(final int res) {
        FrameLayout container = new FrameLayout(getContext());
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(getContext()).inflate(res, container);
        getLoadMoreContract().setErrorMore(container);
        return container;
    }

    /**
     * 设置加载更多异常时的view
     * @param view
     * @return
     */
    public View setError(final View view) {
        getLoadMoreContract().setErrorMore(view);
        return view;
    }

    /**
     * 注册数据变化时的观察者
     * @param observer
     */
    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        if (observer instanceof BYRecyclerView.BYDataObserver) {
            mObserver = observer;
        } else {
            super.registerAdapterDataObserver(observer);
        }
    }

    /**
     * 获取adapter的所有项的数量,包含头部view数量,脚部view数量和所有数据项view
     * @return
     */
    @Override
    public int getItemCount() {
        return mList == null ? footers.size() + headers.size() : footers.size() + headers.size() + mList.size();
    }

    /**
     * 获取adapter的所有数据项数量
     * @return
     */
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    /**
     * 添加单个数据项
     * @param object
     */
    public void add(T object) {
        if (object != null) {
            if (mLoadMoreContract != null) {
                mLoadMoreContract.addData(1);
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

    /**
     * 添加数据项数组
     * @param objects
     */
    public void addAll(T[] objects) {
        if (objects != null) {
            addAll(Arrays.asList(objects));
        }
    }

    /**
     * 添加数据项array
     * @param collection
     */
    public void addAll(Collection<? extends T> collection) {
        if (collection != null && collection.size() > 0) {
            int dataCount = collection.size();
            if (mLoadMoreContract != null) {
                mLoadMoreContract.addData(dataCount);
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

    /**
     * 在adapter的position位置插入一条数据
     *
     * @param object
     * @param position
     */
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

    /**
     * 在adapter的position位置插入一个数组的数据
     * @param objects
     * @param position
     */
    public void insertAll(T[] objects, int position) {
        if (objects != null) {
            insertAll(Arrays.asList(objects), position);
        }
    }

    /**
     * 在adapter的position位置插入一个集合的数据
     * @param objects
     * @param position
     */
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

    /**
     * 移除某个数据
     * @param object
     */
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

    /**
     * 移除position位置的数据
     * @param position
     */
    public void remove(int position) {
        synchronized (mLock) {
            mList.remove(position);
        }
        if (mObserver != null) {
            mObserver.onItemRangeRemoved(position, 1);
        }
        notifyItemRemoved(position);
    }

    /**
     * 清除adapter的所有数据
     */
    public void clear() {
        int count = getItemCount();
        synchronized (mLock) {
            mList.clear();
        }
        if (mLoadMoreContract != null) {
            mLoadMoreContract.clear();
        }
        if (mObserver != null) {
            mObserver.onItemRangeRemoved(0, count);
        }
        notifyItemRangeRemoved(0, count);
    }

    /**
     * 对adapter的数据进行排序
     * @param comparator
     */
    public void sort(Comparator<? super T> comparator) {
        synchronized (mLock) {
            Collections.sort(mList, comparator);
        }
        notifyDataSetChanged();
    }

    /**
     * 初始化delegateManager的delegate方法,提供给子类去实现
     * @param delegatesManager
     */
    public abstract void initDelegatesManager(AdapterDelegatesManager<List<T>> delegatesManager);

    /**
     * 对adapter的修饰对象的封装(headers,footers)
     */
    private class StateViewHolder extends RecyclerView.ViewHolder {

        public StateViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 创建修饰对象的view
     * @param parent
     * @param viewType
     * @return
     */
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
