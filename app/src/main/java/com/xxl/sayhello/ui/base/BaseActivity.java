package com.xxl.sayhello.ui.base;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xxl.sayhello.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public abstract class BaseActivity extends BaseSupportActivity {

    protected abstract Fragment createFragment();

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

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}