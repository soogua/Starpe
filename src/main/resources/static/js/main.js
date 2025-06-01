/**
 * Foro de Calificaciones de Restaurantes - JavaScript Principal
 * 
 * Este archivo contiene la funcionalidad principal de JavaScript para el sitio web del foro de calificaciones de restaurantes.
 * Maneja la inicialización de componentes, escuchadores de eventos y manipulación del DOM.
 * Las funcionalidades clave incluyen:
 * - Inicialización del documento
 * - Manejo del formulario de búsqueda
 * - Comportamiento de los enlaces de navegación
 * 
 * @author Equipo del Foro de Calificaciones de Restaurantes
 * @version 1.0
 */

/**
 * Función de inicialización principal que se ejecuta cuando el DOM está completamente cargado.
 * Configura todos los escuchadores de eventos e inicializa la funcionalidad de la página.
 */
document.addEventListener('DOMContentLoaded', function() {
    // Inicializar cualquier componente o característica necesaria aquí
    console.log('Documento cargado y listo.');

    // Funcionalidad del formulario de reseñas
    initializeReviewForm();

    /**
     * Manejo del formulario de búsqueda
     * Actualizado para trabajar con las rutas de Thymeleaf
     */
    const searchForm = document.querySelector('form[action="/buscar"]');
    if (searchForm) {
        searchForm.addEventListener('submit', function(event) {
            const queryInput = searchForm.querySelector('input[name="q"]');
            if (queryInput && queryInput.value.trim() === '') {
                event.preventDefault();
                alert('Por favor, ingresa un término de búsqueda');
                return false;
            }
            console.log('Búsqueda enviada:', queryInput.value);
            // El formulario se enviará normalmente a /buscar
        });
    }

    /**
     * Manejo de enlaces de navegación
     * Funciona con las nuevas rutas de Spring Boot
     */
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        link.addEventListener('click', function(event) {
            const href = this.getAttribute('href');
            console.log('Navegando a:', href);
            
            // Si es un enlace con hash (#), prevenir el comportamiento por defecto
            if (href && href.startsWith('#')) {
                event.preventDefault();
                console.log('Enlace con hash detectado, funcionalidad pendiente');
            }
            // Los enlaces de Thymeleaf funcionarán normalmente
        });
    });

    /**
     * Manejo de enlaces a restaurantes específicos
     * Para mejorar la experiencia de usuario
     */
    const restaurantLinks = document.querySelectorAll('a[href*="/restaurante/"]');
    restaurantLinks.forEach(link => {
        link.addEventListener('click', function() {
            console.log('Cargando página de restaurante:', this.getAttribute('href'));
            // Aquí podrías añadir un indicador de carga si quisieras
        });
    });
});

/**
 * Inicializa la funcionalidad del formulario de reseñas
 */
function initializeReviewForm() {
    const formularioReseña = document.getElementById('formularioReseña');
    const contadorCaracteres = document.getElementById('contadorCaracteres');
    const comentarioTextarea = document.getElementById('comentario');
    const starInputs = document.querySelectorAll('input[name="calificacion"]');
    const ratingText = document.querySelector('.rating-text');

    // Contador de caracteres
    if (comentarioTextarea && contadorCaracteres) {
        comentarioTextarea.addEventListener('input', function() {
            const length = this.value.length;
            contadorCaracteres.textContent = length;
            
            if (length > 450) {
                contadorCaracteres.classList.add('contador-limite');
            } else {
                contadorCaracteres.classList.remove('contador-limite');
            }
        });
    }

    // Feedback visual para calificación por estrellas
    if (starInputs.length > 0 && ratingText) {
        starInputs.forEach(input => {
            input.addEventListener('change', function() {
                const rating = parseInt(this.value);
                const messages = {
                    1: 'Muy malo 😞',
                    2: 'Malo 😕',
                    3: 'Regular 😐',
                    4: 'Bueno 😊',
                    5: 'Excelente 🤩'
                };
                ratingText.textContent = messages[rating];
                ratingText.style.color = '#ffc107';
                ratingText.style.fontWeight = '600';
            });
        });
    }

    // Validación y envío del formulario
    if (formularioReseña) {
        formularioReseña.addEventListener('submit', function(e) {
            e.preventDefault();
            
            // Validación básica
            const nombreUsuario = document.getElementById('nombreUsuario').value.trim();
            const calificacion = document.querySelector('input[name="calificacion"]:checked');
            const comentario = document.getElementById('comentario').value.trim();
            const politica = document.getElementById('politicaPrivacidad').checked;

            if (!nombreUsuario || nombreUsuario.length < 2) {
                showError('Por favor, ingresa un nombre válido.');
                return;
            }

            if (!calificacion) {
                showError('Por favor, selecciona una calificación.');
                return;
            }

            if (!comentario || comentario.length < 10) {
                showError('Por favor, escribe un comentario de al menos 10 caracteres.');
                return;
            }

            if (!politica) {
                showError('Debes aceptar las políticas de contenido.');
                return;
            }

            // Si todo está bien, enviar el formulario
            showLoading();
            this.submit();
        });
    }
}

/**
 * Muestra un mensaje de error
 */
function showError(message) {
    const alert = document.createElement('div');
    alert.className = 'alert alert-danger alert-dismissible fade show';
    alert.innerHTML = `
        <i class="fas fa-exclamation-triangle me-2"></i>${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    const form = document.getElementById('formularioReseña');
    form.insertBefore(alert, form.firstChild);
    
    // Auto-remover después de 5 segundos
    setTimeout(() => {
        if (alert.parentNode) {
            alert.remove();
        }
    }, 5000);
}

/**
 * Muestra estado de carga en el botón
 */
function showLoading() {
    const btnPublicar = document.getElementById('btnPublicar');
    if (btnPublicar) {
        btnPublicar.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Publicando...';
        btnPublicar.disabled = true;
    }
}