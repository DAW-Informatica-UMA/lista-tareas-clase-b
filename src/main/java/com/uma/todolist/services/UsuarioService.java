package com.uma.todolist.services;

import com.uma.todolist.exceptions.UsuarioYaExisteException;
import com.uma.todolist.models.Usuario;
import com.uma.todolist.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario registrar(Usuario usuario) {
        usuarioRepository.findByUsername(usuario.getUsername())
                .ifPresent(u -> {
                    throw new UsuarioYaExisteException("El nombre de usuario '" + usuario.getUsername() + "' ya está ocupado.");
                });

        // Encriptamos la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Si no vienen roles, le asignamos el de usuario por defecto
        if (usuario.getRoles() == null || usuario.getRoles().isEmpty()) {
            usuario.setRoles(java.util.Set.of("ROLE_USER"));
        }
        return usuarioRepository.save(usuario);
    }
}
