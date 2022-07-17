package com.aso.ectvotingcotrolpanel.ui.authentication.login.viewmodel;

import android.util.Patterns;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aso.ectvotingcotrolpanel.R;
import com.aso.ectvotingcotrolpanel.core.exception.AuthNoUserFoundException;
import com.aso.ectvotingcotrolpanel.core.exception.AuthWrongCredentialsException;
import com.aso.ectvotingcotrolpanel.core.exception.NetworkErrorException;
import com.aso.ectvotingcotrolpanel.data.Result;
import com.aso.ectvotingcotrolpanel.data.authentication.AuthenticationRepository;
import com.aso.ectvotingcotrolpanel.data.models.Admin;
import com.aso.ectvotingcotrolpanel.ui.authentication.customview.LoggedInUserView;
import com.aso.ectvotingcotrolpanel.ui.authentication.login.LoginFormState;
import com.aso.ectvotingcotrolpanel.ui.authentication.login.LoginResult;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private final AuthenticationRepository authenticationRepository;

    LoginViewModel(AuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password) {
        // can be launched in a separate asynchronous job
        authenticationRepository.login(email, password, result -> {
            if (result instanceof Result.Success) {
                Admin data = ((Result.Success<Admin>) result).getData();
                loginResult.setValue(new LoginResult(new LoggedInUserView(data.getFullName(), data.getEmail())));
            } else {
                if (((Result.Error<Admin>) result).getError() instanceof AuthWrongCredentialsException) {
                    loginResult.setValue(new LoginResult(R.string.wrong_credentials));
                } else if (((Result.Error<Admin>) result).getError() instanceof AuthNoUserFoundException) {
                    loginResult.setValue(new LoginResult(R.string.no_user_found));
                }  else if (((Result.Error<Admin>) result).getError() instanceof NetworkErrorException) {
                    loginResult.setValue(new LoginResult(R.string.network_error));
                } else {
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                }
            }
        });
    }

    public void loginDataChanged(String email, String password) {
        @Nullable
        Integer emailError = null;
        @Nullable
        Integer passwordError = null;
        if (!isUserNameValid(email)) {
            emailError = R.string.invalid_email;
        }
        if (!isPasswordValid(password)) {
            passwordError =  R.string.invalid_password;
        }
        if(emailError == null && passwordError == null){
            loginFormState.setValue(new LoginFormState(true));
            return;
        }
        loginFormState.setValue(new LoginFormState(emailError, passwordError));
    }

    // A placeholder email validation check
    private boolean isUserNameValid(String email) {
        if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return !email.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}