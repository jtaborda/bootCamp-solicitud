package co.com.bancolombia.model.paginado;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Paginado {
    private Long page;
    private Long size;
    private Long estado;
}
