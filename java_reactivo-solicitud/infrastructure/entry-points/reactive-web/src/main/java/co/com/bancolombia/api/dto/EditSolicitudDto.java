package co.com.bancolombia.api.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EditSolicitudDto(@NotBlank(message = "El documento no puede estar vacío")
                               Long documento,
                               @NotNull(message = "El nombreEstado no puede estar vacío")
                               String nombreEstado,
                               @NotBlank(message = "El idSolicitud no puede estar vacío")
                               Long idSolicitud) {
}