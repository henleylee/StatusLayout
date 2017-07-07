package com.liyunlong.statuslayout.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.liyunlong.statuslayout.OnRetryActionListener;
import com.liyunlong.statuslayout.OnStatusLayoutChangedListener;
import com.liyunlong.statuslayout.StatusLayoutManager;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String[] actions = {"有数据", "无数据", "加载中", "加载失败", "网络异常", "网络不佳"};
    private TextView tvContent;
    private Toast toast;
    private int currentPosition;
    private StatusLayoutManager statusLayoutManager;
    private Handler mHandler = new Handler();

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
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg_mode);
        RadioButton rbReplace = (RadioButton) findViewById(R.id.rb_replace);
        rbReplace.setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                statusLayoutManager.restoreLayout(); // 恢复显示数据的View
                if (checkedId == R.id.rb_replace) {
                    statusLayoutManager = statusLayoutManager.newBuilder()
                            .setUseOverlapStatusLayoutHelper(false)
                            .build();
                } else if (checkedId == R.id.rb_overlap) {
                    statusLayoutManager = statusLayoutManager.newBuilder()
                            .setUseOverlapStatusLayoutHelper(true)
                            .build();
                }
                updateStatusLayout(currentPosition); // 还原当前状态
            }
        });

    }

    private void creatStatusLayoutManager() {
        statusLayoutManager = StatusLayoutManager.newBuilder(this)
                .setContentLayout(tvContent)
                .setEmptyLayout(R.layout.layout_empty)
                .setErrorLayout(R.layout.layout_error)
                .setLoadingLayout(R.layout.layout_loading)
                .setNetWorkErrorLayout(R.layout.layout_no_network)
                .setNetWorkPoorLayout(R.layout.layout_bad_network)
                .setRetryViewId(R.id.action_retry)
                .setUseOverlapStatusLayoutHelper(false)
                .setOnStatusLayoutChangedListener(new OnStatusLayoutChangedListener() {
                    @Override
                    public void onStatusLayoutChanged(View currentLayout) {

                    }
                }).setOnRetryActionListener(new OnRetryActionListener() {
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
                }).build();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        updateStatusLayout(position);
    }

    private void updateStatusLayout(int position) {
        this.currentPosition = position;
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
