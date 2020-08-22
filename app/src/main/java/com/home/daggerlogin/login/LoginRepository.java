package com.home.daggerlogin.login;

public interface LoginRepository {

    void saveuser(User user);

    User getUser();
}
