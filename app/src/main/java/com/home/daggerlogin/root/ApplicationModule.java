package com.home.daggerlogin.root;


import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    //Es la clase que llevara un control de todas las dependencias dentro nuestro proyecto.
    //Se pueden crear varios modulos para cada una de las funcionalidades, por ejem: Login, server.

    private Application application;

    public ApplicationModule(Application app){
        this.application = app;
    }

    @Provides // porque tiene un valor de retorno (contexto)
    @Singleton //porque es necesario solo instanciarlo una vez
    public Context provideContext(){
        return application;
    }

}
