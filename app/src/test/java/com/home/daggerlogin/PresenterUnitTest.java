package com.home.daggerlogin;

import com.home.daggerlogin.login.LoginActivity;
import com.home.daggerlogin.login.LoginActivityMVP;
import com.home.daggerlogin.login.LoginActivityPresenter;
import com.home.daggerlogin.login.User;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class PresenterUnitTest {

    LoginActivityPresenter presenter;
    User user;

    //Hacemos referencia a las interfaces necesarias para poder validar el Presenter
    LoginActivityMVP.Model mockedModel;
    LoginActivityMVP.View mockedView;

    @Before
    public void init() {

        //Parametros de configuración que se realizan antes de realizar los test
        mockedModel = mock(LoginActivityMVP.Model.class);
        mockedView = mock(LoginActivityMVP.View.class);

        user = new User("Manolo", "Escobar");

        //Con esta línea simulamos el get del Model para poder seguir validando el Presenter
//        when(mockedModel.getUser()).thenReturn(user);

//        when(mockedView.getFirstName()).thenReturn("Antonio");
//        when(mockedView.getLastName()).thenReturn("Banderas");

        presenter = new LoginActivityPresenter(mockedModel);
        presenter.setView(mockedView);
    }

    @Test
    public void noExistInteractionWithView(){
        presenter.getCurrentUser();

        //Con esta verificacion se valida si no hubo interacción
//        verifyZeroInteractions(mockedView);

        //Validación correcta cuando no tenemos un usuario creado y entra al menos una vez
        // al metodo que seleccionamos (showUserNotAvailable)
        verify(mockedView, times(1)).showUserNotAvailable();
    }

    @Test
    public void loadUserFromRepoWhenValidUserIsPresent(){
        //Prueba que cargue el usuario del repo cuando ya hay un user loggeado.
        when(mockedModel.getUser()).thenReturn(user);

        presenter.getCurrentUser();

        //Se comprueba la interacción con el modelo de datos
        verify(mockedModel, times(1)).getUser();

        //Se comprueba la interacción con la vista
        verify(mockedView, times(1)).setTextName("Manolo");
        verify(mockedView, times(1)).setTextLastName("Escobar");
        verify(mockedView, never()).showUserNotAvailable();
    }

    @Test
    public void showErrorMessageWhenUserIsNull(){

        //simulamos el getUser del modelo
        when(mockedModel.getUser()).thenReturn(null);

        presenter.getCurrentUser();

        //Se comprueba la interacción con el modelo de datos
        verify(mockedModel, times(1)).getUser();

        //Se comprueba la interacción con la vista
        verify(mockedView, never()).setTextName("Manolo");
        verify(mockedView, never()).setTextLastName("Escobar");

        verify(mockedView, times(1)).showUserNotAvailable();

    }

    @Test
    public void errorCreateMessageIfAnyFieldIsEmpty(){
        //Crear un mensaje de error si alguno de los campos que comprobramos antes de crear un user está vació.

        //primero validamos FirstName vació sin importar si existe texto o no en LastName
        when(mockedView.getFirstName()).thenReturn("");

        presenter.LoginButtonClicked();
        verify(mockedView, times(1)).getFirstName();
        verify(mockedView, never()).getLastName();
        verify(mockedView, times(1)).showInputError();

        //después validamos LastName vació, teniendo en cuenta que en FirstName sí tiene text.
        when(mockedView.getFirstName()).thenReturn("Pedrito");
        when(mockedView.getLastName()).thenReturn("");

        presenter.LoginButtonClicked();

        //El metodo se debe llamar 2 veces, una en la prueba anterior y otra en la actual.
        verify(mockedView, times(2)).getFirstName();
        verify(mockedView, times(1)).getLastName();
        verify(mockedView, times(2)).showInputError();



    }

    @Test
    public void saveValidUser(){
        //Proceso de crear las llamadas a los metodos necesarios
        // y validar que el user valido puede ser guardado a través de las llamadas.

        when(mockedView.getFirstName()).thenReturn("Joao");
        when(mockedView.getLastName()).thenReturn("Betancourth");

        presenter.LoginButtonClicked();

        //Se verifica que efectivamente se hagan dos llamadas a los metodos get
        verify(mockedView, times(2)).getFirstName();
        verify(mockedView, times(2)).getLastName();

        //Verificamos que el modelo persista en el repositorio.
        verify(mockedModel, times(1)).createUser("Joao", "Betancourth");
        //Verificamos que se muestre el mensaje de exito
        verify(mockedView, times(1)).showUserSaved();

    }

}
