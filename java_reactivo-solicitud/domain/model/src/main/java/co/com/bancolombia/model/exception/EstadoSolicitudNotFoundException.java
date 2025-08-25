package co.com.bancolombia.model.exception;

public class EstadoSolicitudNotFoundException extends RuntimeException{

    public EstadoSolicitudNotFoundException(String estadoSolicitud) {
        super(estadoSolicitud);
    }
}