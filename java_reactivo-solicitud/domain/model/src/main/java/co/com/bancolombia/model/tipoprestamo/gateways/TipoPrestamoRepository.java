package co.com.bancolombia.model.tipoprestamo.gateways;


import co.com.bancolombia.model.tipoprestamo.TipoPrestamo;
import reactor.core.publisher.Mono;

public interface TipoPrestamoRepository {
    Mono<TipoPrestamo> getTipoPrstamo(Long Id);
}
