package co.com.bancolombia.model.reportes.gateways;

import co.com.bancolombia.model.reportes.Reportes;
import reactor.core.publisher.Mono;

public interface ReportesRepository {

    Mono<Reportes> saveDinamo();
}
