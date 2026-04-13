package com.uma.todolist.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtils {

    // En un entorno real, esto iría en application.properties
    private String secret = "ClaveSecretaMuyLargaParaQueElAlgoritmoHS512NoDeErroresDeLongitud123456";
    private Long expiration = 86400L; // 24 horas en segundos

    // Extraer el nombre de usuario (el "Subject")
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Extraer la fecha de expiración
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Para leer el contenido del token necesitamos la clave secreta
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    // Comprobar si el token ha caducado
    private Boolean isTokenExpired(String token) {
        final Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    // Generar el token (Se usa en el proceso de Login)
    public String generateToken(String username, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles); // Metemos los roles en el payload

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String generateToken(Authentication authentication) {
        // Extraemos el nombre del usuario
        String username = authentication.getName();

        // Extraemos los roles y los convertimos a una lista de Strings
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Llamamos al método que ya teníamos (el que firma el token)
        return generateToken(username, roles);
    }

    // Validar el token
    public Boolean validateToken(String token) {
        try {
            // Si podemos parsearlo sin errores y no ha expirado, es válido
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // Extraer los roles (necesario para el filtro)
    public List<String> getRolesFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return (List<String>) claims.get("roles");
    }
}
