package com.xxl.sayhello.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.xxl.sayhello.models.LoginResponse;
import com.xxl.sayhello.repositories.UserRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class LoginViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<LoginResponse> loginResult = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    @Inject
    public LoginViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<LoginResponse> getLoginResult() {
        return loginResult;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            errorMessage.setValue("请输入用户名");
            return;
        }
        if (password == null || password.trim().isEmpty()) {
            errorMessage.setValue("请输入密码");
            return;
        }

        isLoading.setValue(true);
        disposables.add(userRepository.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            isLoading.setValue(false);
                            if (response.isSuccess()) {
                                loginResult.setValue(response);
                            } else {
                                errorMessage.setValue(response.getMessage());
                            }
                        },
                        throwable -> {
                            isLoading.setValue(false);
                            errorMessage.setValue("登录失败：" + throwable.getMessage());
                        }
                ));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}