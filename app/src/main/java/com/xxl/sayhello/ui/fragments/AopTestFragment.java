package com.xxl.sayhello.ui.fragments;

import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xxl.sayhello.R;
import com.xxl.sayhello.ui.base.BaseFragment;

public class AopTestFragment extends BaseFragment {

    private TextView resultText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aop_test, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        resultText = view.findViewById(R.id.tv_result);
        Button btnTest = view.findViewById(R.id.btn_test_android_id);
        btnTest.setOnClickListener(v -> testAndroidId());
    }

    private void testAndroidId() {
        String androidId = Settings.Secure.getString(
                requireActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID
        );
        resultText.setText(getString(R.string.aop_test_result, androidId != null ? androidId : "null"));
    }
}
