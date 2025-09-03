package co.com.bancolombia.model.usuario.gateways;

import co.com.bancolombia.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioRepository
{
    Mono<Usuario> getUsuarioPorDocumento(Long documento);
    Mono<Usuario> getUsuarioPorID(Long id);

}
