package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CreateSolictudDto;
import co.com.bancolombia.api.dto.EditSolicitudDto;
import co.com.bancolombia.api.dto.PaginaDto;
import co.com.bancolombia.api.dto.SolicitudDto;
import co.com.bancolombia.api.mapper.SolicitudDTOMapper;
import co.com.bancolombia.api.security.JwtUtil;
import co.com.bancolombia.model.exception.InvalidJwtException;
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

@Tag(name = "Solicittud", description = "API para manejo de las solicitudes ")
@RestController
@RequestMapping(value = "/api/v1/solicitud", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ApiRest {

    private static final Logger logger = LoggerFactory.getLogger(ApiRest.class);

    private final SolicitudUseCase solicitudUseCase;
    private final SolicitudDTOMapper solicitudDTOMapper;
    private final JwtUtil jwtUtil;

    @Operation(summary = "Crear una nueva solicitud")
    @PostMapping
        public Mono<ResponseEntity<Void>> createSolicitud(@Valid @RequestBody CreateSolictudDto createSolictudDto,
                @RequestHeader("Authorization") String authHeader) {

            String token = authHeader.replace("Bearer ", "");
            Long documento = jwtUtil.extractDocumento(token);

            if (createSolictudDto.documento().compareTo(documento) != 0) {
                return Mono.error(new InvalidJwtException("Usuario solo puede crear solicitud para el mismo"));
            }

            logger.info("Creando Solicitud");
            return solicitudUseCase.saveSolicitud(solicitudDTOMapper.toModel(createSolictudDto))
                    .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()));
        }

    @Operation(summary = "Obtener Todas las Solicitudes")
    @GetMapping
    public Flux<SolicitudDto> getAllSolicitud() {
        logger.info("***************************"); logger.info("Trayendo las solicitudes"); logger.info("*****************************");
        return solicitudUseCase.getAllSolicitud()
                .map(solicitudDTOMapper::toResponse);
    }


    @Operation(summary = "Trae las solicitudes filtradas por paginacion y estado")
    @GetMapping("/paginado")
    public Flux<SolicitudDto> getAllSolicitudPaged(@RequestBody PaginaDto paginaDto) {
        logger.info("***************************"); logger.info("Trayendo las solicitudes"); logger.info("*****************************");
        return solicitudUseCase.getFiltroSolicitud(solicitudDTOMapper.toModel(paginaDto))
                .map(solicitudDTOMapper::toResponse);
    }


    @Operation(summary = "Cambia el estado de una solicitud")
    @PutMapping()
    public Mono<ResponseEntity<Void>> EditSolicitud(@RequestBody EditSolicitudDto editSolicitudDto) {
        logger.info("***************************"); logger.info("Editando estado de una solicitudes"); logger.info("*****************************");
        return solicitudUseCase.editSolicitud(solicitudDTOMapper.toModel(editSolicitudDto))
                .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()));
    }

}
