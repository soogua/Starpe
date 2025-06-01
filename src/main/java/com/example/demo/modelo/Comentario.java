package com.example.demo.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Comentario {
    private String nombreUsuario;
    private int calificacion;
    private String comentario;
    private LocalDateTime fecha;
    private String restaurante;

    // Constructor
    public Comentario() {
        this.fecha = LocalDateTime.now();
    }

    public Comentario(String nombreUsuario, int calificacion, String comentario, String restaurante) {
        this.nombreUsuario = nombreUsuario;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.restaurante = restaurante;
        this.fecha = LocalDateTime.now();
    }

    // Getters y Setters
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(String restaurante) {
        this.restaurante = restaurante;
    }

    // Método para obtener fecha formateada
    public String getFechaFormateada() {
        LocalDateTime ahora = LocalDateTime.now();
        long diasDiferencia = java.time.Duration.between(fecha, ahora).toDays();
        
        if (diasDiferencia == 0) {
            return "Hoy";
        } else if (diasDiferencia == 1) {
            return "Ayer";
        } else if (diasDiferencia < 7) {
            return "Hace " + diasDiferencia + " días";
        } else if (diasDiferencia < 30) {
            long semanas = diasDiferencia / 7;
            return "Hace " + semanas + (semanas == 1 ? " semana" : " semanas");
        } else if (diasDiferencia < 365) {
            long meses = diasDiferencia / 30;
            return "Hace " + meses + (meses == 1 ? " mes" : " meses");
        } else {
            return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
    }
}
