package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.estadosolicitud.gateways.EstadoSolicitudRepository;
import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import co.com.bancolombia.model.tipoprestamo.gateways.TipoPrestamoRepository;
import co.com.bancolombia.model.usuario.gateways.UsuarioRepository;
import co.com.bancolombia.r2dbc.entity.solicitudEntity;
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
    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final EstadoSolicitudRepository estadoRepository;
    private final UsuarioRepository usuarioRepository;


    public SolicitudRepositoryAdapter(SolicitudReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator,
                                      UsuarioRepository usuarioRepository,
                                      TipoPrestamoRepository tipoPrestamoRepository,  EstadoSolicitudRepository estadoRepository1) {
        super(repository, mapper, entity -> mapper.map(entity, Solicitud.class));
        this.transactionalOperator = transactionalOperator;
        this.tipoPrestamoRepository=tipoPrestamoRepository;
        this.estadoRepository = estadoRepository1;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Mono<Solicitud> saveSolicitud(Solicitud solicitud) {
        return usuarioRepository.getUsuarioPorDocumento(solicitud.getDocumento())
                .flatMap(usuario -> {
                    solicitudEntity entity = new solicitudEntity(
                            null,
                            usuario.getId(),
                            solicitud.getMonto(),
                            solicitud.getPlazo(),
                            solicitud.getTipoPrestamo(),
                            1L
                    );

                    return repository.save(entity)
                            .map(e -> new Solicitud(
                                    solicitud.getDocumento(),
                                    e.getMonto(),
                                    e.getPlazo(),
                                    e.getIdTipoPrestamos()
                            ));
                });
    }



    @Override
    public Flux<Solicitud> getAllSolicitud() {
        return repository.findAll()
                .flatMap(entity ->
                        usuarioRepository.getUsuarioPorID(entity.getUsuarioId())
                                .flatMap(user ->
                                        tipoPrestamoRepository.getTipoPrstamo(entity.getIdTipoPrestamos())
                                                .flatMap(tipo ->
                                                        estadoRepository.getEstadoSolicitud(entity.getIdEstado())
                                                .map( estado-> new Solicitud(
                                                        user.getDocumento(),
                                                        entity.getMonto(),
                                                        entity.getPlazo(),
                                                        entity.getIdTipoPrestamos(),
                                                        tipo.getNombre_tipo(),
                                                        estado.getId_estado(),
                                                        estado.getNombreEstado(),
                                                        user.getCorreo(),
                                                        user.getNombre(),
                                                        tipo.getTasa_interes(),
                                                        user.getSalario(),entity.getId_solicitud()
                                                ))
                                )
                )
         );
    }

    @Override
    public Flux<Solicitud> getFiltros(Long estadoId) {
        return repository.findByIdEstado(estadoId)
                .flatMap(entity ->
                        usuarioRepository.getUsuarioPorID(entity.getUsuarioId())
                                .flatMap(user ->
                                        tipoPrestamoRepository.getTipoPrstamo(entity.getIdTipoPrestamos())
                                                .flatMap(tipo ->
                                                        estadoRepository.getEstadoSolicitud(entity.getIdEstado())
                                                                .map( estado-> new Solicitud(
                                                                        user.getDocumento(),
                                                                        entity.getMonto(),
                                                                        entity.getPlazo(),
                                                                        entity.getIdTipoPrestamos(),
                                                                        tipo.getNombre_tipo(),
                                                                        estado.getId_estado(),
                                                                        estado.getNombreEstado(),
                                                                        user.getCorreo(),
                                                                        user.getNombre(),
                                                                        tipo.getTasa_interes(),
                                                                        user.getSalario(),entity.getId_solicitud()
                                                                ))
                                                )
                                )
                );
    }

    @Override
    public Mono<Boolean> editSolicitud(Solicitud solicitud) {
        return estadoRepository.getEstadoSolicitudxNombre(solicitud.getNombreEstado())
                .flatMap(estadoSolicitud ->
                        repository.findById(solicitud.getIdSolicitud())
                                .flatMap(solicitudFinal -> {
                                    solicitudFinal.setIdEstado(estadoSolicitud.getId_estado());
                                    return repository.save(solicitudFinal);
                                })
                )
                .map(saved -> true)
                .defaultIfEmpty(false);

    }

    @Override
    public Flux<Solicitud> getDocumentoEstado(Solicitud solicitud) {
        return usuarioRepository.getUsuarioPorDocumento(solicitud.getDocumento())
                .flatMapMany(usuario ->
                        repository.findByIdEstadoAndUsuarioId(2L, usuario.getId())
                                .flatMap(entity ->
                                        usuarioRepository.getUsuarioPorID(entity.getUsuarioId())
                                                .flatMap(user ->
                                                        tipoPrestamoRepository.getTipoPrstamo(entity.getIdTipoPrestamos())
                                                                .flatMap(tipo ->
                                                                        estadoRepository.getEstadoSolicitud(entity.getIdEstado())
                                                                                .map(estado -> new Solicitud(
                                                                                        user.getDocumento(),
                                                                                        entity.getMonto(),
                                                                                        entity.getPlazo(),
                                                                                        entity.getIdTipoPrestamos(),
                                                                                        tipo.getNombre_tipo(),
                                                                                        estado.getId_estado(),
                                                                                        estado.getNombreEstado(),
                                                                                        user.getCorreo(),
                                                                                        user.getNombre(),
                                                                                        tipo.getTasa_interes(),
                                                                                        user.getSalario(),
                                                                                        entity.getId_solicitud()
                                                                                ))
                                                                )
                                                )
                                )
                );
    }





}
