package co.com.bancolombia.usecase.solicitud;

import co.com.bancolombia.model.estadosolicitud.gateways.EstadoSolicitudRepository;
import co.com.bancolombia.model.exception.TipoPrestamoNotFoundException;
import co.com.bancolombia.model.exception.UserNotFoundException;
import co.com.bancolombia.model.tipoprestamo.gateways.TipoPrestamoRepository;
import co.com.bancolombia.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SolicitudUseCase
{

    private final SolicitudRepository solicitudRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final UsuarioRepository usuarioRepository;


public Mono<Void> saveSolicitud(Solicitud solicitud) {
    return tipoPrestamoRepository.getTipoPrstamo(solicitud.getTipoPrestamo())
            .switchIfEmpty(Mono.error(new TipoPrestamoNotFoundException("Tipo de préstamo no encontrado")))
            .flatMap(tipo ->
                    usuarioRepository.getUsuarioPorDocumento(solicitud.getDocumento())
                            .switchIfEmpty(Mono.error(new UserNotFoundException("Usuario no encontrado")))
            )
            .then(solicitudRepository.saveSolicitud(solicitud))
            .then();
}

    public Flux<Solicitud> getAllSolicitud()
    {
        return solicitudRepository.getAllSolicitud();
    }

}
