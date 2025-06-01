package com.example.demo.Controlador;

import com.example.demo.Entidad.Usuario;
import com.example.demo.Repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class UsuarioControlador {

    @Autowired
    private UsuarioRepositorio usuarioRepo;

    // Mostrar formulario (GET)
    @GetMapping("/login")
    public String mostrarLogin() {
        return "redirect:/login.html";  // Sirve el HTML estático
    }

    // Procesar login (POST)
    @PostMapping("/login")
    @ResponseBody  // Devuelve texto/JSON en lugar de vista
    public String verificarLogin(
            @RequestParam String email,
            @RequestParam String password) {

        Optional<Usuario> usuario = usuarioRepo.findByEmail(email);

        if (usuario.isPresent() && usuario.get().getPassword().equals(password)) {
            return "¡Bienvenido! Credenciales válidas.";
        } else {
            return "Error: Email o contraseña incorrectos.";
        }
    }
}

