package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CapacidadPagoDto;
import co.com.bancolombia.api.mapper.SolicitudDTOMapper;
import co.com.bancolombia.usecase.solicitud.SolicitudUseCase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@Tag(name = "Solicittud", description = "API para calcular la capacidad del usuario")
@RestController
@RequestMapping(value = "/api/v1/calcular-capacidad", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class Capacidad {

    private static final Logger logger = LoggerFactory.getLogger(Capacidad.class);
    private final SolicitudUseCase solicitudUseCase;
    private final SolicitudDTOMapper solicitudDTOMapper;

    @Operation(summary = "Servicio que calcula la c de endeudamiento")
    @PostMapping
    public Mono<ResponseEntity<String>> crearCapacidad(@Valid @RequestBody CapacidadPagoDto capacidad) {

        logger.info("Calculando Capacidad");
        return solicitudUseCase.calcularCapacidadd(solicitudDTOMapper.toModel(capacidad))
                .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()));
    }
}
