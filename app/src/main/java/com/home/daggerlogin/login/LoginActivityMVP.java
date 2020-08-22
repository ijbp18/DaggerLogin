package com.home.daggerlogin.login;

public interface LoginActivityMVP {

    interface View{

        String getFirstName();
        String getLastName();

        void showUserNotAvailable();
        void showInputError();
        void showUserSaved();

        void setTextName(String firstName);
        void setTextLastName(String lastName);

    }

    interface Presenter{
        void setView(LoginActivityMVP.View view);//Con esto realizamos el binding (vinculo)

        void LoginButtonClicked();

        void getCurrentUser();

    }

    interface Model{

        void createUser(String name, String lastName);
        User getUser();

    }
}
