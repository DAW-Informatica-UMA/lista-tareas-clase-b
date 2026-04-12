package com.uma.todolist.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Heredamos de OncePerRequestFilter para asegurar que se ejecute una vez por cada petición
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtils jwtTokenUtils; // La clase que fabrica/lee tokens

    @Autowired
    private CustomUserDetailsService customUserDetailsService; // La que busca en la BD

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Extraemos el encabezado "Authorization"
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Comprobamos si viene un token Bearer
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtTokenUtils.getUsernameFromToken(token);
            } catch (Exception e) {
                logger.error("Error extrayendo el username del token: " + e.getMessage());
            }
        }

        // Si tenemos username y el usuario no está ya autenticado en el contexto
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Cargamos el usuario de la base de datos
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // Validamos el token contra los datos del usuario
            if (jwtTokenUtils.validateToken(token)) {
                // Creamos el objeto de autenticación
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // ¡PASO CLAVE!: Guardamos al usuario en el contexto de seguridad
                // A partir de aquí, @PreAuthorize("hasRole(...)") ya funcionará
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continuamos con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
