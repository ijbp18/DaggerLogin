package com.home.daggerlogin.http;

import com.home.daggerlogin.http.twitch.Twitch;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class TwitchModule {

    public final String BASE_URL = "https://api.twitch.tv/helix/";

    @Provides
    public OkHttpClient provideHttpClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //Elegimos el nivel, en este caso BODY ya que deseamos obtener la data del consumo REST, que generalmente viene en el body.
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS); //Cambiamos Body por Header para poder obtener todos los parametros obtenidos del API (RxJava)
        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    @Provides
    public Retrofit provideRetrofit(String baseURL, OkHttpClient client){
        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //añadimos esta linea para demstrara el RxJava y con esto se sabrá quién será el observble
                .build();
    }

    @Provides
    public TwitchAPI provideTwitchService(){
        return provideRetrofit(BASE_URL, provideHttpClient()).create(TwitchAPI.class);
    }

}
