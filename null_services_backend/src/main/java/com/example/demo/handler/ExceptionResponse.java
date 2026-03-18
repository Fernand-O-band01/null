package com.example.demo.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

/**
 * Objeto de transferencia de datos (DTO)
 * estructurado para las respuestas de error de la API.
 * Unifica el formato en el que se envían los
 * errores genéricos, de negocio y de validación al cliente.
 * Solo incluye en el JSON final los campos que no estén vacíos.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse {

    /**
     * Código numérico interno que identifica
     * el error de negocio específico.
     */
    private Integer businessErrorCode;

    /**
     * Descripción detallada y legible del error de negocio.
     */
    private String businessErrorDescription;

    /**
     * Mensaje de error general, principal o técnico.
     */
    private String error;

    /**
     * Conjunto de mensajes de error de validación
     * a nivel general.
     * (Nota: Se cambió la 'V' inicial a minúscula
     * para cumplir con la convención de Java y Checkstyle).
     */
    private Set<String> validationError;

    /**
     * Mapa de errores de validación específicos por campo.
     * La clave es el nombre del campo que falló y
     * el valor es el motivo del fallo.
     */
    private Map<String, String> errors;

}
