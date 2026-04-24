package com.xxl.sayhello.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xxl.sayhello.R;
import com.xxl.sayhello.adapters.FeatureAdapter;
import com.xxl.sayhello.icon.IconManager;
import com.xxl.sayhello.models.FeatureItem;
import com.xxl.sayhello.share.ShareContent;
import com.xxl.sayhello.share.ShareManager;
import com.xxl.sayhello.ui.activities.TestCaseActivity;
import com.xxl.sayhello.ui.base.BaseFragment;
import com.xxl.sayhello.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends BaseFragment {

    private TextView welcomeText;
    private FeatureAdapter featureAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initViews(view);
        setupWelcomeMessage();
        initFeatureData();
        return view;
    }

    private void initViews(View view) {
        welcomeText = view.findViewById(R.id.welcomeText);
        featureAdapter = new FeatureAdapter(new ArrayList<>());
        featureAdapter.setOnItemClickListener((adapter, view1, position) -> {
            FeatureItem item = (FeatureItem) adapter.getItem(position);
            if (item.getTargetClass() != null) {
                Intent intent = new Intent(requireContext(), item.getTargetClass());
                startActivity(intent);
            } else {
                switch (position) {
                    case 1:
                        handleShare();
                        break;
                    case 2:
                        handleIconSwitch();
                        break;
                    case 3:
                        handleLogout();
                        break;
                }
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.featureRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(featureAdapter);
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

    private void initFeatureData() {
        List<FeatureItem> features = new ArrayList<>();

        features.add(new FeatureItem("测试案例", TestCaseActivity.class));
        features.add(new FeatureItem("分享功能", null));
        features.add(new FeatureItem("图标切换", null));
        features.add(new FeatureItem("退出登录", null));

        featureAdapter.setNewData(features);
    }

    private void handleShare() {
        ShareContent content = new ShareContent.Builder()
                .setTitle("SayHello 应用")
                .setContent("这是一个测试分享，来自 SayHello 应用")
                .setUrl("https://example.com")
                .setImageUrl("https://picsum.photos/200/200")
                .build();
        ShareManager.getInstance().showShareDialog(requireActivity(), content);
    }

    private void handleIconSwitch() {
        boolean success;
        if (IconManager.isDefaultIcon(requireContext())) {
            success = IconManager.setAlternateIcon(requireContext());
            if (success) {
                showToast("已切换到备用图标，请重启应用查看效果");
            } else {
                showToast("切换图标失败");
            }
        } else {
            success = IconManager.setDefaultIcon(requireContext());
            if (success) {
                showToast("已切换到默认图标，请重启应用查看效果");
            } else {
                showToast("切换图标失败");
            }
        }
    }

    private void handleLogout() {
        SharedPreferencesUtil.clearUserData(requireContext());
        SharedPreferencesUtil.setLoggedIn(requireContext(), false);
        requireActivity().finish();
    }
}