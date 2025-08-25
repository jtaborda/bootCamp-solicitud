package co.com.bancolombia.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table("solicitud")
public class solicitudEntity {
    @Id
    private Long id_solicitud;
    private Long usuario_id ;
    private Long monto ;
    private Long plazo ;
    private Long id_tipo_prestamos;
    private Long id_estado;
}
