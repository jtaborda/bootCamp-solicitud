package co.com.bancolombia.model.solicitud.gateways;

import co.com.bancolombia.model.paginado.Paginado;
import co.com.bancolombia.model.solicitud.Solicitud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SolicitudRepository {

    Mono<Solicitud> saveSolicitud(Solicitud solicitud);
    Flux<Solicitud> getAllSolicitud();
    Flux<Solicitud> getFiltros(Long paginado);
}
