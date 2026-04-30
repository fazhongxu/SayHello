package com.xxl.sayhello.ui.fragments;

import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xxl.sayhello.R;
import com.xxl.sayhello.annotations.VipCheck;
import com.xxl.sayhello.ui.base.BaseFragment;
import com.xxl.sayhello.utils.SharedPreferencesUtil;

public class AopTestFragment extends BaseFragment {

    private TextView resultText;
    private Switch switchLogin;
    private Switch switchVip;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aop_test, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        resultText = view.findViewById(R.id.tv_result);
        switchLogin = view.findViewById(R.id.switch_login);
        switchVip = view.findViewById(R.id.switch_vip);

        Button btnAndroidId = view.findViewById(R.id.btn_test_android_id);
        btnAndroidId.setOnClickListener(v -> testAndroidId());

        switchLogin.setChecked(SharedPreferencesUtil.isLoggedIn(requireContext()));
        switchLogin.setOnCheckedChangeListener((buttonView, isChecked) ->
                SharedPreferencesUtil.setLoggedIn(requireContext(), isChecked));

        switchVip.setChecked(SharedPreferencesUtil.isVip(requireContext()));
        switchVip.setOnCheckedChangeListener((buttonView, isChecked) ->
                SharedPreferencesUtil.setVip(requireContext(), isChecked));

        Button btnVip = view.findViewById(R.id.btn_test_vip);
        btnVip.setOnClickListener(v -> testVipFunction());
    }

    private void testAndroidId() {
        String androidId = Settings.Secure.getString(
                requireActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID
        );
        resultText.setText(getString(R.string.aop_test_result, androidId != null ? androidId : "null"));
    }

    @VipCheck
    private void testVipFunction() {
        showToast(getString(R.string.aop_test_vip_success));
    }
}
