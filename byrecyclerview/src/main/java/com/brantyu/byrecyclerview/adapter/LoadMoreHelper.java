package com.brantyu.byrecyclerview.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by brantyu on 16/8/23.
 * 加载更多的帮助类,用来维护各种加载更多时遇到的状态.
 */
public class LoadMoreHelper implements LoadMoreContract {
    private BaseDelegateAdapter mAdapter;
    private EventFooter mFooter;

    private OnLoadMoreListener mOnLoadMoreListener;

    private int mStatus = STATUS_INITIAL;
    private static final int STATUS_INITIAL = 1;
    private static final int STATUS_MORE = 1 << 1;
    private static final int STATUS_NO_MORE = 1 << 2;
    private static final int STATUS_LOADING_MORE = 1 << 3;
    private static final int STATUS_ERROR = 1 << 4;

    public LoadMoreHelper(BaseDelegateAdapter adapter) {
        mAdapter = adapter;
        mFooter = new EventFooter(this);
        adapter.addFooter(mFooter);
    }

    public void onStartLoadMore() {
        if (mStatus != STATUS_LOADING_MORE && mOnLoadMoreListener != null) {
            mOnLoadMoreListener.onLoadMore();
        }
    }

    public void onResumeLoadMore() {
        if (mStatus != STATUS_LOADING_MORE && mOnLoadMoreListener != null) {
            mOnLoadMoreListener.onReloadMore();
        }
    }

    public void onErrorViewShowed() {
        resumeLoadMore();
    }

    @Override
    public void addData(int length) {
        if (mStatus == STATUS_MORE || mStatus == STATUS_LOADING_MORE) {
            if (length == 0) {
                mFooter.showNoMore();
                mStatus = STATUS_NO_MORE;
            } else {
                mFooter.showMore();
                mStatus = STATUS_MORE;
            }
        }
    }

    @Override
    public void clear() {
        if (mOnLoadMoreListener == null) {
            mStatus = STATUS_INITIAL;
            mFooter.hide();
        } else {
            mStatus = STATUS_MORE;
            mFooter.showMore();
        }


    }

    @Override
    public void startLoadMore() {
        mFooter.showMore();
        onStartLoadMore();
        mStatus = STATUS_LOADING_MORE;
    }

    @Override
    public void stopLoadMore() {
        mFooter.showNoMore();
        mStatus = STATUS_NO_MORE;
    }

    @Override
    public void pauseLoadMore() {
        mFooter.showError();
        mStatus = STATUS_ERROR;
    }

    @Override
    public void resumeLoadMore() {
        mFooter.showMore();
        onResumeLoadMore();
        mStatus = STATUS_LOADING_MORE;
    }

    @Override
    public void setMore(View view, OnLoadMoreListener listener) {
        mFooter.setMoreView(view);
        this.mOnLoadMoreListener = listener;
        mStatus = STATUS_MORE;
    }

    @Override
    public void setNoMore(View view) {
        mFooter.setNoMoreView(view);
    }

    @Override
    public void setErrorMore(View view) {
        mFooter.setErrorView(view);
    }


    private class EventFooter implements ItemView {
        private FrameLayout container;
        private LoadMoreHelper mLoadMoreHelper;
        private View moreView;
        private View noMoreView;
        private View errorView;

        public EventFooter(LoadMoreHelper loadMoreHelper) {
            mLoadMoreHelper = loadMoreHelper;
            container = new FrameLayout(mAdapter.getContext());
            container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        @Override
        public View onCreateView(ViewGroup parent) {
            return container;
        }

        @Override
        public void onBindView(View headerView) {
            switch (mLoadMoreHelper.mStatus) {
                case STATUS_INITIAL:
                    hide();
                    break;
                case STATUS_LOADING_MORE:
                case STATUS_MORE:
                    mLoadMoreHelper.startLoadMore();
                    break;
                case STATUS_ERROR:
                    showError();
                    break;
                case STATUS_NO_MORE:
                    showNoMore();
                    break;
            }
        }

        public void showError() {
            hide();
            if (errorView != null) {
                errorView.setVisibility(View.VISIBLE);
                errorView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mLoadMoreHelper.resumeLoadMore();
                    }
                });
            }
        }

        public void showMore() {
            hide();
            if (moreView != null) {
                moreView.setVisibility(View.VISIBLE);
            }
        }

        public void showNoMore() {
            hide();
            if (noMoreView != null) {
                noMoreView.setVisibility(View.VISIBLE);
            }
        }

        public void hide() {
            if (moreView != null) {
                moreView.setVisibility(View.GONE);
            }
            if (noMoreView != null) {
                noMoreView.setVisibility(View.GONE);
            }
            if (errorView != null) {
                errorView.setVisibility(View.GONE);
            }
        }

        public void setMoreView(View moreView) {
            this.moreView = moreView;
            container.addView(moreView);
        }

        public void setNoMoreView(View noMoreView) {
            this.noMoreView = noMoreView;
            container.addView(noMoreView);
        }

        public void setErrorView(View errorView) {
            this.errorView = errorView;
            container.addView(errorView);
        }
    }

}
