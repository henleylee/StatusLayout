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
    private View emptyLayout; // 无数据布局
    private int emptyRetryViewId; // 无数据重试视图的ID
    private View loadingLayout; // 加载中布局
    private View errorLayout; // 加载异常布局
    private int errorRetryViewId; // 加载异常重试视图的ID
    private View netWorkErrorLayout; // 网络异常布局
    private int netWorkErrorRetryViewId; // 网络异常重试视图的ID
    private View netWorkPoorLayout; // 网络不佳布局
    private int netWorkPoorRetryViewId; // 网络不佳重试视图的ID
    private int retryViewId; // 重试视图的ID
    private boolean useOverlapStatusLayoutHelper; // 是否使用覆盖式页面切换辅助类
    private OnRetryActionListener onRetryActionListener; // 重试监听器
    private OnStatusLayoutChangedListener statusLayoutChangedListener; // 状态布局改变监听
    private IStatusLayoutHelper helper; // 切换不同视图的帮助类

    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    private StatusLayoutManager(Builder builder) {
        this.context = builder.context;
        this.contentLayout = builder.contentLayout;
        this.emptyLayout = builder.emptyLayout;
        this.emptyRetryViewId = builder.emptyRetryViewId;
        this.loadingLayout = builder.loadingLayout;
        this.errorLayout = builder.errorLayout;
        this.errorRetryViewId = builder.errorRetryViewId;
        this.netWorkErrorLayout = builder.netWorkErrorLayout;
        this.netWorkErrorRetryViewId = builder.netWorkErrorRetryViewId;
        this.netWorkPoorLayout = builder.netWorkPoorLayout;
        this.netWorkPoorRetryViewId = builder.netWorkPoorRetryViewId;
        this.retryViewId = builder.retryViewId;
        this.onRetryActionListener = builder.onRetryActionListener;
        this.statusLayoutChangedListener = builder.statusLayoutChangedListener;
        this.useOverlapStatusLayoutHelper = builder.useOverlapStatusLayoutHelper;

        initStatusLayoutHelper(); // 初始化StatusLayoutHelper
    }

    private void initStatusLayoutHelper() {
        if (useOverlapStatusLayoutHelper) { // 判断是否使用覆盖式页面切换辅助类
            this.helper = new OverlapStatusLayoutHelper(contentLayout); // 覆盖模式
        } else {
            this.helper = new ReplaceStatusLayoutHelper(contentLayout); // 替换模式
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
        return emptyLayout;
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
        retryLoad(emptyLayout, emptyRetryViewId);
        showStatusLayout(emptyLayout);
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
        emptyLayout = null;
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
        if (retryView == null || onRetryActionListener == null) {
            return;
        }
        retryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRetryActionListener.onRetryAction(v);
            }
        });
    }

    public static final class Builder {

        private Context context;
        private View contentLayout; // 内容布局
        private View emptyLayout; // 无数据布局
        private int emptyRetryViewId; // 无数据重试视图的ID
        private View loadingLayout; // 加载中布局
        private View errorLayout; // 加载异常布局
        private int errorRetryViewId; // 加载异常重试视图的ID
        private View netWorkErrorLayout; // 网络异常布局
        private int netWorkErrorRetryViewId; // 网络异常重试视图的ID
        private View netWorkPoorLayout; // 网络不佳布局
        private int netWorkPoorRetryViewId; // 网络不佳重试视图的ID
        private int retryViewId; // 重试视图的ID
        private boolean useOverlapStatusLayoutHelper; // 是否使用覆盖式页面切换辅助类
        private OnRetryActionListener onRetryActionListener; // 重试监听器
        private OnStatusLayoutChangedListener statusLayoutChangedListener; // 状态布局改变监听
        private LayoutInflater inflater;

        Builder(Context context) {
            this.context = context;
        }

        Builder(StatusLayoutManager builder) {
            this.context = builder.context;
            this.contentLayout = builder.contentLayout;
            this.emptyLayout = builder.emptyLayout;
            this.emptyRetryViewId = builder.emptyRetryViewId;
            this.loadingLayout = builder.loadingLayout;
            this.errorLayout = builder.errorLayout;
            this.errorRetryViewId = builder.errorRetryViewId;
            this.netWorkErrorLayout = builder.netWorkErrorLayout;
            this.netWorkErrorRetryViewId = builder.netWorkErrorRetryViewId;
            this.netWorkPoorLayout = builder.netWorkPoorLayout;
            this.netWorkPoorRetryViewId = builder.netWorkPoorRetryViewId;
            this.retryViewId = builder.retryViewId;
            this.onRetryActionListener = builder.onRetryActionListener;
            this.statusLayoutChangedListener = builder.statusLayoutChangedListener;
            this.useOverlapStatusLayoutHelper = builder.useOverlapStatusLayoutHelper;
        }

        /**
         * 设置内容布局
         *
         * @param contentLayout 内容布局
         */
        public Builder setContentLayout(View contentLayout) {
            this.contentLayout = contentLayout;
            return this;
        }

        /**
         * 设置内容布局资源ID
         *
         * @param contentLayoutResId 内容布局资源ID
         */
        public Builder setContentLayout(@LayoutRes int contentLayoutResId) {
            this.contentLayout = inflate(contentLayoutResId);
            return this;
        }

        /**
         * 设置无数据布局
         *
         * @param emptyLayout 无数据布局
         */
        public Builder setEmptyLayout(View emptyLayout) {
            this.emptyLayout = emptyLayout;
            return this;
        }

        /**
         * 设置无数据布局资源ID
         *
         * @param emptyLayoutResId 无数据布局资源ID
         */
        public Builder setEmptyLayout(@LayoutRes int emptyLayoutResId) {
            this.emptyLayout = inflate(emptyLayoutResId);
            return this;
        }

        /**
         * 设置加载中布局
         *
         * @param loadingLayout 加载中布局
         */
        public Builder setLoadingLayout(View loadingLayout) {
            this.loadingLayout = loadingLayout;
            return this;
        }

        /**
         * 设置加载中布局资源ID
         *
         * @param loadingLayoutResId 加载中布局资源ID
         */
        public Builder setLoadingLayout(@LayoutRes int loadingLayoutResId) {
            this.loadingLayout = inflate(loadingLayoutResId);
            return this;
        }

        /**
         * 设置加载异常布局
         *
         * @param errorLayout 加载异常布局
         */
        public Builder setErrorLayout(View errorLayout) {
            this.errorLayout = errorLayout;
            return this;
        }

        /**
         * 设置加载异常布局资源ID
         *
         * @param errorLayoutResId 加载异常布局资源ID
         */
        public Builder setErrorLayout(@LayoutRes int errorLayoutResId) {
            this.errorLayout = inflate(errorLayoutResId);
            return this;
        }

        /**
         * 设置网络异常布局
         *
         * @param netWorkErrorLayout 网络异常布局
         */
        public Builder setNetWorkErrorLayout(View netWorkErrorLayout) {
            this.netWorkErrorLayout = netWorkErrorLayout;
            return this;
        }

        /**
         * 设置网络异常布局资源ID
         *
         * @param newWorkErrorLayoutResId 网络异常布局资源ID
         */
        public Builder setNetWorkErrorLayout(@LayoutRes int newWorkErrorLayoutResId) {
            this.netWorkErrorLayout = inflate(newWorkErrorLayoutResId);
            return this;
        }

        /**
         * 设置网络不佳布局
         *
         * @param netWorkPoorLayout 网络不佳布局
         */
        public Builder setNetWorkPoorLayout(View netWorkPoorLayout) {
            this.netWorkPoorLayout = netWorkPoorLayout;
            return this;
        }

        /**
         * 设置网络不佳布局资源ID
         *
         * @param newWorkPoorLayoutResId 网络不佳布局资源ID
         */
        public Builder setNetWorkPoorLayout(@LayoutRes int newWorkPoorLayoutResId) {
            this.netWorkPoorLayout = inflate(newWorkPoorLayoutResId);
            return this;
        }

        /**
         * 设置无数据布局重试操作控件ID
         *
         * @param emptyRetryViewId 无数据布局重试操作控件ID
         */
        public Builder setEmptyRetryViewId(@IdRes int emptyRetryViewId) {
            this.emptyRetryViewId = emptyRetryViewId;
            return this;
        }

        /**
         * 设置加载异常布局重试操作控件ID
         *
         * @param errorRetryViewId 加载异常布局重试操作控件ID
         */
        public Builder setErrorRetryViewId(@IdRes int errorRetryViewId) {
            this.errorRetryViewId = errorRetryViewId;
            return this;
        }

        /**
         * 设置网络异常布局重试操作控件ID
         *
         * @param netWorkErrorRetryViewId 网络异常布局重试操作控件ID
         */
        public Builder setNetWorkErrorRetryViewId(@IdRes int netWorkErrorRetryViewId) {
            this.netWorkErrorRetryViewId = netWorkErrorRetryViewId;
            return this;
        }

        /**
         * 设置网络不佳布局重试操作控件ID
         *
         * @param netWorkPoorRetryViewId 网络不佳布局重试操作控件ID
         */
        public Builder setNetWorkPoorRetryViewId(@IdRes int netWorkPoorRetryViewId) {
            this.netWorkPoorRetryViewId = netWorkPoorRetryViewId;
            return this;
        }

        /**
         * 设置各种布局公用的重试操作控件ID
         *
         * @param retryViewId 各种布局公用的重试操作控件ID
         */
        public Builder setRetryViewId(@IdRes int retryViewId) {
            this.retryViewId = retryViewId;
            return this;
        }

        /**
         * 设置是否使用覆盖式页面切换辅助类(默认为false)
         *
         * @param useOverlapStatusLayoutHelper 是否使用覆盖式页面切换辅助类
         */
        public Builder setUseOverlapStatusLayoutHelper(boolean useOverlapStatusLayoutHelper) {
            this.useOverlapStatusLayoutHelper = useOverlapStatusLayoutHelper;
            return this;
        }

        /**
         * 设置重试操作监听
         *
         * @param onRetryActionListener 重试监听
         */
        public Builder setOnRetryActionListener(OnRetryActionListener onRetryActionListener) {
            this.onRetryActionListener = onRetryActionListener;
            return this;
        }

        /**
         * 设置状态布局改变监听
         *
         * @param statusLayoutChangedListener 状态布局改变监听
         */
        public Builder setOnStatusLayoutChangedListener(OnStatusLayoutChangedListener statusLayoutChangedListener) {
            this.statusLayoutChangedListener = statusLayoutChangedListener;
            return this;
        }

        public StatusLayoutManager build() {
            return new StatusLayoutManager(this);
        }

        /**
         * 将指定布局解析为View
         *
         * @param resource 布局资源ID
         */
        private View inflate(@LayoutRes int resource) {
            if (inflater == null) {
                inflater = LayoutInflater.from(context);
            }
            return inflater.inflate(resource, null);
        }
    }

}
