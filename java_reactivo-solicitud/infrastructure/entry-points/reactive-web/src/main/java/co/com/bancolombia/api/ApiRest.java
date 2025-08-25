package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CreateSolictudDto;
import co.com.bancolombia.api.dto.SolicitudDto;
import co.com.bancolombia.api.mapper.SolicitudDTOMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import co.com.bancolombia.usecase.solicitud.SolicitudUseCase;

@Tag(name = "Usuarios", description = "API para manejo de usuarios")
@RestController
@RequestMapping(value = "/api/v1/solicitud", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ApiRest {

    private static final Logger logger = LoggerFactory.getLogger(ApiRest.class);

    private final SolicitudUseCase solicitudUseCase;
    private final SolicitudDTOMapper solicitudDTOMapper;

    @Operation(summary = "Crear una nueva solicitud")
    @PostMapping
    public Mono<ResponseEntity<Void>> createSolicitud(@Valid @RequestBody CreateSolictudDto createSolictudDto) {
        logger.info("Creando Solicitud");
        return solicitudUseCase.saveSolicitud(solicitudDTOMapper.toModel(createSolictudDto))
                .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()));
    }
    @Operation(summary = "Obtener todos los usuarios")
    @GetMapping
    public Flux<SolicitudDto> getAllSolicitud() {
        logger.info("***************************");
        logger.info("Trayendo las solicitudes");
        logger.info("*****************************");
        return solicitudUseCase.getAllSolicitud()
                .map(solicitudDTOMapper::toResponse);
    }
}
