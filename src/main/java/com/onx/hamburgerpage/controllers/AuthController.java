package com.onx.hamburgerpage.controllers;
import com.onx.hamburgerpage.models.Usuario;
import com.onx.hamburgerpage.services.AuthService;
import com.onx.hamburgerpage.services.impl.FsUserDetailService;
import com.onx.hamburgerpage.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private FsUserDetailService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    // Registro de usuario
    @PostMapping("/public/auth/register")
    public ResponseEntity<String> register(@RequestBody Usuario usuario) throws ExecutionException, InterruptedException {
        authService.register(usuario);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    // Inicio de sesión
    @PostMapping("/public/auth/login")
    public ResponseEntity<String> login(@RequestBody Usuario usuario) {
        try {
            // Cargar el usuario desde Firestore
            UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getEmail());

            // Validar la contraseña
            if (!passwordMatches(usuario.getPassword(), userDetails.getPassword())) {
                return ResponseEntity.status(401).body("Credenciales inválidas");
            }

            // Generar el token JWT
            String token = jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(token);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }

    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello World");
    }
    private boolean passwordMatches(String rawPassword, String encodedPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
    }
}
