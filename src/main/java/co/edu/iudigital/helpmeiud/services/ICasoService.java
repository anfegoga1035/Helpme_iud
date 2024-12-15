package co.edu.iudigital.helpmeiud.services;

import co.edu.iudigital.helpmeiud.dtos.requests.CasoRequestDTO;
import co.edu.iudigital.helpmeiud.dtos.response.CasoResponseDTO;
import co.edu.iudigital.helpmeiud.exceptions.NotFoundException;

import java.util.List;

public interface ICasoService {

    // reportar o registrar un caso
    CasoResponseDTO crearCaso(CasoRequestDTO casoRequestDTO) throws NotFoundException;

    // consultar un caso
    CasoResponseDTO consultarCasoPorId(Long id) throws NotFoundException;

    // consultar todos los casos visibles
    List<CasoResponseDTO> consultarCasosVisibles();

    // consultar todos los casos
    List<CasoResponseDTO> consultarCasos();

    // inhabilitar un caso
    CasoResponseDTO visibilizarCaso(Long id, Integer visible) throws NotFoundException;
}
