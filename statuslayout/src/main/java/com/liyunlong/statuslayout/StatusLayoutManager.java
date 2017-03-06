package com.liyunlong.statuslayout;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 有数据、无数据、加载中、加载失败、网络异常、网络不佳页面切换管理器
 *
 * @author liyunlong
 * @date 2017/3/6 14:21
 */
public class StatusLayoutManager implements IStatusLayoutManager {

    private Context context;
    private View contentLayout;
    private View emptyDataLayout; // 无数据布局
    private int emptyDataRetryViewId; // 无数据重试视图的ID
    private View loadingLayout; // 加载中布局
    private View errorLayout; // 加载异常布局
    private int errorRetryViewId; // 加载异常重试视图的ID
    private View netWorkErrorLayout; // 网络异常布局
    private int netWorkErrorRetryViewId; // 网络异常重试视图的ID
    private View netWorkPoorLayout; // 网络不佳布局
    private int netWorkPoorRetryViewId; // 网络不佳重试视图的ID
    private int retryViewId; // 重试视图的ID
    private boolean useOverlapStatusLayoutHelper; // 是否使用覆盖式页面切换辅助类
    private OnRetryListener onRetryListener; // 重试监听器
    private OnStatusLayoutChangedListener statusLayoutChangedListener; // 状态布局改变监听
    private IStatusLayoutHelper helper; // 切换不同视图的帮助类

    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }

    private StatusLayoutManager(Builder builder) {
        this.context = builder.context;
        this.contentLayout = builder.contentLayout;
        this.emptyDataLayout = builder.emptyDataLayout;
        this.emptyDataRetryViewId = builder.emptyDataRetryViewId;
        this.loadingLayout = builder.loadingLayout;
        this.errorLayout = builder.errorLayout;
        this.errorRetryViewId = builder.errorRetryViewId;
        this.netWorkErrorLayout = builder.netWorkErrorLayout;
        this.netWorkErrorRetryViewId = builder.netWorkErrorRetryViewId;
        this.netWorkPoorLayout = builder.netWorkPoorLayout;
        this.netWorkPoorRetryViewId = builder.netWorkPoorRetryViewId;
        this.retryViewId = builder.retryViewId;
        this.onRetryListener = builder.onRetryListener;
        this.statusLayoutChangedListener = builder.statusLayoutChangedListener;
        this.useOverlapStatusLayoutHelper = builder.useOverlapStatusLayoutHelper;
        if (useOverlapStatusLayoutHelper) {
            this.helper = new OverlapStatusLayoutHelper(contentLayout);
        } else {
            this.helper = new ReplaceStatusLayoutHelper(contentLayout);
        }
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public View getCurrentLayout() {
        return helper.getCurrentLayout();
    }

    @Override
    public View getContentLayout() {
        return helper.getContentLayout();
    }

    @Override
    public View getEmptyLayout() {
        return emptyDataLayout;
    }

    @Override
    public View getLoadingLayout() {
        return loadingLayout;
    }

    @Override
    public View getErrorLayout() {
        return errorLayout;
    }

    @Override
    public View getNetworkErrorLayout() {
        return netWorkErrorLayout;
    }

    @Override
    public View getNetworkPoorLayout() {
        return netWorkPoorLayout;
    }

    @Override
    public void showContentLayout() {
        restoreLayout();
    }

    @Override
    public void showEmptyLayout() {
        retryLoad(emptyDataLayout, emptyDataRetryViewId);
        showStatusLayout(emptyDataLayout);
    }

    @Override
    public void showLoadingLayout() {
        showStatusLayout(loadingLayout);
    }

    @Override
    public void showErrorLayout() {
        retryLoad(errorLayout, errorRetryViewId);
        showStatusLayout(errorLayout);
    }

    @Override
    public void showNetworkErrorLayout() {
        retryLoad(netWorkErrorLayout, netWorkErrorRetryViewId);
        showStatusLayout(netWorkErrorLayout);
    }

    @Override
    public void showNetworkPoorLayout() {
        retryLoad(netWorkPoorLayout, netWorkPoorRetryViewId);
        showStatusLayout(netWorkPoorLayout);
    }

    @Override
    public void restoreLayout() {
        if (helper.restoreLayout() && statusLayoutChangedListener != null) {
            statusLayoutChangedListener.onStatusLayoutChanged(getCurrentLayout());
        }
    }

    @Override
    public void showStatusLayout(View view) {
        if (helper.showStatusLayout(view) && statusLayoutChangedListener != null) {
            statusLayoutChangedListener.onStatusLayoutChanged(getCurrentLayout());
        }
    }

    @Override
    public void setStatusLayoutHelper(IStatusLayoutHelper helper) {
        if (helper == null) {
            throw new IllegalArgumentException("The helper con not be null.");
        }
        this.helper = helper;
    }

    @Override
    public void releaseStatusLayouts() {
        emptyDataLayout = null;
        loadingLayout = null;
        errorLayout = null;
        netWorkErrorLayout = null;
        netWorkPoorLayout = null;
    }

    /**
     * 重试加载
     *
     * @param view 重试View所在的根View
     * @param id   重试View的ID
     */
    private void retryLoad(View view, @IdRes int id) {
        if (view == null) {
            return;
        }
        View retryView = view.findViewById(id != 0 ? id : retryViewId);
        if (retryView == null || onRetryListener == null) {
            return;
        }
        retryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRetryListener.onRetry(v);
            }
        });
    }

    public static final class Builder {

        private Context context;
        private View contentLayout; // 内容布局
        private View emptyDataLayout; // 无数据布局
        private int emptyDataRetryViewId; // 无数据重试视图的ID
        private View loadingLayout; // 加载中布局
        private View errorLayout; // 加载异常布局
        private int errorRetryViewId; // 加载异常重试视图的ID
        private View netWorkErrorLayout; // 网络异常布局
        private int netWorkErrorRetryViewId; // 网络异常重试视图的ID
        private View netWorkPoorLayout; // 网络不佳布局
        private int netWorkPoorRetryViewId; // 网络不佳重试视图的ID
        private int retryViewId; // 重试视图的ID
        private boolean useOverlapStatusLayoutHelper; // 是否使用覆盖式页面切换辅助类
        private OnRetryListener onRetryListener; // 重试监听器
        private OnStatusLayoutChangedListener statusLayoutChangedListener; // 状态布局改变监听

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setContentLayout(View contentLayout) {
            this.contentLayout = contentLayout;
            return this;
        }

        public Builder setEmptyLayout(@LayoutRes int noDataViewId) {
            this.emptyDataLayout = inflate(noDataViewId);
            return this;
        }

        public Builder setLoadingLayout(@LayoutRes int loadingLayoutResId) {
            this.loadingLayout = inflate(loadingLayoutResId);
            return this;
        }

        public Builder setErrorLayout(@LayoutRes int errorViewId) {
            this.errorLayout = inflate(errorViewId);
            return this;
        }

        public Builder setNetWorkErrorLayout(@LayoutRes int newWorkErrorId) {
            this.netWorkErrorLayout = inflate(newWorkErrorId);
            return this;
        }

        public Builder setNetWorkPoorLayout(@LayoutRes int newWorkPoorId) {
            this.netWorkPoorLayout = inflate(newWorkPoorId);
            return this;
        }

        public Builder setEmptyRetryViewId(@IdRes int emptyDataRetryViewId) {
            this.emptyDataRetryViewId = emptyDataRetryViewId;
            return this;
        }

        public Builder setErrorRetryViewId(@IdRes int errorRetryViewId) {
            this.errorRetryViewId = errorRetryViewId;
            return this;
        }

        public Builder setNetWorkErrorRetryViewId(@IdRes int netWorkErrorRetryViewId) {
            this.netWorkErrorRetryViewId = netWorkErrorRetryViewId;
            return this;
        }

        public Builder setNetWorkPoorRetryViewId(@IdRes int netWorkPoorRetryViewId) {
            this.netWorkPoorRetryViewId = netWorkPoorRetryViewId;
            return this;
        }

        public Builder setRetryViewId(@IdRes int retryViewId) {
            this.retryViewId = retryViewId;
            return this;
        }

        public Builder setUseOverlapStatusLayoutHelper(boolean useOverlapStatusLayoutHelper) {
            this.useOverlapStatusLayoutHelper = useOverlapStatusLayoutHelper;
            return this;
        }

        public Builder setOnRetryListener(OnRetryListener onRetryListener) {
            this.onRetryListener = onRetryListener;
            return this;
        }

        public Builder setOnStatusLayoutChangedListener(OnStatusLayoutChangedListener onShowHideViewListener) {
            this.statusLayoutChangedListener = onShowHideViewListener;
            return this;
        }

        public StatusLayoutManager build() {
            return new StatusLayoutManager(this);
        }

        private View inflate(@LayoutRes int layoutId) {
            return LayoutInflater.from(context).inflate(layoutId, null);
        }
    }

}
