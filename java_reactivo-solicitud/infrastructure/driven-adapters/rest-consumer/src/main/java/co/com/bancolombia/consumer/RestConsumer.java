package co.com.bancolombia.consumer;

import co.com.bancolombia.model.exception.TechnicalException;
import co.com.bancolombia.model.exception.UserNotFoundException;
import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.usuario.Usuario;
import co.com.bancolombia.model.usuario.gateways.UsuarioRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements UsuarioRepository {
    private final WebClient client;

    public Mono<Usuario> getUsuarioPorDocumento(Long documento) {
        return client
                .get()
                .uri("http://backendAutenticacion:8080/api/v1/usuarios/{id}", documento)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        resp -> Mono.error(new UserNotFoundException("Usuario no encontrado--")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        resp -> Mono.error(new TechnicalException("Error técnico en servicio de usuarios")))
                .bodyToMono(UsuarioResponse.class)
                .switchIfEmpty(Mono.error(new UserNotFoundException("Usuario no encontrado")))
                .map(dto -> Usuario.builder()
                        .id(dto.getId())
                        .nombre(dto.getNombre())
                        .apellido(dto.getApellido())
                        .fechaNacimiento(dto.getFechaNacimiento())
                        .telefono(dto.getTelefono())
                        .direccion(dto.getDireccion())
                        .correo(dto.getCorreo())
                        .salario(dto.getSalario())
                        .documento(dto.getDocumento())
                        .idRol(dto.getIdRol())
                        .nombreRol(dto.getNombreRol())
                        .build()
                );
    }



    public Mono<Usuario> getUsuarioPorID(Long id) {
        return client
                .get()
                .uri("http://backendAutenticacion:8080/api/v1/usuarios/id/{id}", id)
                .retrieve()
                .bodyToMono(UsuarioResponse.class)
                .map(dto -> Usuario.builder()
                        .id(dto.getId())
                        .nombre(dto.getNombre())
                        .apellido(dto.getApellido())
                        .fechaNacimiento(dto.getFechaNacimiento())
                        .telefono(dto.getTelefono())
                        .direccion(dto.getDireccion())
                        .correo(dto.getCorreo())
                        .salario(dto.getSalario())
                        .documento(dto.getDocumento())
                        .idRol(dto.getIdRol())
                        .nombreRol(dto.getNombreRol())
                        .build()
                );
    }

}
