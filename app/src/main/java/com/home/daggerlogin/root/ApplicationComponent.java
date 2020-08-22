package com.home.daggerlogin.root;


import com.home.daggerlogin.http.TwitchModule;
import com.home.daggerlogin.login.LoginActivity;
import com.home.daggerlogin.login.LoginModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, LoginModule.class, TwitchModule.class})
public interface ApplicationComponent {
    //En esta interfaz irán todos los componentes que dependerán del modulo
    void inject(LoginActivity target);
}
