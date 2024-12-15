package co.edu.iudigital.helpmeiud.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UtilService {

    public static String obtenerUsuarioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                // Si el principal es una instancia de UserDetails (usuario autenticado con detalles)
                UserDetails userDetails = (UserDetails) principal;
                return userDetails.getUsername(); // Devuelve el nombre de usuario
            } else {
                // En caso de autenticación sin UserDetails (por ejemplo, token JWT)
                return principal.toString();
            }
        }

        return null; // No hay un usuario autenticado
    }
}
