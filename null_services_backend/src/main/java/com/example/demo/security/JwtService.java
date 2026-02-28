package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Servicio encargado de toda la criptografía y gestión de los JSON Web Tokens (JWT).
 * <p>
 * Su responsabilidad incluye generar nuevos tokens al iniciar sesión, extraer
 * información (claims) de tokens existentes y validar que no hayan sido alterados
 * ni hayan expirado.
 * </p>
 */
@Service
public class JwtService {

    // Se inyectan desde el archivo application.yml (o properties)
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    /**
     * Extrae el nombre de usuario (en este sistema, el email) del token.
     * El "Subject" es el estándar en JWT para guardar el identificador principal.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Método genérico para extraer cualquier "Claim" (pieza de información) del token.
     * Utiliza funciones de orden superior (Function) para retornar el tipo de dato correcto.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsTResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsTResolver.apply(claims);
    }

    /**
     * Desencripta y parsea el token completo para obtener su cuerpo (Payload).
     * Si el token fue alterado o la firma no coincide con nuestra secretKey,
     * este método lanzará una excepción automáticamente (SignatureException).
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Genera un token estándar solo con los datos básicos del usuario.
     */
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Genera un token inyectando claims adicionales (ej. el userId para los WebSockets).
     *
     * @param claims Mapa con información extra a guardar en el token.
     * @param userDetails Los datos del usuario autenticado.
     * @return El token JWT en formato String.
     */
    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return buildToken(claims, userDetails, jwtExpiration);
    }

    /**
     * Constructor real del JWT.
     * <p>
     * Ensambla el Header (algoritmo), el Payload (claims, subject, fechas, roles)
     * y la Signature (firma matemática).
     * </p>
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long jwtExpiration) {

        // Extraemos los roles de Spring Security para guardarlos dentro del token
        var authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // La fecha de expiración se calcula sumando el tiempo configurado a la fecha actual
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                // Inyectamos los roles para que el frontend pueda leerlos si lo necesita
                .claim("authorities" , authorities)
                // Firmamos el token con nuestro secreto usando el algoritmo HMAC SHA-256
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida si un token es legítimo para un usuario específico y si aún está vigente.
     *
     * @param token El JWT recibido en la petición HTTP.
     * @param userDetails El usuario cargado desde la base de datos.
     * @return true si es válido, false en caso contrario.
     */
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Comprueba si la fecha de expiración del token ya pasó.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Decodifica la clave secreta configurada (que debe estar en Base64)
     * y genera el objeto Key criptográfico necesario para firmar y validar los tokens.
     */
    private Key getSignInKey() { // <-- Tip de Arquitecto: Corregido de getSingInKey a getSignInKey
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}