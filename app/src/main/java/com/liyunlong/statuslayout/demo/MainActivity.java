package com.liyunlong.statuslayout.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.liyunlong.statuslayout.OnRetryListener;
import com.liyunlong.statuslayout.OnStatusLayoutChangedListener;
import com.liyunlong.statuslayout.StatusLayoutManager;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String[] actions = {"有数据", "无数据", "加载中", "加载失败", "网络异常", "网络不佳"};
    private TextView tvContent;
    private Toast toast;
    private StatusLayoutManager statusLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gvMenu = (GridView) findViewById(R.id.menu);
        tvContent = (TextView) findViewById(R.id.content);
        creatStatusLayoutManager();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Arrays.asList(actions));
        gvMenu.setAdapter(adapter);
        gvMenu.setOnItemClickListener(this);

    }

    private void creatStatusLayoutManager() {
        statusLayoutManager = StatusLayoutManager.newBuilder(this)
                .setContentLayout(tvContent)
                .setEmptyLayout(R.layout.layout_empty)
                .setErrorLayout(R.layout.layout_error)
                .setLoadingLayout(R.layout.layout_loading)
                .setNetWorkErrorLayout(R.layout.layout_no_network)
                .setNetWorkPoorLayout(R.layout.layout_bad_network)
                .setRetryViewId(R.id.action)
                .setUseOverlapStatusLayoutHelper(false)
                .setOnStatusLayoutChangedListener(new OnStatusLayoutChangedListener() {
                    @Override
                    public void onStatusLayoutChanged(View currentLayout) {

                    }
                }).setOnRetryListener(new OnRetryListener() {
                    @Override
                    public void onRetry(View view) {
                        showToast("开始加载...");
                        statusLayoutManager.showLoadingLayout();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showToast("加载成功");
                                        statusLayoutManager.restoreLayout();
                                    }
                                });
                            }
                        }).start();

                    }
                }).build();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                statusLayoutManager.restoreLayout();
                break;
            case 1:
                statusLayoutManager.showEmptyLayout();
                break;
            case 2:
                statusLayoutManager.showLoadingLayout();
                break;
            case 3:
                statusLayoutManager.showErrorLayout();
                break;
            case 4:
                statusLayoutManager.showNetworkErrorLayout();
                break;
            case 5:
                statusLayoutManager.showNetworkPoorLayout();
                break;
        }
    }

    public void showToast(CharSequence message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

}
