package co.com.bancolombia.api.dto;

import jakarta.validation.constraints.*;

public record CreateSolictudDto(

        @NotNull(message = "El documento no puede estar vacío")
        @Positive(message = "El documento debe ser mayor a 0")
        Long documento,

        @NotNull(message = "El monto no puede estar vacío")
        @Min(value = 1, message = "El monto no puede ser menor a 1")
        Long monto,

        @NotNull(message = "El plazo no puede estar vacío")
        @Min(value = 1, message = "El plazo no puede ser menor a 1")
        Long plazo,

        @NotNull(message = "El tipoPrestamo no puede estar vacío")
        @Positive(message = "El tipoPrestamo debe ser mayor a 0")
        Long tipoPrestamo
) {}
