package co.com.bancolombia.api.dto;


public record SolicitudDto(
         Long documento,
         Long monto,
         Long plazo,
         Long tipoPrestamo,
         String nombreTipoPrestamo,
         Long idEstado,
         String nombreEstado
) {
}