package co.com.bancolombia.model.cola;

import co.com.bancolombia.model.solicitud.Solicitud;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MessageGateway {
    Mono<Void> send(Solicitud solicitud);
    Mono<String> calcularCapacidadEndeudamiento(List<Solicitud> solicitudes);
    Mono<String> recibirRespuestaLambda();

}