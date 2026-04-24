package com.xxl.sayhello.ui.base;

import androidx.fragment.app.Fragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public abstract class BaseFragment extends Fragment {
    protected void showToast(String message) {
        if (requireActivity() instanceof BaseActivity) {
            ((BaseActivity) requireActivity()).showToast(message);
        }
    }
}