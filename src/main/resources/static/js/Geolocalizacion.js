/**
 * Geolocalizacion.js
 * 
 * Este archivo maneja toda la funcionalidad relacionada con la ubicación del usuario:
 * - Solicitar y gestionar permisos de geolocalización
 * - Almacenar datos de ubicación en sessionStorage
 * - Calcular distancias entre la ubicación del usuario y los restaurantes
 * - Actualizar la interfaz de usuario según el estado de la ubicación
 */

document.addEventListener('DOMContentLoaded', function() {
    const locationAlert = document.getElementById('locationAlert');
    const enableLocationBtn = document.getElementById('enableLocation');
    
    // Verificar si ya hay datos de geolocalización almacenados en la sesión
    checkStoredLocation();
    
    // Configurar el evento para el botón de habilitar ubicación
    if (enableLocationBtn) {
        enableLocationBtn.addEventListener('click', requestUserLocation);
    }
    
    /**
     * Solicita la ubicación del usuario utilizando la API de Geolocalización
     * Si el navegador soporta geolocalización, se solicita la posición del usuario
     */
    function requestUserLocation() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                handleLocationSuccess,
                handleLocationError
            );
        } else {
            // El navegador no soporta la geolocalización
            updateLocationAlert(
                'warning',
                '<i class="fas fa-exclamation-triangle me-2"></i>' +
                '<strong>Tu navegador no soporta geolocalización.</strong> Las distancias mostradas son aproximadas.'
            );
        }
    }
    
    /**
     * Maneja la respuesta exitosa al obtener la ubicación del usuario
     * @param {GeolocationPosition} position - Objeto de posición proporcionado por la API de Geolocalización
     */
    function handleLocationSuccess(position) {
        // Almacenar datos de ubicación en sessionStorage para uso futuro
        const locationData = {
            latitude: position.coords.latitude,
            longitude: position.coords.longitude,
            timestamp: new Date().getTime()
        };
        
        sessionStorage.setItem('userLocation', JSON.stringify(locationData));
        
        // Actualizar la interfaz para mostrar mensaje de éxito
        updateLocationAlert(
            'success',
            '<i class="fas fa-check-circle me-2"></i>' +
            '<strong>¡Ubicación activada!</strong> Ahora te mostraremos los restaurantes más cercanos.'
        );
        
        // Actualizar las distancias de los restaurantes según la ubicación del usuario
        updateRestaurantDistances(position.coords.latitude, position.coords.longitude);
    }
    
    /**
     * Maneja los errores al intentar obtener la ubicación del usuario
     * @param {GeolocationPositionError} error - Error generado por la API de Geolocalización
     */
    function handleLocationError(error) {
        console.error('Error al obtener la ubicación:', error);
        
        let errorMessage = '<strong>No pudimos acceder a tu ubicación.</strong> ';
        
        // Proporcionar mensaje específico según el tipo de error
        switch(error.code) {
            case error.PERMISSION_DENIED:
                errorMessage += 'Has denegado el permiso de ubicación.';
                break;
            case error.POSITION_UNAVAILABLE:
                errorMessage += 'La información de ubicación no está disponible.';
                break;
            case error.TIMEOUT:
                errorMessage += 'Se agotó el tiempo para obtener la ubicación.';
                break;
            case error.UNKNOWN_ERROR:
                errorMessage += 'Ocurrió un error desconocido.';
                break;
        }
        
        updateLocationAlert(
            'warning',
            '<i class="fas fa-exclamation-triangle me-2"></i>' +
            errorMessage + ' Las distancias mostradas son aproximadas.'
        );
    }
    
    /**
     * Actualiza el elemento de alerta de ubicación con un estilo y mensaje apropiados
     * @param {string} alertType - Tipo de alerta (success, warning, danger, info)
     * @param {string} message - Contenido HTML del mensaje a mostrar
     */
    function updateLocationAlert(alertType, message) {
        if (locationAlert) {
            locationAlert.innerHTML = message + 
                '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>';
            
            // Restablecer todas las clases de alerta
            locationAlert.classList.remove('alert-info', 'alert-success', 'alert-warning', 'alert-danger');
            // Añadir la clase de alerta específica
            locationAlert.classList.add(`alert-${alertType}`);
        }
    }
    
    /**
     * Verifica si ya hay datos de ubicación almacenados y actualiza la interfaz
     * @returns {boolean} - True si se encontraron y utilizaron datos de ubicación válidos
     */
    function checkStoredLocation() {
        const storedLocation = sessionStorage.getItem('userLocation');
        
        if (storedLocation) {
            try {
                const locationData = JSON.parse(storedLocation);
                const timestamp = locationData.timestamp;
                const currentTime = new Date().getTime();
                
                // Si los datos de ubicación tienen menos de 30 minutos, los utilizamos
                if (currentTime - timestamp < 30 * 60 * 1000) {
                    // Actualizar la interfaz para mostrar que ya tenemos la ubicación
                    updateLocationAlert(
                        'success',
                        '<i class="fas fa-check-circle me-2"></i>' +
                        '<strong>¡Ubicación activa!</strong> Estamos mostrando los restaurantes más cercanos a ti.'
                    );
                    
                    // Actualizar las distancias de los restaurantes
                    updateRestaurantDistances(locationData.latitude, locationData.longitude);
                    return true;
                }
            } catch (e) {
                console.error('Error parsing stored location data', e);
            }
        }
        
        return false;
    }
    
    /**
     * Actualiza las distancias mostradas en las tarjetas de restaurantes
     * @param {number} latitude - Latitud de la ubicación del usuario
     * @param {number} longitude - Longitud de la ubicación del usuario
     */
    function updateRestaurantDistances(latitude, longitude) {
        const restaurantCards = document.querySelectorAll('.card[data-latitude][data-longitude]');
        
        restaurantCards.forEach(card => {
            const restaurantLat = parseFloat(card.dataset.latitude);
            const restaurantLng = parseFloat(card.dataset.longitude);
            const restaurantName = card.dataset.name || 'Restaurante';
            
            if (!isNaN(restaurantLat) && !isNaN(restaurantLng)) {
                const distance = calculateDistance(latitude, longitude, restaurantLat, restaurantLng);
                
                // Encuentra el elemento que muestra la información de distancia
                const distanceElement = card.querySelector('.card-text.text-muted.small:has(i.fa-map-marker-alt)');
                if (distanceElement) {
                    distanceElement.innerHTML = `<i class="fas fa-map-marker-alt me-2"></i>A ${distance.toFixed(1)} km de ti`;
                }
                
                console.log(`Distancia a ${restaurantName}: ${distance.toFixed(2)} km`);
            }
        });
    }
    
    /**
     * Calcula la distancia entre dos coordenadas geográficas usando la fórmula de Haversine
     * @param {number} lat1 - Latitud del punto 1 (usuario)
     * @param {number} lon1 - Longitud del punto 1 (usuario)
     * @param {number} lat2 - Latitud del punto 2 (restaurante)
     * @param {number} lon2 - Longitud del punto 2 (restaurante)
     * @returns {number} - Distancia en kilómetros entre los dos puntos
     */
    function calculateDistance(lat1, lon1, lat2, lon2) {
        const R = 6371; // Radio de la Tierra en km
        const dLat = deg2rad(lat2 - lat1);
        const dLon = deg2rad(lon2 - lon1);
        const a = 
            Math.sin(dLat/2) * Math.sin(dLat/2) +
            Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
            Math.sin(dLon/2) * Math.sin(dLon/2); 
        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
        const distance = R * c; // Distancia en km
        return distance;
    }
    
    /**
     * Convierte grados a radianes, necesario para cálculos geográficos
     * @param {number} deg - Ángulo en grados
     * @returns {number} - Ángulo en radianes
     */
    function deg2rad(deg) {
        return deg * (Math.PI/180);
    }
});