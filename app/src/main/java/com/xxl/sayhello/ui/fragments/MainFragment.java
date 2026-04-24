package com.xxl.sayhello.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xxl.sayhello.R;
import com.xxl.sayhello.icon.IconManager;
import com.xxl.sayhello.share.ShareContent;
import com.xxl.sayhello.share.ShareManager;
import com.xxl.sayhello.ui.activities.TestCaseActivity;
import com.xxl.sayhello.utils.SharedPreferencesUtil;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends Fragment {

    private TextView welcomeText;
    private Button logoutButton;
    private Button shareButton;
    private Button iconSwitchButton;
    private Button aiImageCaseButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initViews(view);
        setupWelcomeMessage();
        return view;
    }

    private void initViews(View view) {
        welcomeText = view.findViewById(R.id.welcomeText);
        logoutButton = view.findViewById(R.id.logoutButton);
        shareButton = view.findViewById(R.id.shareButton);
        iconSwitchButton = view.findViewById(R.id.iconSwitchButton);
        aiImageCaseButton = view.findViewById(R.id.aiImageCaseButton);

        logoutButton.setOnClickListener(v -> {
            SharedPreferencesUtil.clearUserData(requireContext());
            SharedPreferencesUtil.setLoggedIn(requireContext(), false);
            requireActivity().finish();
        });

        shareButton.setOnClickListener(v -> {
            // 测试分享功能
            ShareContent content = new ShareContent.Builder()
                    .setTitle("SayHello 应用")
                    .setContent("这是一个测试分享，来自 SayHello 应用")
                    .setUrl("https://example.com")
                    .setImageUrl("https://picsum.photos/200/200") // 测试网络图片
                    .build();
            ShareManager.getInstance().showShareDialog(requireActivity(), content);
        });

        iconSwitchButton.setOnClickListener(v -> {
            boolean success;
            if (IconManager.isDefaultIcon(requireContext())) {
                success = IconManager.setAlternateIcon(requireContext());
                if (success) {
                    Toast.makeText(requireContext(), "已切换到备用图标，请重启应用查看效果", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "切换图标失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                success = IconManager.setDefaultIcon(requireContext());
                if (success) {
                    Toast.makeText(requireContext(), "已切换到默认图标，请重启应用查看效果", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "切换图标失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        aiImageCaseButton.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), TestCaseActivity.class));
        });
    }

    private void setupWelcomeMessage() {
        String nickname = SharedPreferencesUtil.getNickname(requireContext());
        if (nickname == null || nickname.isEmpty()) {
            nickname = SharedPreferencesUtil.getUsername(requireContext());
        }
        if (nickname == null || nickname.isEmpty()) {
            nickname = "User";
        }
        welcomeText.setText(getString(R.string.welcome, nickname));
    }
}