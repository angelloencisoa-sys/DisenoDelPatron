package pe.edu.utp.pf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.pf.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

}
