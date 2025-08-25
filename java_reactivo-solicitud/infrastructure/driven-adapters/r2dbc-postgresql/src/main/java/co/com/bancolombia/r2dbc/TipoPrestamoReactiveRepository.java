package co.com.bancolombia.r2dbc;

import co.com.bancolombia.r2dbc.entity.tipoPrestamoEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface TipoPrestamoReactiveRepository
        extends ReactiveCrudRepository
        <tipoPrestamoEntity, Long>, ReactiveQueryByExampleExecutor<tipoPrestamoEntity> {

}
