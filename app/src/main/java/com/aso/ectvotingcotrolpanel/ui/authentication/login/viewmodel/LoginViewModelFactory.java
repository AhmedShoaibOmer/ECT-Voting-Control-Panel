package com.aso.ectvotingcotrolpanel.ui.authentication.login.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.aso.ectvotingcotrolpanel.data.authentication.AuthenticationDataSource;
import com.aso.ectvotingcotrolpanel.data.authentication.AuthenticationRepository;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(AuthenticationRepository.getInstance(new AuthenticationDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}