package co.edu.iudigital.helpmeiud.repositories;

import co.edu.iudigital.helpmeiud.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    //@Query("SELECT u FROM Usuario u WHERE u.username=?1")
    // SELECT * FROM usuarios WHERE username='0sdadasda' OR npmbre='dadadds'
    Optional<User> findByUsername(String username);

    /*Ejemplos:
    * // SELECT * FROM usuarios WHERE username='0sdadasda' OR npmbre='dadadds'
    Usuario findByUsernameOrNombre(String username, String nombre);
    // select * from usuarios WHERE username LIKE '%ddasd%'
    *
    * * */
}