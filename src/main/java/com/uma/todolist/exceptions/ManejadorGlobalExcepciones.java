package com.uma.todolist.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ManejadorGlobalExcepciones
{
    @ExceptionHandler(UsuarioYaExisteException.class)
    public ResponseEntity<Map<String, String>> handleUsuarioYaExiste(UsuarioYaExisteException ex) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error", "Registro fallido");
        respuesta.put("mensaje", ex.getMessage());

        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
    }

    // Puedes agregar más @ExceptionHandler aquí para otros errores (404, 403, etc.)
}
