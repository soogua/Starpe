package com.example.demo.Controlador;

import com.example.demo.Entidad.Usuario;
import com.example.demo.modelo.Comentario;
import com.example.demo.servicio.ComentarioService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; // Indica que esta clase es un controlador en Spring MVC
import org.springframework.ui.Model; // El modelo se usa para pasar datos desde el controlador a la vista (Thymeleaf)
import org.springframework.web.bind.annotation.GetMapping; // Se usa para mapear las solicitudes HTTP GET
import org.springframework.web.bind.annotation.PostMapping; // Se usa para mapear las solicitudes HTTP POST
import org.springframework.web.bind.annotation.RequestParam; // Captura los parámetros enviados en la solicitud HTTP
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class Controlador {
    
    @Autowired
    private ComentarioService comentarioService;
    
    // Página principal con ranking dinámico
    @GetMapping("/")
    public String index(Model model) {
        // Obtener top 3 restaurantes dinámicamente
        List<String> top3 = comentarioService.obtenerTop3Restaurantes();
        
        // Crear lista con información completa de cada restaurante
        List<Map<String, Object>> restaurantesTop3 = top3.stream()
                .map(comentarioService::obtenerInfoRestauranteParaCarrusel)
                .collect(Collectors.toList());
        
        model.addAttribute("restaurantesTop3", restaurantesTop3);
        return "index";
    }
    
    // Página principal alternativa
    @GetMapping("/index")
    public String indexAlternativo() {
        return "index";
    }
    
    // Página de login
    @GetMapping("/login")
    public String mostrarLogin() {
        return "pages/login";
    }
    
    // Procesar login
    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model) {
        // Aquí irá la lógica de autenticación
        // Por ahora, simulamos login exitoso
        model.addAttribute("email", email);
        return "pages/dashboard"; // Página después del login
    }
    
    // Muestra la página de registro
    @GetMapping("/register")
public String mostrarRegistro(Model model) {
    model.addAttribute("usuario", new Usuario());
    return "pages/register";
}
    
    // Procesa el envío del formulario de registro
    @PostMapping("/registerrrrr")
    public String procesarRegistro(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam(value = "terms", defaultValue = "false") boolean terms,
            Model model) {
        
        // Validar datos
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "pages/register";
        }
        
        if (!terms) {
            model.addAttribute("error", "Debes aceptar los términos y condiciones");
            return "pages/register";
        }
        
        // Si todo está correcto, añadir datos al modelo y redirigir a la página de éxito
        model.addAttribute("nombreCompleto", firstName + " " + lastName);
        model.addAttribute("email", email);
        // Aquí añadirías la lógica para guardar el usuario en la base de datos
        
        return "pages/registro-exitoso"; 
    }
    
    // Páginas de exploración
    @GetMapping("/explorar")
    public String explorar(Model model) {
        // Aquí cargarías la lista de restaurantes desde la base de datos
        model.addAttribute("titulo", "Explorar Restaurantes");
        return "pages/explorar";
    }
    
    @GetMapping("/mejor-valorados")
    public String mejorValorados(Model model) {
        // Aquí cargarías los restaurantes mejor valorados
        model.addAttribute("titulo", "Mejor Valorados");
        return "pages/mejor-valorados";
    }
    
    @GetMapping("/cerca-de-mi")
    public String cercaDeMi(Model model) {
        // Aquí cargarías restaurantes cercanos
        model.addAttribute("titulo", "Cerca de Mí");
        return "pages/cerca-de-mi";
    }
    
    // Búsqueda
    @GetMapping("/buscar")
    public String buscar(@RequestParam(value = "q", required = false) String query, Model model) {
        if (query != null && !query.isEmpty()) {
            // Aquí irá la lógica de búsqueda
            model.addAttribute("query", query);
            model.addAttribute("resultados", "Resultados para: " + query);
        }
        return "pages/buscar";
    }
    
    // Rutas para restaurantes específicos
    @GetMapping("/restaurante/el-fundo")
    public String elFundo(Model model) {
        String restauranteSlug = "el-fundo";
        
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
        List<Comentario> comentarios = comentarioService.obtenerComentarios(restauranteSlug);
        double calificacionPromedio = comentarioService.calcularCalificacionPromedio(restauranteSlug);
        int totalOpiniones = comentarioService.obtenerTotalOpiniones(restauranteSlug);
        
        model.addAttribute("comentarios", comentarios);
        model.addAttribute("calificacion", calificacionPromedio);
        model.addAttribute("calificacionFormateada", String.format("%.1f", calificacionPromedio));
        model.addAttribute("totalOpiniones", totalOpiniones);
        
        return "restaurantes/detalle-restaurante";
    }
    
    @GetMapping("/restaurante/chifa-yemheng")
    public String chifaYemheng(Model model) {
        String restauranteSlug = "chifa-yemheng";
        
        // Datos del restaurante Chifa Yemheng
        model.addAttribute("nombreRestaurante", "Chifa Yemheng");
        model.addAttribute("ranking", "#2 de 198 restaurantes en Ica");
        model.addAttribute("categoria", "China, Peruana");
        model.addAttribute("precio", "$$ - $$$");
        model.addAttribute("horario", "Abierto hasta las 10:00 PM");
        model.addAttribute("direccion", "Av. José Matias Manzanilla 479, Ica 11004 Perú");
        model.addAttribute("telefono", "+ 51 977 860 672");
        model.addAttribute("paginaWeb", "#");
        model.addAttribute("descripcion", "En Chifa Yemheng fusionamos lo mejor de la cocina china y peruana para ofrecerte platos sabrosos, frescos y llenos de tradición. Ubicados en Ica, nos esforzamos por brindar un servicio cálido y una experiencia gastronómica inolvidable para toda la familia.");
        
        // Imágenes específicas del restaurante
        model.addAttribute("imagenes", new String[]{
            "/imagenes/Yemheng/yem4.jpg",
            "/imagenes/Yemheng/Yem5.jpg",
            "/imagenes/Yemheng/yem1.jpg",
            "/imagenes/Yemheng/yem3.jpg",
            "/imagenes/Yemheng/yem2.jpg"
        });
        
        // Agregar datos dinámicos de comentarios
        List<Comentario> comentarios = comentarioService.obtenerComentarios(restauranteSlug);
        double calificacionPromedio = comentarioService.calcularCalificacionPromedio(restauranteSlug);
        int totalOpiniones = comentarioService.obtenerTotalOpiniones(restauranteSlug);
        
        model.addAttribute("comentarios", comentarios);
        model.addAttribute("calificacion", calificacionPromedio);
        model.addAttribute("calificacionFormateada", String.format("%.1f", calificacionPromedio));
        model.addAttribute("totalOpiniones", totalOpiniones);
        
        return "restaurantes/detalle-restaurante";
    }
    
    @GetMapping("/restaurante/cordon-y-la-rosa")
    public String cordonYLaRosa(Model model) {
        String restauranteSlug = "cordon-y-la-rosa";
        
        // Datos del restaurante El Cordón y la Rosa
        model.addAttribute("nombreRestaurante", "El Cordón y la Rosa");
        model.addAttribute("ranking", "#3 de 198 restaurantes en Ica");
        model.addAttribute("categoria", "Peruana, Internacional");
        model.addAttribute("precio", "$$ - $$$");
        model.addAttribute("horario", "Abierto hasta las 10:00 PM");
        model.addAttribute("direccion", "Av. José Matias Manzanilla 479, Ica 11004 Perú");
        model.addAttribute("telefono", "+51 1 786030");
        model.addAttribute("paginaWeb", "https://elcordonylarosa.pe/");
        model.addAttribute("descripcion", "El Cordón y la Rosa es un restaurante con más de 12 años de experiencia en el sector gastronómico y considerado uno de los mejores restaurantes de la región. Te invitamos a visitarnos en compañía de tus seres queridos, degustar los mejores platos a la carta, bebidas y postres, un servicio de primer nivel y un ambiente único.");
        
        // Imágenes específicas del restaurante
        model.addAttribute("imagenes", new String[]{
            "/imagenes/CordonylaRosa/cordon1.jpg",
            "/imagenes/CordonylaRosa/cordon2.jpg",
            "/imagenes/CordonylaRosa/cordon3.jpg",
            "/imagenes/CordonylaRosa/cordon4.jpg"
        });
        
        // Agregar datos dinámicos de comentarios
        List<Comentario> comentarios = comentarioService.obtenerComentarios(restauranteSlug);
        double calificacionPromedio = comentarioService.calcularCalificacionPromedio(restauranteSlug);
        int totalOpiniones = comentarioService.obtenerTotalOpiniones(restauranteSlug);
        
        model.addAttribute("comentarios", comentarios);
        model.addAttribute("calificacion", calificacionPromedio);
        model.addAttribute("calificacionFormateada", String.format("%.1f", calificacionPromedio));
        model.addAttribute("totalOpiniones", totalOpiniones);
        
        return "restaurantes/detalle-restaurante";
    }
    
    @GetMapping("/restaurante/norkys")
    public String norkys(Model model) {
        String restauranteSlug = "norkys";
        
        // Datos del restaurante Norkys
        model.addAttribute("nombreRestaurante", "Pollería Norkys");
        model.addAttribute("ranking", "#4 de 198 restaurantes en Ica");
        model.addAttribute("categoria", "Peruana, Pollo a la brasa");
        model.addAttribute("precio", "$$ - $$$");
        model.addAttribute("horario", "Abierto hasta las 10:00 PM");
        model.addAttribute("direccion", "Av. José Matias Manzanilla 479, Ica 11004 Perú");
        model.addAttribute("telefono", "+51 1 283030");
        model.addAttribute("paginaWeb", "https://www.norkys.pe/");
        model.addAttribute("descripcion", "El Restaurante Norkys es una cadena peruana de comida rápida fundada en 1986, especializada en pollo a la brasa y otros platos populares como parrilladas, hamburguesas, salchipapas y anticuchos, destacando por su sabor casero, precios accesibles y servicio rápido. Con su eslogan \"Norkys, como en casa\", la marca se ha expandido en Lima y otras ciudades, ofreciendo un ambiente familiar e incluso áreas de juegos para niños en algunos locales, consolidándose como una opción reconocida en el rubro de comida al paso en Perú.");
        
        // Imágenes específicas del restaurante
        model.addAttribute("imagenes", new String[]{
            "/imagenes/norkys/norkys1.jpg",
            "/imagenes/norkys/norkys2.jpg"
        });
        
        // Agregar datos dinámicos de comentarios
        List<Comentario> comentarios = comentarioService.obtenerComentarios(restauranteSlug);
        double calificacionPromedio = comentarioService.calcularCalificacionPromedio(restauranteSlug);
        int totalOpiniones = comentarioService.obtenerTotalOpiniones(restauranteSlug);
        
        model.addAttribute("comentarios", comentarios);
        model.addAttribute("calificacion", calificacionPromedio);
        model.addAttribute("calificacionFormateada", String.format("%.1f", calificacionPromedio));
        model.addAttribute("totalOpiniones", totalOpiniones);
        
        return "restaurantes/detalle-restaurante";
    }
    
    // POST endpoint para agregar comentarios
    @PostMapping("/restaurante/{restaurante}/comentario")
    public String agregarComentario(
            @PathVariable("restaurante") String restaurante,
            @RequestParam("nombreUsuario") String nombreUsuario,
            @RequestParam("calificacion") int calificacion,
            @RequestParam("comentario") String comentarioTexto,
            Model model) {
        
        // Crear y guardar el comentario
        Comentario nuevoComentario = new Comentario(nombreUsuario, calificacion, comentarioTexto, restaurante);
        comentarioService.agregarComentario(restaurante, nuevoComentario);
        
        // Redireccionar de vuelta a la página del restaurante
        return "redirect:/restaurante/" + restaurante + "?comentario=agregado";
    }
}

