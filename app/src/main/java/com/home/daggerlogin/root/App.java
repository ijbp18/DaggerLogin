package com.home.daggerlogin.root;

import android.app.Application;

import com.home.daggerlogin.login.LoginModule;

public class App extends Application {

    //Clase donde se inicializaran todas y cada unas de los componentes que necesitemos crear DaggerII
    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .loginModule(new LoginModule())
                .build();
    }

    public ApplicationComponent getComponent(){
        return component;
    }
}
