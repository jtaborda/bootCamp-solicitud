package co.com.bancolombia.usecase.solicitud;


import co.com.bancolombia.model.cola.MessageGateway;
import co.com.bancolombia.model.estadosolicitud.gateways.EstadoSolicitudRepository;
import co.com.bancolombia.model.exception.EstadoSolicitudNotFoundException;
import co.com.bancolombia.model.exception.TipoPrestamoNotFoundException;
import co.com.bancolombia.model.exception.UserNotFoundException;
import co.com.bancolombia.model.paginado.Paginado;
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
    private final EstadoSolicitudRepository estadoSolicitudRepository;
    private final MessageGateway messageGateway;



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

    public Flux<Solicitud> getFiltroSolicitud(Paginado paginado)
    {
        long page = (paginado.getPage() != null && paginado.getPage() >= 0) ? paginado.getPage() : 0L;
        long size = (paginado.getSize() != null && paginado.getSize() > 0) ? paginado.getSize() : 10L;


        if (paginado.getEstado() == null) {
            return Flux.error(new IllegalArgumentException("El tipo de estado es obligatorio"));
        }
        return estadoSolicitudRepository.getEstadoSolicitud(paginado.getEstado())
                .switchIfEmpty(Mono.error(new TipoPrestamoNotFoundException("Estado no encontrado")))
                .thenMany(
                        solicitudRepository.getFiltros(paginado.getEstado())
                                .skip(page * size)
                                .take(size)
                );

    }

    public Mono<Void> editSolicitud(Solicitud solicitud) {
        return estadoSolicitudRepository.getEstadoSolicitudxNombre(solicitud.getNombreEstado())
                .switchIfEmpty(Mono.error(new EstadoSolicitudNotFoundException("Estado no encontrado")))
                .flatMap(tipo -> usuarioRepository.getUsuarioPorDocumento(solicitud.getDocumento())
                        .switchIfEmpty(Mono.error(new UserNotFoundException("Usuario no encontrado"))))
                .flatMap(usuario -> solicitudRepository.findById(solicitud.getIdSolicitud())
                        .flatMap(soliFinal -> solicitudRepository.editSolicitud(solicitud)
                                .flatMap(saved -> {
                                    if (Boolean.TRUE.equals(saved)) {
                                        Mono<Void> acciones = messageGateway.send(solicitud);
                                        soliFinal.setIdSolicitud(solicitud.getIdSolicitud());
                                        if ("Aprobado".equalsIgnoreCase(solicitud.getNombreEstado())) {
                                            acciones = acciones.then(messageGateway.sendUpdate(soliFinal));
                                        }

                                        return acciones;
                                    }
                                    return Mono.empty();
                                })
                        )
                )
                .then();
    }


    public Mono<String> calcularCapacidadd(Solicitud solicitud) {
        return usuarioRepository.getUsuarioPorDocumento(solicitud.getDocumento())
                .switchIfEmpty(Mono.error(new UserNotFoundException("Usuario no encontrado")))
                .flatMap(usuario ->
                        tipoPrestamoRepository.getTipoPrstamo(solicitud.getTipoPrestamo())
                                .switchIfEmpty(Mono.error(new TipoPrestamoNotFoundException("Tipo de préstamo no encontrado")))
                                .flatMap(tipoPrestamo ->
                                        solicitudRepository.getDocumentoEstado(solicitud)
                                                .collectList()
                                                .flatMap(solicitudes -> {
                                                    if (solicitudes.isEmpty()) {
                                                        return Mono.error(new RuntimeException("No se encontraron solicitudes para el usuario y estado indicados"));
                                                    }

                                                    return messageGateway.calcularCapacidadEndeudamiento(solicitudes);

                                                })
                                )
                );
    }
}
