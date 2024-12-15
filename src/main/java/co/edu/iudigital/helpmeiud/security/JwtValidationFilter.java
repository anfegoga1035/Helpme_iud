package co.edu.iudigital.helpmeiud.security;

import co.edu.iudigital.helpmeiud.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Filtro para validación del Token
 */
@Slf4j
public class JwtValidationFilter extends BasicAuthenticationFilter {

    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Obtiene el encabezado de autorización de la solicitud HTTP.
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Si el encabezado no está presente o no comienza con el prefijo esperado, pasa la solicitud al siguiente filtro y termina.
        if (header == null || !header.startsWith(Constants.BEARER)) {
            chain.doFilter(request, response);
            return;
        }

        // Extrae el JWT del encabezado eliminando el prefijo definido en "Constantes.TOKEN".
        String jwt = header.replace(Constants.BEARER.concat(" "), StringUtils.EMPTY);
        log.info(jwt);
        try {
            // Parsea y verifica el JWT usando la clave secreta definida en "Constantes.SECRET_KEY".
            Claims claims = Jwts.parser()
                    .verifyWith(Constants.SECRET_KEY) // Verifica la firma del JWT.
                    .build()
                    .parseSignedClaims(jwt) // Parsea las reclamaciones del JWT.
                    .getPayload(); // Obtiene las reclamaciones.

            // Obtiene el nombre de usuario (sujeto) de las reclamaciones.
            String username = claims.getSubject();

            // Obtiene las autoridades (roles) del usuario desde las reclamaciones del JWT.
            Object authoritiesClaims = claims.get(Constants.AUTHORITIES);
            Collection<? extends GrantedAuthority> authorities = List.of(
                    new ObjectMapper()
                            .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class) // Configura un "mixin" para mapear los roles.
                            .readValue(
                                    authoritiesClaims.toString().getBytes(), // Convierte las reclamaciones a un formato procesable.
                                    SimpleGrantedAuthority[].class // Mapea las autoridades a un array de SimpleGrantedAuthority.
                            )
            );

            // Crea un token de autenticación basado en el nombre de usuario y las autoridades extraídas del JWT.
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            // Establece el token de autenticación en el contexto de seguridad.
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            // Continúa con el siguiente filtro en la cadena de filtros.
            chain.doFilter(request, response);

        } catch (JwtException e) {
            log.error(e.getMessage());
            // Manejo de excepciones: en caso de que el JWT sea inválido o no verificable.

            // Crea un mapa para devolver un mensaje de error detallado en el cuerpo de la respuesta.
            Map<String, String> body = new HashMap<>();
            body.put(Constants.ERROR, e.getMessage()); // Agrega el mensaje de error de la excepción.
            body.put(Constants.MESSAGE, "Token inválido"); // Mensaje adicional para el cliente.

            // Escribe el cuerpo JSON en la respuesta.
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));

            // Configura el tipo de contenido de la respuesta como JSON.
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            // Establece el estado HTTP como "401 Unauthorized".
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

}
