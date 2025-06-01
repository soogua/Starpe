/**
 * Controlador para la funcionalidad de inicio de sesión y registro
 * Este script maneja las interacciones del usuario con los formularios de registro e inicio de sesión,
 * incluyendo la validación de formularios y la gestión de los modales para términos y condiciones.
 */
document.addEventListener('DOMContentLoaded', function() {
    // Gestión de los modales para Términos y Condiciones y Política de Privacidad
    const termsModal = document.getElementById('termsModal');
    const privacyModal = document.getElementById('privacyModal');
    const termsCheckbox = document.getElementById('terms');
    
    /**
     * Maneja la interacción con el modal de Términos y Condiciones
     * Permite que al hacer clic en "Acepto", se marque automáticamente la casilla de verificación
     */
    if (termsModal) {
        // Obtiene el botón de aceptar en el modal de términos
        const termsAcceptBtn = document.getElementById('acceptTermsBtn');
        if (termsAcceptBtn) {
            termsAcceptBtn.addEventListener('click', function() {
                // Marca la casilla de términos cuando el usuario acepta
                if (termsCheckbox) {
                    termsCheckbox.checked = true;
                }
            });
        }
    }
    
    /**
     * Maneja la interacción con el modal de Política de Privacidad
     * También marca la casilla de verificación al aceptar la política
     */
    if (privacyModal) {
        // Obtiene el botón de aceptar en el modal de privacidad
        const privacyAcceptBtn = document.getElementById('acceptPrivacyBtn');
        if (privacyAcceptBtn) {
            privacyAcceptBtn.addEventListener('click', function() {
                // Marca la casilla de términos cuando el usuario acepta la política de privacidad
                if (termsCheckbox) {
                    termsCheckbox.checked = true;
                }
            });
        }
    }
    
    /**
     * Validación del formulario de registro
     * Verifica que:
     * 1. Las contraseñas coincidan
     * 2. La contraseña tenga al menos 8 caracteres
     * 3. Se hayan aceptado los términos y condiciones
     */
    const passwordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('confirmPassword');
    const registerForm = document.querySelector('form[action="/register"]');
    
    if (registerForm && passwordInput && confirmPasswordInput) {
        registerForm.addEventListener('submit', function(e) {
            // Validación de coincidencia de contraseñas
            if (passwordInput.value !== confirmPasswordInput.value) {
                e.preventDefault();
                alert('Las contraseñas no coinciden');
                return false;
            }
            
            // Validación de longitud de contraseña
            if (passwordInput.value.length < 8) {
                e.preventDefault();
                alert('La contraseña debe tener al menos 8 caracteres');
                return false;
            }
            
            // Verificación de aceptación de términos
            if (termsCheckbox && !termsCheckbox.checked) {
                e.preventDefault();
                alert('Debe aceptar los términos y condiciones y la política de privacidad');
                return false;
            }
        });
    }
    
    // Validación en tiempo real de coincidencia de contraseñas
    if (confirmPasswordInput) {
        confirmPasswordInput.addEventListener('input', function() {
            if (this.value !== passwordInput.value) {
                this.setCustomValidity('Las contraseñas no coinciden');
            } else {
                this.setCustomValidity('');
            }
        });
    }
});
