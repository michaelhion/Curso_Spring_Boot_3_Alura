package med.voll.api.infra.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * O @Component é ultilizado para que o spring carregue uma
 * classe/componente gérico
 * não é um serviço,repository, ou qualquer outra
 * */
/**
 * OncePerRequestFilter do spring garante que seja filtrado todas requisições
 * apenas uma vez,e ela herda os metodos do filter do java jakarta servlet
 * */
@Component
public class SecurityFilter  extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;
    /**
     * uma aplicação pode ter 0 filtros ou muitos filtros
     * cada filtro precisa chamar o próximo filtro do fluxo e nesse caso o próximo fluxo é a execução da requisição
     * para isso acontecer é necessário chamar o parametro filterChain.doFilter e passar a requisição e a resposta
     * */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJwt = recuperarToken(request);
        if (tokenJwt != null) {
            var subject = tokenService.getSubject(tokenJwt);
            var usuario = repository.findByLogin(subject);

            //é necessário passar um objeto do tipo UsernamePasswordAuthenticationToken para o SecurityContextHolder
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

            //setAuthentication "informa" o spring que o usuario está logado e libera os endpoints
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            System.out.println(authorizationHeader);
            return authorizationHeader.replace("Bearer ", "");
        }

        return null;
    }
}
