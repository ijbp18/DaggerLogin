package com.home.daggerlogin.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.home.daggerlogin.R;
import com.home.daggerlogin.http.TwitchAPI;
import com.home.daggerlogin.http.twitch.Game;
import com.home.daggerlogin.http.twitch.Twitch;
import com.home.daggerlogin.root.App;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity implements LoginActivityMVP.View{

    @Inject
    LoginActivityMVP.Presenter presenter;

    @Inject
    TwitchAPI twitchAPI;

    @BindViews({R.id.edit_text_name, R.id.edit_text_last_name})
    List<EditText> inputs;

    @BindView(R.id.button_login)
    Button btnLogin;
    @OnClick(R.id.button_login)
    public void onClickLogin() {
        presenter.LoginButtonClicked();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ((App) getApplication()).getComponent().inject(this);

//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                presenter.LoginButtonClicked();
//            }
//        });

        //ejemplo de uso de la API de Twitch con retrofit
       /* Call<Twitch> call = twitchAPI.getTopGames("cqojukkav9dxjhwyonjzvv9itzupf9");

        call.enqueue(new Callback<Twitch>() {
            @Override
            public void onResponse(Call<Twitch> call, Response<Twitch> response) {

                if(response.body() != null){
                    List<Game> gameList = response.body().getGame();
                    for (Game game : gameList)
                        System.out.println(game.getName());
                }

            }

            @Override
            public void onFailure(Call<Twitch> call, Throwable t) {
                t.printStackTrace();
            }
        });*/

        //Mismo llamado pero utilizando RxJava
        twitchAPI.getTopGamesObservable("cqojukkav9dxjhwyonjzvv9itzupf9")
                .flatMap(new Function<Twitch, Observable<Game>>() {
                    @Override
                    public Observable<Game> apply(Twitch twitch) {
                        return Observable.fromIterable(twitch.getGame());
                    }
                }).flatMap(new Function<Game, Observable<String>>() {

            @Override
            public Observable<String> apply(Game game) {
                return Observable.just(game.getName());
            }
        }).filter(new Predicate<String>() {

            @Override
            public boolean test(String name) {
                return name.contains("W")||name.contains("w");
            }
        }).subscribeOn(Schedulers.io()) //se suscribe en un hilo en segundo plano para realizar el proceso de entrada y salida de datos
                .observeOn(AndroidSchedulers.mainThread()) //Parametro que indica que utizaremos el hilo en primer plano para mostrar los datos en la interfaz grafica
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String name) {
                        System.out.println("RxJava Says: " + name);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.getCurrentUser();
    }

    @Override
    public String getFirstName() {
        return this.inputs.get(0).getText().toString();
    }

    @Override
    public String getLastName() {
        return this.inputs.get(1).getText().toString();
    }

    @Override
    public void showUserNotAvailable() {
        Toast.makeText(this, "Error, el usuario no está disponible", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showInputError() {
        Toast.makeText(this, "Error, el nombre o apellido no pueden estar vacíos", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUserSaved() {
        Toast.makeText(this, "El usuario se guardó correctamente", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setTextName(String firstName) {
        this.inputs.get(0).setText(firstName);
    }

    @Override
    public void setTextLastName(String lastName) {
        this.inputs.get(1).setText(lastName);
    }
}
