package co.com.bancolombia.api.dto;


public record SolicitudDto(
         Long documento,
         Long monto,
         Long plazo,
         Long tipoPrestamo,
         String nombreTipoPrestamo,
         Long idEstado,
         String nombreEstado,
         String correo,
         String nombre,
         Long  tasa_interes,
         Long  salario_base
) {
}