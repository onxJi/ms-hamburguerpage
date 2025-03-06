package com.onx.hamburgerpage.models;
import lombok.Data;
import java.util.List;

@Data
public class Pedido {
    private String id;
    private String usuarioId;
    private List<Producto> productos;
    private double total;
    private String estado; // "PENDIENTE", "COMPLETADO", etc.
}