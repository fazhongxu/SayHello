package com.xxl.sayhello.ui.activities;

import androidx.fragment.app.Fragment;

import com.xxl.sayhello.R;
import com.xxl.sayhello.ui.base.BaseActivity;
import com.xxl.sayhello.ui.fragments.TestCaseFragment;

public class TestCaseActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_case;
    }

    @Override
    protected Fragment createFragment() {
        return new TestCaseFragment();
    }
}