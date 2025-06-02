package com.example.demo.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.Entidad.Comentarios;

public interface ComentarioRepositorio extends JpaRepository<Comentarios, Long> {

    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar comentarios por usuario o por contenido específico

    // Ejemplo de método personalizado (si fuera necesario)
    // List<Comentarios> findByUsuarioId(Long usuarioId);   
    
}
