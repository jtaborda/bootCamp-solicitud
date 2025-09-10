package co.com.bancolombia.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CapacidadPagoDto(

        @NotNull(message = "El documento no puede estar vacío")
        @Positive(message = "El documento debe ser mayor a 0")
        Long documento,

        @NotNull(message = "El tipoPrestamo no puede estar vacío")
        @NotBlank(message = "El tipoPrestamo no puede ser vacío o solo espacios")
        String tipoPrestamo
) {}
