package com.liyunlong.statuslayout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 有数据、无数据、加载中、加载失败、网络异常、网络不佳页面切换辅助类(覆盖)
 *
 * @author liyunlong
 * @date 2017/3/6 15:45
 */
public class OverlapStatusLayoutHelper implements IStatusLayoutHelper {

    private View contentLayout;  // 显示数据的View
    private IStatusLayoutHelper helper; // 切换不同视图的帮助类

    public OverlapStatusLayoutHelper(View contentLayout) {
        this.contentLayout = contentLayout; // 显示数据的View
        ViewGroup parent; // 找到父View
        if (this.contentLayout.getParent() != null) {
            parent = (ViewGroup) this.contentLayout.getParent();
        } else {
            parent = (ViewGroup) this.contentLayout.getRootView().findViewById(android.R.id.content);
        }

        int childIndex = 0; // 要显示的View在父View中的位置
        int childCount = parent.getChildCount();
        for (int index = 0; index < childCount; index++) { // 遍历显示数据的View的父View的Child
            if (this.contentLayout == parent.getChildAt(index)) {
                childIndex = index; // 获取要显示的View在父View中的位置
                break;
            }
        }

        // 移除显示数据的View，并重新将一个FrameLayout添加到显示数据的View的位置
        Context context = this.contentLayout.getContext();
        ViewGroup.LayoutParams layoutParams = this.contentLayout.getLayoutParams();// 获取显示数据的View的参数信息
        FrameLayout frameLayout = new FrameLayout(context); // 创建一个FrameLayout
        parent.removeViewAt(childIndex); // 移除显示数据的View
        parent.addView(frameLayout, childIndex, layoutParams);// 将创建的FrameLayout添加进原来的View的位置
        // 在这个FrameLayout中实现将新的View覆盖在原来显示数据的View上
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View floatView = new View(context); // 创建用于覆盖显示数据的View的新View
        frameLayout.addView(this.contentLayout, params);// 将显示数据的View添加到创建的FrameLayout中
        frameLayout.addView(floatView, params);// 将用于覆盖显示数据的View的新View添加到创建的FrameLayout中

        helper = new ReplaceStatusLayoutHelper(floatView); // 创建切换不同视图的帮助类
    }

    @Override
    public Context getContext() {
        return helper.getContext();
    }

    @Override
    public ViewGroup getParentLayout() {
        return helper.getParentLayout();
    }

    @Override
    public View getContentLayout() {
        return helper.getContentLayout();
    }

    @Override
    public View getCurrentLayout() {
        return helper.getCurrentLayout();
    }

    @Override
    public boolean showStatusLayout(View view) {
        helper.showStatusLayout(view);
        return false;
    }

    @Override
    public boolean restoreLayout() {
        helper.restoreLayout();
        return false;
    }
}
