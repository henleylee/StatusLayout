# StatusLayout-master —— 状态布局管理器
Activity或Fragment中切换不同状态页：有数据、无数据、加载中、加载失败、网络异常、网络不佳

## 效果演示 ##
#### 替换模式： ####
![](/screenshots/替换模式.gif)
#### 覆盖模式： ####
![](/screenshots/覆盖模式.gif)

## 开始使用 ##

#### 在Activity/Fragment中创建状态布局管理器： ####
```java
        statusLayoutManager = StatusLayoutManager.newBuilder(this)
                .setContentLayout(tvContent)                        // 设置内容布局
                .setEmptyLayout(R.layout.layout_empty)              // 设置无数据布局
                .setLoadingLayout(R.layout.layout_loading)          // 设置加载中布局
                .setErrorLayout(R.layout.layout_error)              // 设置加载异常布局
                .setNetWorkErrorLayout(R.layout.layout_no_network)  // 设置网络异常布局
                .setNetWorkPoorLayout(R.layout.layout_bad_network)  // 设置网络不佳布局
                .setRetryViewId(R.id.action_retry)                  // 设置各种布局公用的重试操作控件ID
                .setUseOverlapStatusLayoutHelper(false)             // 设置是否使用覆盖式页面切换辅助类
                .setOnStatusLayoutChangedListener(new OnStatusLayoutChangedListener() { // 设置状态布局改变监听
                    @Override
                    public void onStatusLayoutChanged(View currentLayout) {

                    }
                })
                .setOnRetryActionListener(new OnRetryActionListener() { // 设置重试操作监听
                    @Override
                    public void onRetryAction(View view) {
                        showToast("开始加载...");
                        statusLayoutManager.showLoadingLayout();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showToast("加载成功");
                                statusLayoutManager.restoreLayout();
                            }
                        }, 1500);
                    }
                }).build();                                         // 构建StatusLayoutManager

```

#### 由StatusLayoutManager重新转换为Builder进行构建： ####
```java
        statusLayoutManager = statusLayoutManager.newBuilder()
                .setUseOverlapStatusLayoutHelper(true)              // 设置是否使用覆盖式页面切换辅助类
                .build();
```
注意：使用这个方法的时候必须保证当前显示的布局是显示数据的内容布局。

#### 恢复显示数据的布局： ####
```java
        statusLayoutManager.restoreLayout();

```

#### 显示无数据布局： ####
```java
        statusLayoutManager.showEmptyLayout();

```

#### 显示加载中布局： ####
```java
        statusLayoutManager.showLoadingLayout();

```

#### 显示加载失败布局： ####
```java
        statusLayoutManager.showErrorLayout();

```

#### 显示无网络布局： ####
```java
        statusLayoutManager.showNetworkErrorLayout();

```

#### 显示网络不佳布局： ####
```java
        statusLayoutManager.showNetworkPoorLayout();

```