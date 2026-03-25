package com.xxl.sayhello.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xxl.sayhello.R;
import com.xxl.sayhello.utils.SharedPreferencesUtil;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends Fragment {

    private TextView welcomeText;
    private Button logoutButton;

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

        logoutButton.setOnClickListener(v -> {
            SharedPreferencesUtil.clearUserData(requireContext());
            SharedPreferencesUtil.setLoggedIn(requireContext(), false);
            requireActivity().finish();
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
