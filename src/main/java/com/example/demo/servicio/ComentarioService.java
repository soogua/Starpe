package com.example.demo.servicio;

import com.example.demo.modelo.Comentario;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ComentarioService {
    
    // Simulamos una base de datos en memoria
    private final Map<String, List<Comentario>> comentariosPorRestaurante = new HashMap<>();
    
    public ComentarioService() {
        // Inicializar con algunos comentarios de ejemplo
        inicializarComentarios();
    }
    
    /**
     * Agregar un nuevo comentario a un restaurante
     */
    public void agregarComentario(String restaurante, Comentario comentario) {
        comentario.setRestaurante(restaurante);
        comentariosPorRestaurante.computeIfAbsent(restaurante, k -> new ArrayList<>()).add(comentario);
    }
    
    /**
     * Obtener comentarios de un restaurante específico
     */
    public List<Comentario> obtenerComentarios(String restaurante) {
        return comentariosPorRestaurante.getOrDefault(restaurante, new ArrayList<>())
                .stream()
                .sorted((c1, c2) -> c2.getFecha().compareTo(c1.getFecha())) // Más recientes primero
                .collect(Collectors.toList());
    }
    
    /**
     * Calcular calificación promedio de un restaurante
     * Retorna 0 si no hay comentarios
     */
    public double calcularCalificacionPromedio(String restaurante) {
        List<Comentario> comentarios = comentariosPorRestaurante.getOrDefault(restaurante, new ArrayList<>());
        if (comentarios.isEmpty()) {
            return 0.0; // Calificación 0 si no hay comentarios
        }
        return comentarios.stream()
                .mapToInt(Comentario::getCalificacion)
                .average()
                .orElse(0.0);
    }
    
    /**
     * Obtener total de opiniones de un restaurante
     */
    public int obtenerTotalOpiniones(String restaurante) {
        return comentariosPorRestaurante.getOrDefault(restaurante, new ArrayList<>()).size();
    }
    
    /**
     * Obtener fecha del comentario más reciente de un restaurante
     */
    public LocalDateTime obtenerFechaComentarioMasReciente(String restaurante) {
        List<Comentario> comentarios = comentariosPorRestaurante.getOrDefault(restaurante, new ArrayList<>());
        return comentarios.stream()
                .map(Comentario::getFecha)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.MIN); // Fecha muy antigua si no hay comentarios
    }
    
    /**
     * Obtener ranking de restaurantes por calificación con criterios de desempate
     */
    public List<String> obtenerRankingRestaurantes() {
        Set<String> restaurantes = Set.of("el-fundo", "chifa-yemheng", "cordon-y-la-rosa", "norkys");
        
        return restaurantes.stream()
                .sorted((r1, r2) -> {
                    // 1. Comparar por calificación promedio (descendente)
                    double calificacion1 = calcularCalificacionPromedio(r1);
                    double calificacion2 = calcularCalificacionPromedio(r2);
                    
                    int comparacionCalificacion = Double.compare(calificacion2, calificacion1);
                    if (comparacionCalificacion != 0) {
                        return comparacionCalificacion;
                    }
                    
                    // 2. Si hay empate en calificación, comparar por cantidad de comentarios (descendente)
                    int comentarios1 = obtenerTotalOpiniones(r1);
                    int comentarios2 = obtenerTotalOpiniones(r2);
                    
                    int comparacionComentarios = Integer.compare(comentarios2, comentarios1);
                    if (comparacionComentarios != 0) {
                        return comparacionComentarios;
                    }
                    
                    // 3. Si hay empate en cantidad, comparar por fecha del comentario más reciente (descendente)
                    LocalDateTime fecha1 = obtenerFechaComentarioMasReciente(r1);
                    LocalDateTime fecha2 = obtenerFechaComentarioMasReciente(r2);
                    
                    return fecha2.compareTo(fecha1);
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Obtener top 3 restaurantes para el carrusel
     */
    public List<String> obtenerTop3Restaurantes() {
        return obtenerRankingRestaurantes().stream()
                .limit(3)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtener información completa de un restaurante para el carrusel
     */
    public Map<String, Object> obtenerInfoRestauranteParaCarrusel(String restaurante) {
        Map<String, Object> info = new HashMap<>();
        double calificacion = calcularCalificacionPromedio(restaurante);
        int totalOpiniones = obtenerTotalOpiniones(restaurante);
        
        // Información básica del restaurante
        switch (restaurante) {
            case "el-fundo":
                info.put("nombre", "El Fundo");
                info.put("descripcion", "Excelente ventilación y la mejor comida marina y criolla de Ica");
                info.put("imagen", "/imagenes/ElFundo/fundo5.jpg");
                break;
            case "chifa-yemheng":
                info.put("nombre", "Chifa Yemheng");
                info.put("descripcion", "La perfecta fusión china-peruana con tradición y sabor único");
                info.put("imagen", "/imagenes/Yemheng/yem4.jpg");
                break;
            case "cordon-y-la-rosa":
                info.put("nombre", "El Cordón y la Rosa");
                info.put("descripcion", "12 años de experiencia gastronómica y servicio de primer nivel");
                info.put("imagen", "/imagenes/CordonylaRosa/cordon1.jpg");
                break;
            case "norkys":
                info.put("nombre", "Pollería Norkys");
                info.put("descripcion", "Especialistas en pollo a la brasa con sabor casero y precios accesibles");
                info.put("imagen", "/imagenes/norkys/norkys1.jpg");
                break;
        }
        
        info.put("calificacion", calificacion);
        info.put("totalOpiniones", totalOpiniones);
        info.put("slug", restaurante);
        
        return info;
    }
    
    /**
     * Inicializar comentarios de ejemplo
     */
    private void inicializarComentarios() {
        // El Fundo - Mejor calificado
        agregarComentario("el-fundo", new Comentario("María González", 5, "Excelente comida marina, muy fresco todo.", "el-fundo"));
        agregarComentario("el-fundo", new Comentario("Carlos Ruiz", 5, "El mejor ceviche de Ica, sin duda.", "el-fundo"));
        agregarComentario("el-fundo", new Comentario("Ana Torres", 5, "Muy buena atención y ambiente familiar.", "el-fundo"));
        agregarComentario("el-fundo", new Comentario("Luis Martín", 4, "Excelente lugar, volveré pronto.", "el-fundo"));
        
        // Chifa Yemheng - Segundo lugar
        agregarComentario("chifa-yemheng", new Comentario("Pedro Sánchez", 5, "Auténtica fusión chino-peruana, delicioso.", "chifa-yemheng"));
        agregarComentario("chifa-yemheng", new Comentario("Lucía Mendoza", 5, "El arroz chaufa es espectacular.", "chifa-yemheng"));
        agregarComentario("chifa-yemheng", new Comentario("Roberto Kim", 4, "Buen sabor, porciones generosas.", "chifa-yemheng"));
        
        // El Cordón y la Rosa - Tercer lugar
        agregarComentario("cordon-y-la-rosa", new Comentario("Elena Vargas", 5, "Servicio impecable, ambiente elegante.", "cordon-y-la-rosa"));
        agregarComentario("cordon-y-la-rosa", new Comentario("Miguel Herrera", 4, "Muy buena experiencia gastronómica.", "cordon-y-la-rosa"));
        agregarComentario("cordon-y-la-rosa", new Comentario("Carmen Silva", 4, "Los postres son exquisitos.", "cordon-y-la-rosa"));
        
        // Norkys - Cuarto lugar
        agregarComentario("norkys", new Comentario("José López", 4, "Buen pollo a la brasa, precios justos.", "norkys"));
        agregarComentario("norkys", new Comentario("Patricia Rojas", 4, "Rápido y sabroso, ideal para familia.", "norkys"));
    }
}
