package co.com.bancolombia.usecase.reportes;


import co.com.bancolombia.model.reportes.Reportes;
import co.com.bancolombia.model.reportes.gateways.ReportesRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ReportesUseCase
{
    private final ReportesRepository reportesRepository;

    public Mono<Reportes> getReportes()
    {
        return reportesRepository.saveDinamo();
    }

}
