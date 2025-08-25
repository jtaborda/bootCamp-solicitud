package co.com.bancolombia.model.estadosolicitud.gateways;

import co.com.bancolombia.model.estadosolicitud.EstadoSolicitud;
import reactor.core.publisher.Mono;

public interface EstadoSolicitudRepository
{
    Mono<EstadoSolicitud> getEstadoSolicitud(Long Id);
}
