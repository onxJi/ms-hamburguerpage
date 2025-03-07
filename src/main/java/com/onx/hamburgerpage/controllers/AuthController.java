package com.onx.hamburgerpage.controllers;
import com.onx.hamburgerpage.models.Usuario;
import com.onx.hamburgerpage.services.AuthService;
import com.onx.hamburgerpage.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    // Registro de usuario
    @PostMapping("/public/auth/register")
    public ResponseEntity<String> register(@RequestBody Usuario usuario) throws ExecutionException, InterruptedException {
        authService.register(usuario);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    // Inicio de sesión
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario usuario) {
        Usuario foundUser = authService.authenticate(usuario.getEmail(), usuario.getPasswordHash());
        if (foundUser != null) {
            String token = jwtUtil.generateToken(foundUser.getEmail());
            return ResponseEntity.ok(token); // Devuelve el token JWT
        }
        return ResponseEntity.status(401).body("Credenciales inválidas");
    }
}
