package co.com.bancolombia.model.solicitud;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Solicitud
{
    private Long documento;
    private Long monto;
    private Long plazo;
    private Long tipoPrestamo;
    private String nombreTipoPrestamo;
    private Long idEstado;
    private String nombreEstado;
    private String correo;
    private String nombre;
    private Long  tasa_interes;
    private Long  salario_base;


   public Solicitud(Long documento, Long monto, Long plazo, Long idTipoPrestamos, String nombreTipo, Long idEstado, String nombreEstado, Object o, String correo, String nombre, Long tasaInteres, Long salario) {
    }

    public Solicitud(Long documento, Long monto, Long plazo, Long idTipoPrestamos) {
    }
}
