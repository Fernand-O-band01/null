package com.example.demo.server;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un Canal específico dentro de un Servidor.
 * Define la estructura de los canales (texto o voz) donde los usuarios
 * pueden enviar mensajes e interactuar dentro de una comunidad.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "channels")
public class Channel {

    /**
     * Identificador único del canal,
     * generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre descriptivo del canal (ejemplo: "general", "anuncios").
     */
    @Column(nullable = false)
    private String name;

    /**
     * Tipo de canal para diferenciar su
     * propósito (por ejemplo, "TEXT" o "VOICE").
     * Se inicializa por defecto como un canal de texto.
     */
    @Column(nullable = false)
    private String type = "TEXT";

    /**
     * El servidor principal al que pertenece este canal.
     * Representa una relación Muchos-a-Uno
     * (Muchos canales apuntan a un solo servidor).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id", nullable = false)
    private Server server;

    /**
     * Indicador de privacidad del canal.
     * Si es verdadero, el canal tiene un
     * "cantadito" y su acceso está restringido
     * solo a ciertos usuarios o roles dentro del servidor.
     */
    @Column(name = "is_private")
    private Boolean isPrivate = false;

}
