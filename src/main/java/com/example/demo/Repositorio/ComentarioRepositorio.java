package com.example.demo.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.Entidad.Comentarios;
import java.util.List;

public interface ComentarioRepositorio extends JpaRepository<Comentarios, Long> {
    
    // Método para buscar comentarios por slug de restaurante
    List<Comentarios> findByRestauranteSlug(String restauranteSlug);
    
    // Método adicional para ordenar por fecha (más recientes primero)
    List<Comentarios> findByRestauranteSlugOrderByFechaCreacionDesc(String restauranteSlug);
}
