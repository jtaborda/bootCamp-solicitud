package co.com.bancolombia.api.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record EditSolicitudDto(@NotBlank(message = "El nombre no puede estar vacío")
                               Long documento,
                               @Min(value = 0, message = "El monto no puede ser menor a 0")
                               Long monto,
                               @Min(value = 0, message = "El monto no puede ser menor a 0")
                               Long plazo,
                               Long tipoPrestamo) {
}