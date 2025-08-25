package co.com.bancolombia.r2dbc;


import co.com.bancolombia.r2dbc.entity.estadoSolicitudEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;



public interface EstadoSolicitudReactiveRepository
        extends ReactiveCrudRepository
        <estadoSolicitudEntity, Long>, ReactiveQueryByExampleExecutor<estadoSolicitudEntity> {

}
