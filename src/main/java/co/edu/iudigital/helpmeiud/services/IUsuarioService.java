package co.edu.iudigital.helpmeiud.services;

import co.edu.iudigital.helpmeiud.dtos.requests.UsuarioRequestDTO;
import co.edu.iudigital.helpmeiud.dtos.requests.UsuarioRequestUpdateDTO;
import co.edu.iudigital.helpmeiud.dtos.response.UsuarioResponseDTO;
import co.edu.iudigital.helpmeiud.exceptions.RestException;
import co.edu.iudigital.helpmeiud.models.User;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUsuarioService {

    // REGISTRARSE (SIGN UP)
    UsuarioResponseDTO registrar(UsuarioRequestDTO usuarioRequestDTO) throws RestException;

    // AUTENTICARSE (LOG IN)

    // CONSULTAR PERFIL POR USUARIO (USERNAME)
    User findByUsername(String username) throws RestException;

    UsuarioResponseDTO consutarPorId(Long id) throws RestException;

    UsuarioResponseDTO consutarPorUsername() throws RestException;

    // ACTUALIZAR PERFIL (CONTRASEÑA, FECHA NACIMI, ...)
    UsuarioResponseDTO actualizar(UsuarioRequestUpdateDTO usuarioRequestUpdateDTO) throws RestException; // TODO: AUTENTICACION

    // SUBIR UNA FOTO
    UsuarioResponseDTO subirImagen(MultipartFile image) throws RestException;;

    // OBTENER MI FOTO DE PERFIL
    Resource obtenerImagen(String nombre) throws RestException;;
}
