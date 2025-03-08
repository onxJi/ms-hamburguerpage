package com.onx.hamburgerpage.services.impl;

import com.google.cloud.firestore.Firestore;
import com.onx.hamburgerpage.models.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class FsUserDetailService implements UserDetailsService {

    private final Firestore firestore;

    public FsUserDetailService(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            // Consulta el usuario por correo electrónico en Firestore
            var document = firestore.collection("usuarios")
                    .whereEqualTo("email", email)
                    .get()
                    .get()
                    .getDocuments()
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            // Convierte el documento a un objeto Usuario
            Usuario usuario = document.toObject(Usuario.class);
            if (usuario == null) {
                throw new UsernameNotFoundException("Usuario no encontrado");
            }
            return usuario;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error al consultar Firestore", e);
        }
    }
}
