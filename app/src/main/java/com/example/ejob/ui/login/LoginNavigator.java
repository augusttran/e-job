package com.example.ejob.ui.login;

public interface LoginNavigator {
    void handleError(Throwable throwable);

    void login();

    void openAdminActivity();

    void openEmployerActivity();

    void openUserActivity();

}
