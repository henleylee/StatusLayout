package com.liyunlong.statuslayout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * 有数据、无数据、加载中、加载失败、网络异常、网络不佳页面切换辅助类(替换模式)
 *
 * @author liyunlong
 * @date 2017/3/6 14:30
 */
final class ReplaceStatusLayoutHelper implements IStatusLayoutHelper {

    private View contentLayout; // 显示数据的View
    private ViewGroup parentLayout; // 显示数据的View的父View
    private int viewIndex; // 要显示的View在父View中的位置
    private ViewGroup.LayoutParams params; // 显示数据的View的参数信息
    private View currentLayout; // 当前显示的View

    ReplaceStatusLayoutHelper(View contentLayout) {
        this.contentLayout = contentLayout; // 显示数据的View
        initContentLayoutParams(); // 初始化参数信息
    }

    /**
     * 初始化参数信息
     */
    private void initContentLayoutParams() {
        this.params = this.contentLayout.getLayoutParams(); // 获取显示数据的View的参数信息
        if (contentLayout.getParent() != null) { // 获取显示数据的View的父View
            this.parentLayout = (ViewGroup) contentLayout.getParent();
        } else {
            this.parentLayout = (ViewGroup) contentLayout.getRootView().findViewById(android.R.id.content);
        }
        int count = parentLayout.getChildCount();
        for (int index = 0; index < count; index++) { // 遍历显示数据的View的父View的Child
            if (contentLayout == parentLayout.getChildAt(index)) {
                this.viewIndex = index; // 获取要显示的View在父View中的位置
                break;
            }
        }
        this.currentLayout = this.contentLayout; // 当前显示的View
    }

    @Override
    public Context getContext() {
        return contentLayout.getContext();
    }

    @Override
    public ViewGroup getParentLayout() {
        return parentLayout;
    }

    public View getContentLayout() {
        return contentLayout;
    }

    public View getCurrentLayout() {
        return currentLayout;
    }

    @Override
    public boolean showStatusLayout(View view) {
        if (parentLayout == null) {
            initContentLayoutParams();
        }
        // 如果已经是那个view，那就不需要再进行替换操作了
        if (currentLayout != view) {
            this.currentLayout = view;
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            // 切换的时候，先移除原先显示的View,再显示需要的View
            parentLayout.removeViewAt(viewIndex);
            parentLayout.addView(view, viewIndex, params);
            return true;
        }
        return false;
    }

    @Override
    public boolean restoreLayout() {
        return showStatusLayout(contentLayout);
    }

}
