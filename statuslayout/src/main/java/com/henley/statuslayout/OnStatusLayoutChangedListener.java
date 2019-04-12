package com.henley.statuslayout;

import android.view.View;

/**
 * 状态布局改变监听
 *
 * @author Henley
 * @date 2017/3/6 14:24
 */
public interface OnStatusLayoutChangedListener {

    /**
     * 当状态布局改变时调用该方法
     *
     * @param currentLayout 当前显示的布局
     */
    void onStatusLayoutChanged(View currentLayout);
}
