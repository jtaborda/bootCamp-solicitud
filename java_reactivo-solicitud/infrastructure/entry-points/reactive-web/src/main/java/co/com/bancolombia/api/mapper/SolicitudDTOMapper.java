package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.*;
import co.com.bancolombia.model.paginado.Paginado;
import org.mapstruct.Mapper;
import co.com.bancolombia.model.solicitud.Solicitud;
import java.util.List;

@Mapper(componentModel ="spring")
public interface SolicitudDTOMapper {

    SolicitudDto toResponse(Solicitud solicitud);
    List<SolicitudDto> toResponseList(List<Solicitud> solicitudes);
    Solicitud toModel(CreateSolictudDto createSolictudDto);
    Solicitud toModel(EditSolicitudDto editSolicitudDto);
    Paginado toModel(PaginaDto paginaDto);
    Solicitud toModel(CapacidadPagoDto capacidadPagoDto);
}