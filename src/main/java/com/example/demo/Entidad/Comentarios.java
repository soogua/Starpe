package com.example.demo.Entidad;

import com.example.demo.Entidad.Usuario;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios")
public class Comentarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación muchos comentarios a un solo usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private int calificacion;

    @Column(name = "titulo_comentario", nullable = false, length = 100)
    private String tituloComentario;

    @Column(nullable = false, length = 500)
    private String comentario;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "restaurante_slug", nullable = false)
    private String restauranteSlug;
    


    public Comentarios() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public Comentarios(Usuario usuario, int calificacion, String comentario, String restauranteSlug) {
        this.usuario = usuario;
        this.calificacion = calificacion;
        this.comentario = comentario;   
        this.restauranteSlug = restauranteSlug;
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
}

    public String getRestauranteSlug() {
        return restauranteSlug;
    }

    public void setRestauranteSlug(String restauranteSlug) {
        this.restauranteSlug = restauranteSlug;
    }

    public void setTituloComentario(String tituloComentario) {
        this.tituloComentario = tituloComentario;
    }

    public String getTituloComentario() {
        return tituloComentario;
    }
}
