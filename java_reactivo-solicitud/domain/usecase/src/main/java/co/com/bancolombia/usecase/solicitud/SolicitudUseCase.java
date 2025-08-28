package co.com.bancolombia.usecase.solicitud;

import lombok.RequiredArgsConstructor;
import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SolicitudUseCase
{
    private final SolicitudRepository solicitudRepository;
//aqyui validaciones
    public Mono<Void> saveSolicitud(Solicitud solicitud)
    {
        return solicitudRepository.saveSolicitud(solicitud)
                .then();
    }
    public Flux<Solicitud> getAllSolicitud()
    {
        return solicitudRepository.getAllSolicitud();
    }

}
