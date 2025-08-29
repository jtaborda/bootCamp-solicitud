package co.com.bancolombia.model.exception;

public class TechnicalException extends RuntimeException{

    public TechnicalException(String estadoSolicitud) {
        super(estadoSolicitud);
    }
}