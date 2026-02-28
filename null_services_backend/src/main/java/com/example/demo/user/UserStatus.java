package com.example.demo.user;

/**
 * Enum que define los posibles estados de presencia de un usuario en la plataforma.
 * <p>
 * Estos estados determinan cómo los demás usuarios ven la disponibilidad del
 * usuario en la lista de amigos y en los servidores.
 * </p>
 */
public enum UserStatus {

    /**
     * El usuario está conectado y activo. (Normalmente el círculo verde 🟢)
     */
    ONLINE,

    /**
     * El usuario no está conectado al WebSocket. (Normalmente el círculo gris ⚪)
     */
    OFFLINE,

    /**
     * El usuario está conectado pero inactivo o lejos del teclado. (Normalmente la luna amarilla 🌙)
     */
    AWAY,

    /**
     * El usuario está conectado pero no desea recibir notificaciones de sonido/popups.
     * (Normalmente el círculo rojo de prohibido 🔴)
     */
    DO_NOT_DISTURB
}