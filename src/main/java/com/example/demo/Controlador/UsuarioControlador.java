package com.example.demo.Controlador;

import com.example.demo.Entidad.Usuario;
import com.example.demo.Repositorio.UsuarioRepositorio;
import com.example.demo.modelo.Comentario;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import com.example.demo.Entidad.Comentarios;
import com.example.demo.Repositorio.ComentarioRepositorio;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/auth")
public class UsuarioControlador {

    @Autowired
    private UsuarioRepositorio usuarioRepo;
    @Autowired
    private ComentarioRepositorio comentarioRepository;

    // Mostrar formulario (GET)
   // @GetMapping("/login")
  //  public String mostrarLogin() {
  //      return "redirect:/login.html";  // Sirve el HTML estático
  //  }

    @PostMapping("/registrar")
    public String registrarUsuario(@RequestParam("firstName") String firstName,
                                @RequestParam("lastName") String lastName,
                                @RequestParam("email") String email,
                                @RequestParam("password") String password,
                                @RequestParam("confirmPassword") String confirmPassword,
                                Model model) {

        // Validaciones básicas
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "registro"; // Nombre del HTML del formulario
        }

        // Aquí guardarías el usuario en la base de datos (servicio o repositorio)
        // usuarioService.guardar(new Usuario(...));

        // Redireccionar al login o a otra vista
        return "redirect:/login";
    }


    @PostMapping("/procesarRegistro")
public String procesarRegistro(
        @RequestParam("firstName") String firstName,
        @RequestParam("lastName") String lastName,
        @RequestParam("email") String email,
        @RequestParam("password") String password,
        @RequestParam("confirmPassword") String confirmPassword,
        @RequestParam(value = "terms", defaultValue = "false") boolean terms,
        Model model) {

    
    // Validar contraseñas
    if (!password.equals(confirmPassword)) {
        model.addAttribute("error", "Las contraseñas no coinciden");
        return "pages/register";
    }

    // Validar términos
    if (!terms) {
        model.addAttribute("error", "Debes aceptar los términos y condiciones");
        return "pages/register";
    }

    // Validar si ya existe el correo
    if (usuarioRepo.existsByEmail(email)) {
        model.addAttribute("error", "Este correo ya está registrado");
        return "pages/register";
    }

    // Crear y guardar el usuario
    Usuario usuario = new Usuario();
    usuario.setNombre(firstName);
    //usuario.setApellido(lastName);
    usuario.setEmail(email);
    usuario.setPassword(password); // Recomendado: encriptar antes de guardar

    usuarioRepo.save(usuario);

    // Pasar datos a la vista de éxito
    model.addAttribute("nombreCompleto", firstName + " " + lastName);
    model.addAttribute("email", email);

    return "pages/registro-exitoso"; // Nombre del HTML de éxito
}




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

    @GetMapping("/restaurante/el-fundo")
    public String elFundo(Model model, HttpSession session) {
        String restauranteSlug = "el-fundo";

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario != null) {
        model.addAttribute("nombreusuario", usuario.getNombre());
        model.addAttribute("emailusuario", usuario.getEmail());
        }

        model.addAttribute("nombreRestaurante", "El Fundo");
        
        // Datos del restaurante El Fundo
        model.addAttribute("nombreRestaurante", "El Fundo");
        model.addAttribute("ranking", "#1 de 198 restaurantes en Ica");
        model.addAttribute("categoria", "Peruana, Saludable");
        model.addAttribute("precio", "$$ - $$$");
        model.addAttribute("horario", "Abierto hasta las 10:00 PM");
        model.addAttribute("direccion", "Av. José Matias Manzanilla 479, Ica 11004 Perú");
        model.addAttribute("telefono", "+ 51 977 860 672");
        model.addAttribute("paginaWeb", "http://www.fundolaspalmeras.com/");
        model.addAttribute("descripcion", "Lugar fuera del centro de Ica, se caracteriza por ser amplio, con techos altos para una muy buena ventilación, la atención es excelente desde la llegada hasta la partida, la comida tanto marina como criolla de lo mejor, el restaurante cuenta con estacionamiento propio y seguro.");
        
        // Imágenes específicas del restaurante
        model.addAttribute("imagenes", new String[]{
            "/imagenes/ElFundo/fundo5.jpg",
            "/imagenes/ElFundo/fundo2.jpg",
            "/imagenes/ElFundo/fundo3.jpg",
            "/imagenes/ElFundo/fundo4.jpg"
        });
        
        // Agregar datos dinámicos de comentarios



        /*
        List<Comentario> comentarios = comentarioService.obtenerComentarios(restauranteSlug);
        double calificacionPromedio = comentarioService.calcularCalificacionPromedio(restauranteSlug);
        int totalOpiniones = comentarioService.obtenerTotalOpiniones(restauranteSlug);
        
        model.addAttribute("comentarios", comentarios);
        model.addAttribute("calificacion", calificacionPromedio);
        model.addAttribute("calificacionFormateada", String.format("%.1f", calificacionPromedio));
        model.addAttribute("totalOpiniones", totalOpiniones);

         */
        
        return "restaurantes/detalle-restaurante";
    }

}

