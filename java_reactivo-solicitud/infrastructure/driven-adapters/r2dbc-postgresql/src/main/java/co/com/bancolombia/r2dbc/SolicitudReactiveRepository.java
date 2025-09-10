package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.r2dbc.entity.solicitudEntity;
import co.com.bancolombia.r2dbc.entity.userEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface SolicitudReactiveRepository
        extends ReactiveCrudRepository
        <solicitudEntity, Long>, ReactiveQueryByExampleExecutor<solicitudEntity> {

    Flux<solicitudEntity> findByIdTipoPrestamos(Long tipoPrestamo);
    Flux<solicitudEntity> findByIdEstado(Long estadoId);
    Flux<solicitudEntity> findByIdEstadoAndUsuarioId(Long estadoId, long idDocument);
}
