package co.com.bancolombia.api.dto;

import jakarta.validation.constraints.*;

public record CreateSolictudDto(

        @NotNull(message = "El nombre no puede estar vacío")
        Long documento,
        @Min(value = 0, message = "El monto no puede ser menor a 0")
        Long monto,
        @Min(value = 0, message = "El monto no puede ser menor a 0")
        Long plazo,
        Long tipoPrestamo
) {
}