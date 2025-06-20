package com.example.demo.Repositorio;

import com.example.demo.Entidad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {


    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}
