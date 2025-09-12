package co.com.bancolombia.api;


import co.com.bancolombia.api.dto.ReporteDto;
import co.com.bancolombia.api.mapper.SolicitudDTOMapper;
import co.com.bancolombia.usecase.reportes.ReportesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@Tag(name = "Reportes", description = "API para manejo de los reportes ")
@RestController
@RequestMapping(value = "/api/v1/reportes", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ApiReportes {

    private static final Logger logger = LoggerFactory.getLogger(ApiReportes.class);

    private final ReportesUseCase reportesUseCase;
    private final SolicitudDTOMapper solicitudDTOMapper;


    @Operation(summary = "Trae los reportes de las solicitudes")
    @GetMapping
    public Mono<ReporteDto> getReporte() {
        logger.info("***************************"); logger.info("Trayendo el reporte "); logger.info("*****************************");
        return reportesUseCase.getReportes()
                .map(solicitudDTOMapper::toResponse);
    }

}
