package med.voll.api.infra.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
    /**
     * uma aplicação pode ter 0 filtros ou muitos filtros
     * cada filtro precisa chamar o próximo filtro do fluxo e nesse caso o próximo fluxo é a execução da requisição
     * para isso acontecer é necessário chamar o parametro filterChain.doFilter e passar a requisição e a resposta
     * */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJwt = recuperarToken(request);
        //retorna login do usuario
        var subject = tokenService.getSubject(tokenJwt);
        System.out.println(subject);
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null){
            throw new RuntimeException("Token não enviado no cabeçalho Authorization!");
        }
        return authorizationHeader.replace("Bearer ","");
    }
}
