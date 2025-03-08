package com.onx.hamburgerpage.repositories;

import com.onx.hamburgerpage.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class UsuarioRepository {
    @Autowired
    private FirestoreService firestoreService;

    private static final String COLLECTION = "usuarios";

    public void save(Usuario usuario) throws ExecutionException, InterruptedException {
        firestoreService.save(COLLECTION, usuario.getId(), usuario);
    }

    public Usuario findByEmail(String email) {
        try {
            List<Usuario> usuarios = firestoreService.getAll(COLLECTION, Usuario.class);
            return usuarios.stream()
                    .filter(u -> u.getEmail().equals(email))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar usuario", e);
        }
    }


}
