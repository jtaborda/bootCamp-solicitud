package co.com.bancolombia.r2dbc.entity;

import jakarta.persistence.Column;
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
    @Column(name = "usuario_id")
    private Long usuarioId ;
    private Long monto ;
    private Long plazo ;
    @Column(name = "id_tipo_prestamos")
    private Long idTipoPrestamos;
    @Column(name = "id_estado")
    private Long idEstado;
}
