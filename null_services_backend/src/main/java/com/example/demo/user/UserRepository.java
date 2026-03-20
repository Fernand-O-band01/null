package com.example.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de acceso a datos para la entidad {@link User}.
 * <p>
 * Extiende {@link JpaRepository} para heredar
 * todas las operaciones CRUD estándar
 * (guardar, buscar, eliminar) y de paginación
 * proporcionada por Spring Data JPA.
 * </p>
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Busca a un usuario en la base de datos
     * utilizando su dirección de correo electrónico.
     * <p>
     * Este método es vital para el sistema de autenticación
     * de Spring Security
     * (dentro del UserDetailsService), ya que el email
     * se utiliza como el identificador
     * principal (username) para iniciar sesión en la aplicación.
     * </p>
     *
     * @param email El correo electrónico exacto del usuario a buscar.
     * @return Un {@link Optional} que contiene al usuario
     * si se encuentra en la base de datos,
     * o un Optional vacío si no existe ningún usuario con ese email.
     */
    Optional<User> findByEmail(String email);

}
