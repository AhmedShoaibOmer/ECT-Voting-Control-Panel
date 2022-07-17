package com.aso.ectvotingcotrolpanel.ui.authentication.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import com.aso.ectvotingcotrolpanel.MainActivity;
import com.aso.ectvotingcotrolpanel.R;
import com.aso.ectvotingcotrolpanel.databinding.ActivityLoginBinding;
import com.aso.ectvotingcotrolpanel.ui.authentication.customview.LoggedInUserView;
import com.aso.ectvotingcotrolpanel.ui.authentication.login.viewmodel.LoginViewModel;
import com.aso.ectvotingcotrolpanel.ui.authentication.login.viewmodel.LoginViewModelFactory;
import com.aso.ectvotingcotrolpanel.ui.customview.CustomProgressDialog;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private LoginResult loginResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Needed for the loading overlay
        CustomProgressDialog progressDialog = new CustomProgressDialog(this);

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final TextInputEditText emailEditText = binding.editTextEmail;
        final TextInputEditText passwordEditText = binding.editTextPassword;
        final AppCompatButton loginButton = binding.cirLoginButton;

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            loginButton.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getEmailError() != null) {
                emailEditText.setError(getString(loginFormState.getEmailError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        });

        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) {
                return;
            }
            progressDialog.stop();
            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
            }
            if (loginResult.getSuccess() != null) {
                this.loginResult = loginResult;
                Intent switchActivity = new Intent(this, MainActivity.class);
                startActivity(switchActivity);
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
            return false;
        });

        loginButton.setOnClickListener(v -> {
            if(!emailEditText.getText().toString().isEmpty() || !passwordEditText.getText().toString().isEmpty()){
                progressDialog.start(getString(R.string.logging_in));
                loginViewModel.login(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}