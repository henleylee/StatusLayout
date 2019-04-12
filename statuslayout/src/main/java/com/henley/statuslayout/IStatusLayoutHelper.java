package com.henley.statuslayout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * 状态布局页面切换辅助类接口
 *
 * @author Henley
 * @date 2017/3/6 14:28
 */
interface IStatusLayoutHelper {

    /**
     * 返回上下文
     */
    Context getContext();

    /**
     * 返回内容布局的父容器
     */
    ViewGroup getParentLayout();

    /**
     * 返回显示数据的View
     */
    View getContentLayout();

    /**
     * 返回当前正在显示的View
     */
    View getCurrentLayout();

    /**
     * 切换要显示的View
     */
    boolean showStatusLayout(View view);

    /**
     * 恢复显示数据的View
     */
    boolean restoreLayout();

}
