package com.aso.ectvotingcotrolpanel.data.authentication;

import com.aso.ectvotingcotrolpanel.data.Result;
import com.aso.ectvotingcotrolpanel.data.ResultCallback;
import com.aso.ectvotingcotrolpanel.data.models.Admin;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class AuthenticationRepository {

    private static volatile AuthenticationRepository instance;

    private final AuthenticationDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    public Admin admin = null;

    // private constructor : singleton access
    private AuthenticationRepository(AuthenticationDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static AuthenticationRepository getInstance(AuthenticationDataSource dataSource) {
        if (instance == null) {
            instance = new AuthenticationRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return admin != null;
    }

    public void logout(ResultCallback<Void> callback) {
        admin = null;
        dataSource.logout(callback);
    }

    private void setLoggedInUser(Admin admin) {
        this.admin = admin;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public void login(String email, String password, ResultCallback<Admin> callback) {
        // handle login
        dataSource.login(email, password, result -> {
            if (result instanceof Result.Success) {
                setLoggedInUser(((Result.Success<Admin>) result).getData());
            }
            callback.onComplete(result);
        });
    }

    public void register(String email, String password, String fullName, ResultCallback<Admin> callback) {
        // handle register
        dataSource.register(email, password, fullName, result -> {
            if (result instanceof Result.Success) {
                setLoggedInUser(((Result.Success<Admin>) result).getData());
            }
            callback.onComplete(result);
        });
    }
}