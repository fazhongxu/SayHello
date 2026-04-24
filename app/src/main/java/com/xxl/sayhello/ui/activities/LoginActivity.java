package com.xxl.sayhello.ui.activities;

import androidx.fragment.app.Fragment;

import com.xxl.sayhello.ui.base.BaseActivity;
import com.xxl.sayhello.ui.fragments.LoginFragment;

public class LoginActivity extends BaseActivity {

    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }
}