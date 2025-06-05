package com.example.demo.servicio;

import com.example.demo.Entidad.Comentarios;
import com.example.demo.Repositorio.ComentarioRepositorio;
import com.example.demo.modelo.Comentario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComentariosBDService {
    
    @Autowired
    private ComentarioRepositorio comentarioRepositorio;
    
    /**
     * Obtener comentarios de un restaurante desde la base de datos
     */
    public List<Comentario> obtenerComentariosPorRestaurante(String restauranteSlug) {
    System.out.println("=== DEBUG SERVICIO BD ===");
    System.out.println("Buscando comentarios para slug: " + restauranteSlug);
    
    List<Comentarios> comentariosBD = comentarioRepositorio.findByRestauranteSlug(restauranteSlug);
    
    System.out.println("Comentarios encontrados en BD: " + comentariosBD.size());
    for (Comentarios c : comentariosBD) {
        System.out.println("- ID: " + c.getId() + ", Usuario: " + c.getUsuario().getNombre() + ", Slug: " + c.getRestauranteSlug());
    }
    
    // Convertir entidades a modelo para el frontend
    List<Comentario> resultado = comentariosBD.stream()
            .map(this::convertirAModelo)
            .collect(Collectors.toList());
    
    System.out.println("Comentarios convertidos: " + resultado.size());
    
    return resultado;
}
    
    /**
     * Convertir entidad Comentarios a modelo Comentario
     */
    private Comentario convertirAModelo(Comentarios entidad) {
    Comentario modelo = new Comentario();
    modelo.setNombreUsuario(entidad.getUsuario().getNombre());
    modelo.setTituloComentario(entidad.getTituloComentario()); // Agregar esta línea
    modelo.setCalificacion(entidad.getCalificacion());
    modelo.setComentario(entidad.getComentario());
    modelo.setFecha(entidad.getFechaCreacion());
    modelo.setRestaurante(entidad.getRestauranteSlug());
    return modelo;
}
    
    /**
     * Calcular calificación promedio
     */
    public double calcularCalificacionPromedio(String restauranteSlug) {
        List<Comentarios> comentarios = comentarioRepositorio.findByRestauranteSlug(restauranteSlug);
        if (comentarios.isEmpty()) {
            return 0.0;
        }
        return comentarios.stream()
                .mapToInt(Comentarios::getCalificacion)
                .average()
                .orElse(0.0);
    }
    
    /**
     * Obtener total de opiniones
     */
    public int obtenerTotalOpiniones(String restauranteSlug) {
        return comentarioRepositorio.findByRestauranteSlug(restauranteSlug).size();
    }
}
