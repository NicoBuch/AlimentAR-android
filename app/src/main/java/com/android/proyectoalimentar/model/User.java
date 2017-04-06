package com.android.proyectoalimentar.model;

public class User {

    String email;
    String name;
    String username;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    private Avatar avatar;

    public Avatar getAvatar() {
        return avatar;
    }
}
