package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.tipoprestamo.TipoPrestamo;
import co.com.bancolombia.model.tipoprestamo.gateways.TipoPrestamoRepository;
import co.com.bancolombia.r2dbc.entity.tipoPrestamoEntity;
import co.com.bancolombia.model.exception.TipoPrestamoNotFoundException;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Repository
public class TipoPrestamoRepositoryAdapter extends ReactiveAdapterOperations<
        TipoPrestamo,
        tipoPrestamoEntity,
        Long,
        TipoPrestamoReactiveRepository
> implements TipoPrestamoRepository {

    private final TransactionalOperator transactionalOperator;


    public TipoPrestamoRepositoryAdapter(TipoPrestamoReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator) {
        super(repository, mapper, entity -> mapper.map(entity, TipoPrestamo.class));
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<TipoPrestamo> getTipoPrstamo(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new TipoPrestamoNotFoundException("Tipo de Prestamo no encontrado ")))
                .map(entity -> mapper.map(entity, TipoPrestamo.class));
    }

    }
