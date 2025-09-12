package co.com.bancolombia.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table("reporte_aprobados")
public class reporteAprobadosEntity
{
    private Long metrica;
    private Long idSolicitud ;
    private Long monto ;

}
