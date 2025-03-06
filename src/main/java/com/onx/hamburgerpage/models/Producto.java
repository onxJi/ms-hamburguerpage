package com.onx.hamburgerpage.models;

import lombok.Data;

@Data
public class Producto {
    private String id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String imagenUrl;
}
