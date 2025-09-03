package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.estadosolicitud.EstadoSolicitud;
import co.com.bancolombia.model.estadosolicitud.gateways.EstadoSolicitudRepository;
import co.com.bancolombia.model.exception.EstadoSolicitudNotFoundException;
import co.com.bancolombia.r2dbc.entity.estadoSolicitudEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Repository
public class EstadoSolicitudRepositoryAdapter extends ReactiveAdapterOperations<
        EstadoSolicitud,
        estadoSolicitudEntity,
        Long,
        EstadoSolicitudReactiveRepository
> implements EstadoSolicitudRepository {

    private final TransactionalOperator transactionalOperator;


    public EstadoSolicitudRepositoryAdapter(EstadoSolicitudReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator) {
        super(repository, mapper, entity -> mapper.map(entity, EstadoSolicitud.class));
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<EstadoSolicitud> getEstadoSolicitud(Long Id) {
        return repository.findById(Id)
                .switchIfEmpty(Mono.error(new EstadoSolicitudNotFoundException("El estado de solicitud No existe ")))
                .map(entity -> mapper.map(entity, EstadoSolicitud.class));
    }


    @Override
    public Mono<EstadoSolicitud> getEstadoSolicitudxNombre(String nombreEstado) {
        return repository.findByNombreEstadoContainsIgnoreCase(nombreEstado.toLowerCase())
                .switchIfEmpty(Mono.error(new EstadoSolicitudNotFoundException("El estado de solicitud No existe ")))
                .map(entity -> mapper.map(entity, EstadoSolicitud.class));
    }


}
