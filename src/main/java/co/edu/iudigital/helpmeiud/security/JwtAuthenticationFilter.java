package co.edu.iudigital.helpmeiud.security;

import co.edu.iudigital.helpmeiud.models.User;
import co.edu.iudigital.helpmeiud.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Filtro de Login
 */
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Sobrescribe el método "attemptAuthentication" para manejar la autenticación personalizada.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            // Leer el cuerpo de la solicitud como un objeto JSON
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            String username = user.getUsername();
            String password = user.getPassword();
            log.info("{}", username);
            // Crear un token de autenticación
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);
            log.info("{}", authenticationToken);
            // Usar el authenticationManager para autenticar el token
            return authenticationManager.authenticate(authenticationToken);

        } catch (IOException e) {
            throw new RuntimeException("Error al procesar la solicitud de autenticación", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        // Obtiene el nombre de usuario del principal autenticado (usualmente una implementación de UserDetails).
        String username = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername();
        // Obtiene los roles o autoridades asignados al usuario autenticado.
        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
        // Crea los "claims" (reclamaciones) del JWT, que son datos adicionales incluidos en el token.
        Claims claims = Jwts.claims()
                .add(Constants.AUTHORITIES, new ObjectMapper().writeValueAsString(roles)) // Agrega los roles del usuario como un claim (reclamar)
                .add(Constants.USERNAME, username) // Agrega el nombre de usuario como un claim
                .build();
        // Genera el token JWT con las reclamaciones y configuraciones necesarias.
        String jwt = // Json Web Token
                Jwts.builder()
                        .subject(username) // Establece el sujeto del token (normalmente el nombre de usuario).
                        .claims(claims) // Agrega las reclamaciones al token.
                        .expiration(new Date(System.currentTimeMillis() + 3600000)) // Define la fecha de expiración del token (1 hora desde la creación).
                        .issuedAt(new Date()) // Define la fecha de emisión del token.
                        .signWith(Constants.SECRET_KEY) // Firma el token con una clave secreta.
                        .compact(); // Genera el token como una cadena compacta.
        // Establece el encabezado de autorización en la respuesta con el JWT generado.
        response.setHeader(HttpHeaders.AUTHORIZATION, Constants.BEARER.concat(" ").concat(jwt));
        // Crea un cuerpo JSON para enviar información adicional en la respuesta.
        Map<String, String> body = new HashMap<>();
        body.put(Constants.TOKEN, jwt); // Agrega el token JWT al cuerpo.
        body.put(Constants.USERNAME, username); // Agrega el nombre de usuario al cuerpo.
        body.put(Constants.MESSAGE, Constants.SESSION_EXITOSO + username); // Agrega un mensaje personalizado.
        // Escribe el cuerpo JSON en la respuesta HTTP.
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        // Configura el tipo de contenido de la respuesta como JSON.
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // Establece el estado HTTP de la respuesta como "200 OK".
        response.setStatus(HttpStatus.OK.value());
    }


    /**
     * En caso que falle el login
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        Map<String, String> body = new HashMap<>();
        body.put(Constants.MESSAGE, Constants.SESSION_ERROR);
        body.put(Constants.ERROR, failed.getMessage());
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
