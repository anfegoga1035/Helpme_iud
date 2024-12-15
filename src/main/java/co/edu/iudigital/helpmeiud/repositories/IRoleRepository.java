package co.edu.iudigital.helpmeiud.repositories;

import co.edu.iudigital.helpmeiud.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByNombre(String nombre);
}
