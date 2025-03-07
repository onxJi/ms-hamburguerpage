package com.onx.hamburgerpage.services.impl;

import com.onx.hamburgerpage.models.Usuario;
import com.onx.hamburgerpage.repositories.UsuarioRepository;
import com.onx.hamburgerpage.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Registrar un nuevo usuario
    @Override
    public void register(Usuario usuario) throws ExecutionException, InterruptedException {
        usuario.setId(UUID.randomUUID().toString());
        usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        usuario.setRol("USER"); // Por defecto, todos son usuarios normales
        usuarioRepository.save(usuario);
    }

    // Autenticar usuario
    @Override
    public Usuario authenticate(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario != null && passwordEncoder.matches(password, usuario.getPasswordHash())) {
            return usuario;
        }
        return null;
    }
}
