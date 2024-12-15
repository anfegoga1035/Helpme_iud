package co.edu.iudigital.helpmeiud.services.impl;

import co.edu.iudigital.helpmeiud.dtos.ErrorDTO;
import co.edu.iudigital.helpmeiud.dtos.requests.CasoRequestDTO;
import co.edu.iudigital.helpmeiud.dtos.response.CasoResponseDTO;
import co.edu.iudigital.helpmeiud.exceptions.NotFoundException;
import co.edu.iudigital.helpmeiud.mappers.CasoMapper;
import co.edu.iudigital.helpmeiud.models.Caso;
import co.edu.iudigital.helpmeiud.models.Delito;
import co.edu.iudigital.helpmeiud.models.User;
import co.edu.iudigital.helpmeiud.repositories.ICasoRepository;
import co.edu.iudigital.helpmeiud.repositories.IDelitoRepository;
import co.edu.iudigital.helpmeiud.repositories.IUserRepository;
import co.edu.iudigital.helpmeiud.security.UtilService;
import co.edu.iudigital.helpmeiud.services.ICasoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CasoServiceImpl implements ICasoService {

    @Autowired
    private ICasoRepository casoRepository; // inyección de dependencias

    @Autowired
    private IUserRepository usuarioRepository;

    @Autowired
    private IDelitoRepository delitoRepository;

    @Autowired
    private CasoMapper casoMapper;

    @Autowired
    private UtilService utilService;

    @Transactional
    @Override
    public CasoResponseDTO crearCaso(final CasoRequestDTO casoRequestDTO) throws NotFoundException {
        log.info("Entrar a crear caso service");

      // validar que existe usuario
        final String username = utilService.obtenerUsuarioActual();

        User user = validarUsuario(username);

        // validar que existe delito
        Delito delito = validarDelito(casoRequestDTO.getDelitoId());

        Caso caso = casoMapper.toCaso(casoRequestDTO, user, delito);

        caso = casoRepository.save(caso);

        return casoMapper.toCasoResponseDTO(caso);
    }

    private User validarUsuario(String username) throws NotFoundException {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(
                        () -> new NotFoundException(ErrorDTO.getErrorDto(
                                "Error de Usuario", "No existe usuario con Id: " + username, HttpStatus.NOT_FOUND.value()
                        ))
                );
    }

    private Delito validarDelito(Long delitoId) throws NotFoundException {
        return delitoRepository.findById(delitoId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorDTO.getErrorDto(
                                "Error de Delito", "No existe delito con Id: " + delitoId, HttpStatus.NOT_FOUND.value()
                        ))
                );
    }


    @Transactional(readOnly = true)
    @Override
    public CasoResponseDTO consultarCasoPorId(Long id) throws NotFoundException {
       Caso caso =  casoRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(ErrorDTO.getErrorDto(
                                "Error de Caso", "No existe caso con Id: " + id, HttpStatus.NOT_FOUND.value()
                            )
                        )
                );
        return casoMapper.toCasoResponseDTO(caso);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CasoResponseDTO> consultarCasosVisibles() {
        List<Caso> casos = casoRepository.findByIsVisibleTrue();
        return casoMapper.toCasoResponseDTOs(casos);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CasoResponseDTO> consultarCasos() {
        List<Caso> casos = casoRepository.findAll();
        return casoMapper.toCasoResponseDTOs(casos);
    }

    @Transactional
    @Override
    public CasoResponseDTO visibilizarCaso(Long id, Integer visible) throws NotFoundException {
        Caso caso = casoRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(ErrorDTO.getErrorDto(
                                "Error de Caso", "No existe caso con Id: " + id, HttpStatus.NOT_FOUND.value()
                        )
                        )
                );
        caso.setIsVisible((visible > 0) ? true : false);
        caso = casoRepository.save(caso);
        return casoMapper.toCasoResponseDTO(caso);
    }
}
