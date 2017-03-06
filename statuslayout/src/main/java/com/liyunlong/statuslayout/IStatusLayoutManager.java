package com.liyunlong.statuslayout;

import android.content.Context;
import android.view.View;

/**
 * 状态布局页面切换管理器接口
 *
 * @author liyunlong
 * @date 2017/3/6 15:49
 */
interface IStatusLayoutManager {

    /**
     * 返回上下文
     */
    Context getContext();

    /**
     * 返回当前正在显示的View
     */
    View getCurrentLayout();

    /**
     * 返回显示数据的View
     */
    View getContentLayout();

    /**
     * 返回无数据View
     */
    View getEmptyLayout();

    /**
     * 返回加载中View
     */
    View getLoadingLayout();

    /**
     * 返回加载失败View
     */
    View getErrorLayout();

    /**
     * 返回无网络View
     */
    View getNetworkErrorLayout();

    /**
     * 返回网络不佳View
     */
    View getNetworkPoorLayout();

    /**
     * 显示内容View
     */
    void showContentLayout();

    /**
     * 显示无数据View
     */
    void showEmptyLayout();

    /**
     * 显示加载中View
     */
    void showLoadingLayout();

    /**
     * 显示加载失败View
     */
    void showErrorLayout();

    /**
     * 显示无网络View
     */
    void showNetworkErrorLayout();

    /**
     * 显示网络不佳View
     */
    void showNetworkPoorLayout();

    /**
     * 恢复显示有数据View
     */
    void restoreLayout();

    /**
     * 切换要显示的View
     */
    void showStatusLayout(View view);

    /**
     * 设置IStatusLayoutHelper
     */
    void setStatusLayoutHelper(IStatusLayoutHelper helper);

    /**
     * 释放所有View
     */
    void releaseStatusLayouts();

}
