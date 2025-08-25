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



    public Solicitud(Long documento, Long monto, Long plazo, Long idTipoPrestamos) {
    }

}
