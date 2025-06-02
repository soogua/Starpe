package com.example.demo.Controlador;

import com.example.demo.Entidad.Usuario;
import com.example.demo.Repositorio.UsuarioRepositorio;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;


@Controller
@RequestMapping("/auth")
public class UsuarioControlador {

    @Autowired
    private UsuarioRepositorio usuarioRepo;

    // Mostrar formulario (GET)
   // @GetMapping("/login")
  //  public String mostrarLogin() {
  //      return "redirect:/login.html";  // Sirve el HTML estático
  //  }

    // Procesar login (POST)
    @PostMapping("/login")
public String verificarLogin(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,
                            Model modelo) {

    Optional<Usuario> usuario = usuarioRepo.findByEmail(email);

    if (usuario.isPresent() && usuario.get().getPassword().equals(password)) {
        session.setAttribute("usuarioLogueado", usuario.get());
        return "redirect:/auth/index"; // Puedes redirigir, ya que los datos están en sesión
    } else {
        modelo.addAttribute("error", "Email o contraseña incorrectos.");
        return "login";
    }
}

@GetMapping("/index")
public String mostrarIndex(HttpSession session, Model modelo) {
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

    if (usuario != null) {
        modelo.addAttribute("nombreusuario", usuario.getNombre());
        modelo.addAttribute("emailusuario", usuario.getEmail());
    }

    return "index";
}



    
    /*
    @GetMapping("login")
public String obtenerUsuario(@RequestParam String email, @RequestParam String password, Model modelo) {
    Optional<Usuario> usuario = usuarioRepo.findByEmail(email);
    
    if (usuario.isPresent() && usuario.get().getPassword().equals(password)) {
        modelo.addAttribute("nombreusuario", usuario.get().getNombre());
        modelo.addAttribute("emailusuario", usuario.get().getEmail());
        return "index"; // Nombre del template, NO redirecciona
    } else {
        modelo.addAttribute("error", "Email o contraseña incorrectos.");
        return "login"; // Mostrar nuevamente la vista login con error
    }
    }

     */

}

