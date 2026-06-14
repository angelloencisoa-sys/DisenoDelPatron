package pe.edu.utp.PF.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.PF.model.Cliente;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Optional<Cliente> findByNombresCompletos(String nombresCompletos);

    Optional<Cliente> findByDocumento(void attr0);
}
