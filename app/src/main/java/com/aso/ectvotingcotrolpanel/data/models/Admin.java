package com.aso.ectvotingcotrolpanel.data.models;

import java.util.HashMap;
import java.util.Map;

public class Admin {
    private final String id;
    private final String email;
    private final String fullName;

    public Admin(String id, String email, String fullName) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> admin = new HashMap<>();
        admin.put("fullName", fullName);
        return admin;
    }

    public static Admin fromMap(Map<String, Object> admin) {
        return new Admin(
                (String) admin.get("id"),
                (String) admin.get("email"),
                (String) admin.get("fullName")
        );
    }
}
