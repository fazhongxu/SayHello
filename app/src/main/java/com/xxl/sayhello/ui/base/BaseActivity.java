package com.xxl.sayhello.ui.base;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xxl.sayhello.R;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity抽象基类，封装Activity + Fragment的通用模式。
 * 提供默认的布局容器和Fragment加载逻辑，子类只需实现{@link #createFragment()}即可。
 * 集成了Hilt依赖注入。
 */
@AndroidEntryPoint
public abstract class BaseActivity extends InnerActivity {

    /**
     * 创建Fragment
     */
    protected abstract Fragment createFragment();

    /**
     * 获取布局资源ID，默认返回base_activity布局。
     */
    protected int getLayoutId() {
        return R.layout.base_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        if (savedInstanceState == null) {
            loadFragment();
        }
    }

    /**
     * 加载fragment
     */
    protected void loadFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment existingFragment = fragmentManager.findFragmentById(R.id.container);
        if (existingFragment != null) {
            transaction.remove(existingFragment);
        }
        transaction.add(R.id.container, createFragment());
        transaction.commit();
    }

    /**
     * 显示Toast提示
     *
     * @param message 提示内容
     */
    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}