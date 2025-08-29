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
@Table("tipo_prestamo")
public class tipoPrestamoEntity {
    @Id
    private Long id_tipo_prestamos;
    private String nombre_tipo ;
    private Long monto_minimo;
    private Long monto_maximo;
    private Long tasa_interes;
    private Long valdiacion_automatica;
}
