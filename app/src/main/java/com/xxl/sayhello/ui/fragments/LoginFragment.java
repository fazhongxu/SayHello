package com.xxl.sayhello.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.xxl.sayhello.R;
import com.xxl.sayhello.models.LoginResponse;
import com.xxl.sayhello.ui.activities.MainActivity;
import com.xxl.sayhello.utils.SharedPreferencesUtil;
import com.xxl.sayhello.viewmodels.LoginViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private LoginViewModel viewModel;
    private TextInputEditText usernameEditText;
    private TextInputEditText passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initViews(view);
        setupObservers();
        return view;
    }

    private void initViews(View view) {
        usernameEditText = view.findViewById(R.id.usernameEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        loginButton = view.findViewById(R.id.loginButton);
        progressBar = view.findViewById(R.id.progressBar);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            viewModel.login(username, password);
        });
    }

    private void setupObservers() {
        viewModel.getLoginResult().observe(getViewLifecycleOwner(), this::handleLoginSuccess);
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), this::handleError);
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), this::handleLoading);
    }

    private void handleLoginSuccess(LoginResponse response) {
        if (response.isSuccess()) {
            SharedPreferencesUtil.saveToken(requireContext(), response.getToken());
            if (response.getUser() != null) {
                SharedPreferencesUtil.saveUserInfo(
                        requireContext(),
                        response.getUser().getId(),
                        response.getUser().getUsername(),
                        response.getUser().getNickname()
                );
            }
            SharedPreferencesUtil.setLoggedIn(requireContext(), true);
            Toast.makeText(requireContext(), getString(R.string.login_success), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(requireContext(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        }
    }

    private void handleError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }

    private void handleLoading(Boolean loading) {
        if (loading) {
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            loginButton.setEnabled(true);
        }
    }
}
