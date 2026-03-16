package com.example.demo.auth;

import com.example.demo.email.EmailService;
import com.example.demo.email.EmailTemplateName;
import com.example.demo.role.RoleRepository;
import com.example.demo.security.JwtService;
import com.example.demo.user.Token;
import com.example.demo.user.TokenRepository;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import com.example.demo.user.UserStatus;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * Servicio central de Gestión de Identidad y Acceso (IAM).
 * <p>
 * Orquesta la lógica de negocio para el registro
 * de nuevos usuarios, la validación
 * mediante correos electrónicos (OTP - One Time Passwords) y la emisión de
 * tokens JWT
 * para el inicio de sesión.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    /**
     * Tamaño del código de activación que es enviado
     * al usuario.
     */
    private static final int CODE_LENGTH = 8;

    /**
     * Límite de tiempo para usar el código de activación
     * antes de que sea inválido.
     */
    private static final int MAX_TIME_TO_EXPIRED = 8;

    /**
     * Repositorio para acceder y gestionar los roles de
     * los usuarios en la base de datos.
     */
    private final RoleRepository roleRepository;
    /**
     * Componente encargado de encriptar y verificar
     * las contraseñas de forma segura.
     */
    private final PasswordEncoder passwordEncoder;
    /**
     * Repositorio para acceder y gestionar la información de
     * los usuarios en la base de datos.
     */
    private final UserRepository userRepository;
    /**
     * Repositorio para gestionar los tokens de autenticación.
     */
    private final TokenRepository tokenRepository;
    /**
     * Servicio utilizado para el envío de correos electrónicos
     * (ej. validación de cuentas).
     */
    private final EmailService emailService;
    /**
     * Gestor principal de Spring Security encargado de procesar
     * el inicio de sesión.
     */
    private final AuthenticationManager authenticationManager;
    /**
     * Servicio responsable de la generación, validación
     * y extracción de datos de los tokens JWT.
     */
    private final JwtService jwtService;
    /**
     * URL del frontend a la que se redirige al usuario
     * para activar su cuenta.
     */
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    /**
     * Registra a un nuevo usuario en la base de datos y dispara
     * el proceso de validación.
     * <p>
     * NOTA DE ARQUITECTURA: El usuario se crea bloqueado
     * por defecto (enable = false).
     * No podrá hacer login ni usar el sistema de WebSockets
     * hasta que valide su correo.
     * </p>
     *
     * @param request DTO con los datos del formulario de registro.
     * @throws MessagingException Si falla el envío del correo electrónico.
     */
    public void register(final RegistrationRequest request)
            throws MessagingException {
        var userRole = roleRepository.findByName("USER")
                .orElseThrow(() ->
                        new IllegalStateException("User Role Not Found"));

        var user = User.builder()
                .email(request.getEmail())
                .fullname(request.getFullname())
                .nickName(request.getNickName())
                //Hasheamos la contraseña antes de guardarla (NUNCA texto plano)
                .password(passwordEncoder.encode(request.getPassword()))
                .dateOfBirth(request.getDateOfBirth())
                .accountLocked(false)
                .enable(false) // Requiere activación por email
                .roles(List.of(userRole))
                .build();

        userRepository.save(user);
        sendValidationEmail(user);
    }

    /**
     * Helper method: Coordina la creación del token de 6 dígitos y
     * el envío del correo.
     * @param user extracción de información para enviar
     * correo con el token de validación
     */
    private void sendValidationEmail(final User user)
            throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.getFullname(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account Activation"
        );
    }

    /**
     * Genera un token de activación con una vida útil de
     * 15 minutos y lo asocia al usuario.
     * @param user asignación del token al usuario
     * @return Una respuesta que contiene el token JWT recién generado.
     */
    private String generateAndSaveActivationToken(final User user) {
        String generateToken = generateActivationCode(CODE_LENGTH);
        var token = Token.builder()
                .token(generateToken)
                .createdAt(LocalDateTime.now())
                // El token expira estrictamente en 15 minutos por seguridad
                .expiresAt(LocalDateTime.now().plusMinutes(MAX_TIME_TO_EXPIRED))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generateToken;
    }

    /**
     * Genera un código numérico aleatorio criptográficamente seguro.
     *
     * @param length La longitud del código (ej. 6 dígitos).
     * @return El código en formato String.
     */
    private String generateActivationCode(final int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        // Usamos SecureRandom en lugar de Math.random() para
        // evitar vulnerabilidades de predicción
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex =  random.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }

    /**
     * Autentica al usuario comprobando sus credenciales y genera
     * su token de sesión (JWT).
     *
     * @param request DTO con el email y la contraseña.
     * @return DTO con el JWT generado y datos
     * básicos del usuario para la UI.
     */
    public AuthenticationResponse authenticate(
            final AuthenticationRequest request) {
        // Esto delega la validación de la contraseña
        // al UserDetailsService y PasswordEncoder
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        var user = ((User) auth.getPrincipal());

        // ⚠️ IMPORTANTE PARA EL CHAT: Inyectamos el ID
        // del usuario en el token.
        // El frontend extraerá este 'userId' para usarlo
        // en el motor de WebSockets.
        claims.put("username", user.getUsername());
        claims.put("userId", user.getId());

        var jwtToken = jwtService.generateToken(claims, user);

        // Cambiamos el estado a ONLINE al hacer login
        user.setStatus(UserStatus.ONLINE);
        userRepository.save(user); // Guardamos el cambio en la base de datos

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .nickname(user.getNickName())
                .email(user.getEmail())
                .status(user.getStatus())
                .build();
    }

    /**
     * Valida el código OTP ingresado por el usuario y habilita
     * su cuenta si es correcto.
     *
     * @param token El código numérico de 6 dígitos.
     * @throws MessagingException Si el token expiró
     * (reenvía un correo nuevo automáticamente).
     */
    public void activateAccount(final String token)
            throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid Token"));

        // Lógica UX: Si se le pasó el tiempo, le enviamos
        // otro código automáticamente
        // en lugar de obligarlo a hacer clic en un botón de "Reenviar correo".
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation Token Expired.");
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        // Habilitamos al usuario para que pueda hacer login
        user.setEnable(true);
        userRepository.save(user);

        // Quemamos el token marcando su fecha de uso
        // (Auditoría / Prevención de reúso)
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }
}
