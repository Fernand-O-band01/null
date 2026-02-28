package com.example.demo.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de acceso a datos para la entidad {@link Role}.
 * <p>
 * Facilita la interacción con la tabla de roles en la base de datos sin necesidad
 * de escribir consultas SQL manuales, heredando el poder de Spring Data JPA.
 * </p>
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * Busca un rol específico en la base de datos por su nombre exacto (ej. "USER" o "ADMIN").
     * <p>
     * NOTA DE ARQUITECTURA: Este método es utilizado críticamente durante el proceso de
     * registro (en el AuthenticationService) para buscar y asignar el rol predeterminado
     * a los nuevos usuarios antes de guardarlos.
     * </p>
     *
     * @param role El nombre del rol a buscar en formato String.
     * @return Un {@link Optional} que contiene el Rol si se encuentra. Si el rol no existe
     * en la base de datos, devuelve un Optional vacío, permitiendo al servicio lanzar
     * una excepción controlada ("User Role Not Found") en lugar de fallar silenciosamente.
     */
    Optional<Role> findByName(String role);
}