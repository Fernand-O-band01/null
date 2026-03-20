package com.example.demo.server;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST encargado de gestionar las
 * operaciones de los Servidores o Comunidades.
 *
 * Expone endpoints para el descubrimiento de nuevos
 * servidores, la consulta de servidores a los que
 * pertenece el usuario y la creación de nuevos espacios comunitarios.
 */
@RestController
@RequestMapping("/servers")
@RequiredArgsConstructor
public class ServerController {

    /**
     * Servicio inyectado que contiene la lógica de
     * negocio para la gestión de servidores.
     */
    private final ServerService serverService;

    /**
     * Recupera una lista de todos los servidores
     * disponibles en la plataforma.
     *
     * Este endpoint se utiliza para la sección de "Descubrimiento",
     * permitiendo a los usuarios encontrar
     * comunidades a las que unirse.
     *
     * @return ResponseEntity con la lista de servidores
     * mapeados a ServerResponse.
     */
    @GetMapping("/discover")
    public ResponseEntity<List<ServerResponse>> findAllServer() {
        return ResponseEntity.ok(serverService.findAll());
    }

    /**
     * Recupera la lista de servidores donde
     * el usuario actual es miembro o propietario.
     *
     * Utiliza el objeto Authentication inyectado por
     * Spring Security para obtener de forma segura
     * la identidad del usuario desde el token JWT.
     *
     * @param connectedUser Objeto que representa al
     * usuario autenticado actual.
     * @return ResponseEntity con la lista de servidores del usuario.
     */
    @GetMapping("/my-servers")
    public ResponseEntity<List<ServerResponse>> findMyServers(
            final Authentication connectedUser) {
        return ResponseEntity.ok(serverService
                .findByUser(connectedUser));
    }

    /**
     * Crea un nuevo servidor en la plataforma.
     *
     * El usuario que realiza la petición
     * será asignado automáticamente como el
     * propietario (Owner) del servidor.
     * Se aplica validación sobre el cuerpo de la petición.
     *
     * @param request Datos del nuevo servidor (nombre, imagen, etc.).
     * @param connectedUser El usuario que será dueño del servidor.
     * @return ResponseEntity con los detalles del servidor recién creado.
     */
    @PostMapping
    public ResponseEntity<ServerResponse> saveServer(
            final @RequestBody @Valid ServerRequest request,
            final Authentication connectedUser) {
        return ResponseEntity.ok(serverService
                .createServer(request, connectedUser));
    }

    /**
     * Recupera toda la información de un
     * servidor específico, incluyendo sus canales.
     *
     * Se invoca cuando el usuario hace clic en el
     * icono de un servidor en la barra lateral.
     *
     * @param serverId El ID del servidor a consultar.
     * @return ResponseEntity con los detalles del servidor y sus canales.
     */
    @GetMapping("/{serverId}")
    public ResponseEntity<ServerResponse> findServerById(
            final @PathVariable Long serverId) {
        return ResponseEntity.ok(serverService
                .findById(serverId));
    }

    /**
     * Endpoint para que un usuario se una a una comunidad existente.
     *
     * @param serverId El ID del servidor al que se desea unir.
     * @param connectedUser El usuario autenticado que hace la petición.
     * @return ResponseEntity con los detalles del servidor
     * al que se acaba de unir.
     */
    @PostMapping("/{serverId}/join")
    public ResponseEntity<ServerResponse> joinServer(
            final @PathVariable Long serverId,
            final Authentication connectedUser) {
        return ResponseEntity.ok(serverService
                .joinServer(serverId, connectedUser));
    }

    /**
     * Endpoint para que un usuario abandone una comunidad.
     *
     * @param serverId El ID del servidor a abandonar.
     * @param connectedUser El usuario autenticado
     * que solicita salir del servidor.
     * @return ResponseEntity vacío indicando que la operación fue exitosa.
     */
    @PostMapping("/{serverId}/leave")
    public ResponseEntity<Void> leaveServer(
            final @PathVariable Long serverId,
            final Authentication connectedUser) {
        serverService.leaveServer(serverId, connectedUser);
        return ResponseEntity.ok().build();
    }

    /**
     * Crea un nuevo canal dentro de un servidor específico.
     *
     * @param serverId El ID del servidor padre donde se creará el canal.
     * @param request Los datos de configuración del nuevo
     * canal (nombre, tipo, privacidad).
     * @param connectedUser El usuario autenticado que intenta
     * crear el canal.
     * @return ResponseEntity con el canal recién
     * creado y mapeado a ChannelResponse.
     */
    @PostMapping("/{serverId}/channels")
    public ResponseEntity<ChannelResponse> createChannel(
            final @PathVariable Long serverId,
            final @RequestBody @Valid ChannelRequest request,
            final Authentication connectedUser) {
        return ResponseEntity.ok(serverService
                .createChannel(serverId, request, connectedUser));
    }

    /**
     * Elimina un servidor por completo.
     * Esta acción suele estar restringida al propietario.
     *
     * @param serverId El identificador único del servidor a eliminar.
     * @param connectedUser El usuario autenticado que solicita la eliminación.
     * @return ResponseEntity sin contenido (204 No Content) indicando éxito.
     */
    @DeleteMapping("/{serverId}")
    public ResponseEntity<Void> deleteServer(
            final @PathVariable Long serverId,
            final Authentication connectedUser) {
        serverService.deleteServer(serverId, connectedUser);
        return ResponseEntity.noContent().build();
    }

}
