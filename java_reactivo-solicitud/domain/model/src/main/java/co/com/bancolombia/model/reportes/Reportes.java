package co.com.bancolombia.model.reportes;
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
public class Reportes
{
    private Long prestamosAprobados;
    private Long monto;
    private Long metrica;
}
