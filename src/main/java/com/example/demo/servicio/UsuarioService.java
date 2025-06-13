package com.example.demo.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
// import com.example.demo.modelo.Entidad.Usuario;
import com.example.demo.Repositorio.UsuarioRepositorio; 
// import com.example.demo.modelo.Usuario; 
import com.example.demo.Entidad.Usuario;

@Service
public class UsuarioService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepositorio usuarioRepository;

    public void registerUser(Usuario usuario) {
        String hashedPassword = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(hashedPassword);
        // Save to database
        usuarioRepository.save(usuario);
    }
}
