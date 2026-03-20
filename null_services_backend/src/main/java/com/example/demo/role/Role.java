package com.example.demo.role;

import com.example.demo.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad que representa un Rol de seguridad
 * en el sistema (ejemplo: USER o ADMIN).
 * Es fundamental para el Control de Acceso Basado
 * en Roles (RBAC) y se utiliza
 * junto con Spring Security para determinar los permisos
 * de cada usuario.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Role {

    /**
     * Identificador único del rol generado
     * automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * Nombre descriptivo del rol.
     * Se define como único para evitar
     * duplicados en la base de datos
     * y mantener la integridad estricta de los permisos.
     */
    @Column(unique = true)
    private String name;

    /**
     * Relación inversa (Many-to-Many) que
     * contiene la lista de usuarios con este rol.
     * La anotación JsonIgnore es crítica
     * arquitectónicamente para evitar un bucle
     * infinito (StackOverflowError) al serializar
     * las entidades de forma bidireccional.
     */
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<User> users;

    /**
     * Fecha exacta en la que se insertó este
     * rol en la base de datos.
     * Este campo es gestionado y protegido
     * automáticamente por la auditoría de JPA.
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    /**
     * Fecha de la última modificación de los
     * datos de este rol.
     * Es actualizada de forma automática por
     * el listener de Spring Data JPA.
     */
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

}
