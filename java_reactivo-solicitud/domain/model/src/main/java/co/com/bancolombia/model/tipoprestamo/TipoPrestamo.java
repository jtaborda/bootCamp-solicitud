package co.com.bancolombia.model.tipoprestamo;
import lombok.*;
import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TipoPrestamo {
    private Long id_tipo_prestamos;
    private String nombre_tipo;
}