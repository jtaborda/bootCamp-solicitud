package co.com.bancolombia.model.usuario;
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
public class Usuario {
    private Long id;
    private String nombre;
    private String apellido;
    private String fechaNacimiento;
    private Integer telefono;
    private String direccion;
    private String correo;
    private Long salario;
    private Long documento;
    private Long idRol;
    private String nombreRol;
}
