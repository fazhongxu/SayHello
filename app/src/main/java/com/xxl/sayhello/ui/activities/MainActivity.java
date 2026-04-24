package com.xxl.sayhello.ui.activities;

import androidx.fragment.app.Fragment;

import com.xxl.sayhello.R;
import com.xxl.sayhello.ui.base.BaseActivity;
import com.xxl.sayhello.ui.fragments.MainFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected Fragment createFragment() {
        return new MainFragment();
    }
}