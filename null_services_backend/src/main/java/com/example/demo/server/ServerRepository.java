package com.example.demo.server;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de acceso a datos para la entidad {@link Server}.
 * <p>
 * Proporciona la interfaz necesaria para realizar operaciones CRUD sobre la tabla de servidores.
 * Gracias a Spring Data JPA, las consultas se generan automáticamente basándose en los
 * nombres de los métodos o mediante anotaciones @Query.
 * </p>
 */
public interface ServerRepository extends JpaRepository<Server, Long> {

    /**
     * Busca todos los servidores a los que pertenece un usuario específico.
     * <p>
     * Este método utiliza una consulta derivada de Spring Data que navega a través
     * de la relación Many-to-Many entre Servidores y Usuarios.
     * Es fundamental para poblar la barra lateral del frontend (Angular) con los
     * iconos de los servidores del usuario autenticado.
     * </p>
     *
     * @param userId El identificador único del usuario (Integer) cuyos servidores se desean recuperar.
     * @return Una lista de entidades {@link Server} donde el usuario es miembro.
     * Si el usuario no pertenece a ningún servidor, devuelve una lista vacía.
     */
    List<Server> findAllByMembersId(Integer userId);
}