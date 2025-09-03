package co.com.bancolombia.model.cola;

import co.com.bancolombia.model.solicitud.Solicitud;
import reactor.core.publisher.Mono;

public interface MessageGateway {
    Mono<Void> send(Solicitud solicitud);
}