package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.*;
import co.com.bancolombia.model.paginado.Paginado;
import co.com.bancolombia.model.reportes.Reportes;
import org.mapstruct.Mapper;
import co.com.bancolombia.model.solicitud.Solicitud;

@Mapper(componentModel ="spring")
public interface SolicitudDTOMapper {

    SolicitudDto toResponse(Solicitud solicitud);
    Solicitud toModel(CreateSolictudDto createSolictudDto);
    Solicitud toModel(EditSolicitudDto editSolicitudDto);
    Paginado toModel(PaginaDto paginaDto);
    Solicitud toModel(CapacidadPagoDto capacidadPagoDto);
    ReporteDto toResponse(Reportes reportes);

}