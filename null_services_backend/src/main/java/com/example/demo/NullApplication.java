package com.example.demo;
import com.example.demo.role.Role;
import com.example.demo.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
/**
 * Clase principal de arranque para
 * la aplicación Spring Boot.
 * Configura el contexto de Spring, levanta
 * el servidor embebido y habilita
 * características globales como la auditoría de
 * JPA y las tareas asíncronas.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableAsync

public class NullApplication {
    /**
     * Método principal (entry point) que inicia la ejecución del backend.
     * @param args Argumentos de línea de comandos
     * pasados durante el inicio de la aplicación.
     */

    public static void main(final String[] args) {
        SpringApplication.run(NullApplication.class, args);
    }

    /**
     * Bean de inicialización que se ejecuta
     * automáticamente al arrancar la aplicación.
     * Se encarga de verificar y crear el rol
     * predeterminado "USER" en la base de datos
     * si es que aún no existe, garantizando que los
     * nuevos registros puedan asignarse.
     *
     * @param roleRepository El repositorio JPA
     * utilizado para consultar y guardar roles.
     * @return Un CommandLineRunner que ejecuta la
     * lógica de inicialización de roles.
     */
    @Bean
    public CommandLineRunner runner(final RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("USER").isEmpty()) {
                roleRepository.save(
                        Role.builder().name("USER")
                                .build()
                );
            }
        };
    }

}
