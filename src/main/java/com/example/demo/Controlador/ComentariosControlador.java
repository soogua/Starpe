package com.example.demo.Controlador;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Entidad.Comentarios;
import com.example.demo.Entidad.Usuario;
import com.example.demo.Repositorio.ComentarioRepositorio;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/main")
public class ComentariosControlador {
    @Autowired
    private ComentarioRepositorio comentarioRepository;

    @PostMapping("/comentario")
    public String procesarComentario(
            @RequestParam("restauranteSlug") String restauranteSlug,
            @RequestParam("tituloComentario") String tituloComentario,
            @RequestParam("comentario") String comentarioTexto,
            @RequestParam("calificacion") int calificacion,
            HttpSession session
    ) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/main/login";
        }
        
        Comentarios comentario = new Comentarios();
        comentario.setTituloComentario(tituloComentario);
        comentario.setComentario(comentarioTexto);
        comentario.setCalificacion(calificacion);
        comentario.setRestauranteSlug(restauranteSlug);
        comentario.setUsuario(usuario);
        comentario.setFechaCreacion(LocalDateTime.now());
        
        try {
            comentarioRepository.save(comentario);
            System.out.println("Comentario guardado para: " + restauranteSlug);
        } catch (Exception e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
        
        return "redirect:/main/restaurante/" + restauranteSlug;
    }
}
