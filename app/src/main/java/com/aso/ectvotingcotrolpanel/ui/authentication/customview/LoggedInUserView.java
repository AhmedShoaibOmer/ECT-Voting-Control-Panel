package com.aso.ectvotingcotrolpanel.ui.authentication.customview;

/**
 * Class exposing authenticated user details to the UI.
 */
public class LoggedInUserView {
    private final String fullName;
    private final String email;
    //... other data fields that may be accessible to the UI

    public LoggedInUserView(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }
}