package com.home.daggerlogin.login;

public class LoginActivityModel implements LoginActivityMVP.Model{

    private LoginRepository repository;

    public LoginActivityModel(LoginRepository repository){
        this.repository = repository;
    }

    @Override
    public void createUser(String name, String lastName) {
        //Aquí irá logica de business,ejemplo comprobar si existe o no, email validado, temas de validación antes de persistir.
        repository.saveuser(new User(name, lastName));
    }

    @Override
    public User getUser() {
        //Aquí irá lógica de business antes de devolveer el usuario, modificar, agregar campos, etc.
        return repository.getUser();
    }
}
