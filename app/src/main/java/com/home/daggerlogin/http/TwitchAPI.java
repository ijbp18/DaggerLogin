package com.home.daggerlogin.http;

import com.home.daggerlogin.http.twitch.Twitch;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface TwitchAPI {

//    @GET("games/top")
//    Call<Twitch> getTopGames(@Header("Client-Id") String clientId);

    //El mismo llamado pero utilizando Programación reactiva (RxJava)
    @GET("games/top")
    Observable<Twitch> getTopGamesObservable(@Header("Client-Id") String clientId);
}
