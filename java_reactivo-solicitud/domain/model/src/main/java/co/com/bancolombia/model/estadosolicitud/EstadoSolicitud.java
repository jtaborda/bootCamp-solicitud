package co.com.bancolombia.model.estadosolicitud;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EstadoSolicitud
{
    private Long id_estado;
    private String nombre_estado;
}
