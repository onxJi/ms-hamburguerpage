package com.onx.hamburgerpage.services;

import com.onx.hamburgerpage.models.Usuario;

import java.util.concurrent.ExecutionException;

public interface AuthService {
    void register(Usuario usuario) throws ExecutionException, InterruptedException;
    Usuario authenticate(String email, String password);
}
