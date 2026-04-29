package com.xxl.sayhello.ui.base;

import androidx.fragment.app.Fragment;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment抽象基类，提供Fragment通用的辅助方法。
 * 集成了Hilt依赖注入。
 */
@AndroidEntryPoint
public abstract class BaseFragment extends Fragment {

    /**
     * 显示Toast提示，委托给所属的{@link BaseActivity}来显示
     *
     * @param message 提示内容
     */
    protected void showToast(String message) {
        if (requireActivity() instanceof BaseActivity) {
            ((BaseActivity) requireActivity()).showToast(message);
        }
    }
}