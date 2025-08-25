package co.com.bancolombia.r2dbc;

import co.com.bancolombia.r2dbc.entity.solicitudEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface SolicitudReactiveRepository
        extends ReactiveCrudRepository
        <solicitudEntity, Long>, ReactiveQueryByExampleExecutor<solicitudEntity> {

}
