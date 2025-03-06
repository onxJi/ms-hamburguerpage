package com.onx.hamburgerpage.models;
import lombok.Data;

@Data
public class Usuario {
    private String id;
    private String nombre;
    private String email;
    private String passwordHash; // Almacenar hash de la contraseña
    private String rol; // "USER" o "ADMIN"
}
