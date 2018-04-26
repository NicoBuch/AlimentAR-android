package com.android.proyectoalimentar.utils;

import com.android.proyectoalimentar.Configuration;
import com.android.proyectoalimentar.model.AuthenticatedUser;
import com.android.proyectoalimentar.model.User;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserStorage {

    private static final String LOGGED_USER = "LOGGED_USER";

    private User loggedUser;

    @Inject
    UserStorage() {
    }

    public User getLoggedUser() {
        if (loggedUser == null) {
            loggedUser = StorageUtils.getObjectFromSharedPreferences(LOGGED_USER, User.class);
        }
        return loggedUser;
    }

    public void login(AuthenticatedUser authenticatedUser) {
        loggedUser = authenticatedUser;
        StorageUtils.storeInSharedPreferences(
                Configuration.ACCESS_TOKEN, authenticatedUser.getAccessToken());
        StorageUtils.storeInSharedPreferences(LOGGED_USER, authenticatedUser);
    }

    public boolean isUserLogged() {
        return getLoggedUser() != null;
    }

    public void logOut() {
        StorageUtils.clearKey(
                Configuration.ACCESS_TOKEN);
        StorageUtils.clearKey(LOGGED_USER);

    }

}
