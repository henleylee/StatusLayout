package com.henley.statuslayout;

import android.view.View;

/**
 * 重试操作监听器
 *
 * @author Henley
 * @date 2017/3/6 14:24
 */
public interface OnRetryActionListener {

    /**
     * 当重试操作触发时调用该方法
     *
     * @param view 重试操作的View
     */
    void onRetryAction(View view);
}
