package com.example.demo.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;

/**
 * Entidad que representa un token de
 * validación o activación de cuenta.
 * <p>
 * NOTA DE ARQUITECTURA: Este token NO es el JWT
 * utilizado para mantener la sesión
 * del usuario. Este es un código de un solo uso
 * (ej. código de 6 dígitos) que se genera
 * durante el registro y se envía por correo electrónico
 * para verificar la identidad del usuario y habilitar
 * su cuenta (Account Activation).
 * </p>
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Token {

    /**
     * Identificador único del canal,
     * generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * El valor real del token o código de activación.
     * Generado aleatoriamente
     * (ej. "849201") para ser enviado al usuario.
     */
    @Column(unique = true)
    private String token;

    /**
     * Fecha y hora exacta en la que se
     * generó y guardó este token.
     */
    private LocalDateTime createdAt;

    /**
     * Fecha y hora límite para utilizar el token
     * antes de que caduque.
     * Pasada esta fecha, el sistema exigirá
     * generar un nuevo token de validación.
     */
    private LocalDateTime expiresAt;

    /**
     * Fecha y hora en la que el token
     * fue canjeado o validado con éxito.
     * Si este campo es nulo (null), significa que el
     * token está pendiente de uso.
     * Sirve como registro de auditoría para evitar
     * que un mismo token se use dos veces.
     */
    private LocalDateTime validatedAt;

    /**
     * El usuario propietario de este token.
     * Relación Muchos a Uno: Un usuario puede tener
     * múltiples tokens generados a lo largo
     * del tiempo (por ejemplo, si el primero expiró y solicitó uno nuevo)
     * pero un token específico pertenece
     * únicamente a un usuario.
     */
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

}
