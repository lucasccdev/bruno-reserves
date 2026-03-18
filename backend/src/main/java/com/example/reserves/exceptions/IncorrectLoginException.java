package com.example.reserves.exceptions;

public class IncorrectLoginException extends Exception {

    private final String usuario;
    private final String contrasena;

    public IncorrectLoginException(String usuario, String contrasena) {
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }
}