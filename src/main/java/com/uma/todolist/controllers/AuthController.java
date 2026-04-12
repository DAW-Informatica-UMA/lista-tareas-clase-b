package com.uma.todolist.controllers;

import com.uma.todolist.security.JwtTokenUtils;
import com.uma.todolist.models.Usuario;
import com.uma.todolist.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para registro de usuarios y obtención de tokens JWT")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private UsuarioService usuarioService;

    // REGISTRO: Crear un nuevo usuario
    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Crea un usuario en la base de datos y encripta su contraseña."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos o username duplicado", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.registrar(usuario));
    }

    // LOGIN: Obtener el token JWT
    @Operation(
            summary = "Iniciar sesión y obtener JWT",
            description = "Valida las credenciales y devuelve un token Bearer válido para 24 horas."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"token\": \"eyJhbGciOiJIUzUxMiJ9...\"}"))
            ),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        // 1. Autenticar al usuario con Spring Security
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.get("username"),
                        loginRequest.get("password")
                )
        );

        // 2. Si la autenticación fue éxito, generamos el Token
        // Usamos el método que ya tienes en JwtTokenUtils
        String token = jwtTokenUtils.generateToken(auth);

        // 3. Devolvemos el token en un JSON
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
}
