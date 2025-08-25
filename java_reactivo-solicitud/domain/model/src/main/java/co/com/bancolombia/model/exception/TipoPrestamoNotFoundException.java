package co.com.bancolombia.model.exception;

public class TipoPrestamoNotFoundException extends RuntimeException{

    public TipoPrestamoNotFoundException(String tipoDePrestamoNoEncontrado) {
        super(tipoDePrestamoNoEncontrado);
    }
}