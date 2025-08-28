package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.estadosolicitud.gateways.EstadoSolicitudRepository;
import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import co.com.bancolombia.model.tipoprestamo.gateways.TipoPrestamoRepository;
import co.com.bancolombia.r2dbc.entity.solicitudEntity;
import co.com.bancolombia.model.exception.TipoPrestamoNotFoundException;
import co.com.bancolombia.r2dbc.exception.UserNotFoundException;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class SolicitudRepositoryAdapter extends ReactiveAdapterOperations<
        Solicitud,
        solicitudEntity,
        Long,
        SolicitudReactiveRepository
> implements SolicitudRepository {

    private final TransactionalOperator transactionalOperator;
    private final UserReactiveRepository userReactiveRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final EstadoSolicitudRepository estadoRepository;

    public SolicitudRepositoryAdapter(SolicitudReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator, UserReactiveRepository usuarioRepository, UserReactiveRepository userReactiveRepository, TipoPrestamoRepository tipoPrestamoRepository,  EstadoSolicitudRepository estadoRepository1) {
        super(repository, mapper, entity -> mapper.map(entity, Solicitud.class));
        this.transactionalOperator = transactionalOperator;
        this.userReactiveRepository = userReactiveRepository;
        this.tipoPrestamoRepository = tipoPrestamoRepository;

        this.estadoRepository = estadoRepository1;
    }
//valdiacion usercase
    //
    @Override
    public Mono<Solicitud> saveSolicitud(Solicitud solicitud) {
        return tipoPrestamoRepository.getTipoPrstamo(solicitud.getTipoPrestamo())
                .switchIfEmpty(Mono.error(new TipoPrestamoNotFoundException("Tipo de Prestamo no encontrado "
                )))
                .flatMap(tipo ->
                        userReactiveRepository.findByDocumento(solicitud.getDocumento())
                                .switchIfEmpty(Mono.error(new UserNotFoundException(
                                        "Usuario no encontrado " )))
                                .flatMap(usuario -> {
                                    solicitudEntity entity = new solicitudEntity(
                                            null,
                                            usuario.getId(),
                                            solicitud.getMonto(),
                                            solicitud.getPlazo(),
                                            solicitud.getTipoPrestamo(),
                                            1L
                                    );//

                                    return repository.save(entity)
                                            .map(e -> new Solicitud(
                                                    solicitud.getDocumento(),
                                                    e.getMonto(),
                                                    e.getPlazo(),
                                                    e.getId_tipo_prestamos()
                                            ));
                                })
                );
    }

    @Override
    public Flux<Solicitud> getAllSolicitud() {
        return repository.findAll()
                .flatMap(entity ->
                        userReactiveRepository.findById(entity.getUsuario_id())
                                .flatMap(user ->
                                        tipoPrestamoRepository.getTipoPrstamo(entity.getId_tipo_prestamos())
                                                .flatMap(tipo ->
                                                        estadoRepository.getEstadoSolicitud(entity.getId_estado())
                                                .map( estado-> new Solicitud(
                                                        user.getDocumento(),
                                                        entity.getMonto(),
                                                        entity.getPlazo(),
                                                        entity.getId_tipo_prestamos(),
                                                        tipo.getNombre_tipo(),
                                                        estado.getId_estado(),
                                                        estado.getNombre_estado()
                                                ))
                                )
                )
         );
    }

}
