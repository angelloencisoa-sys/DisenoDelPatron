package pe.edu.utp.PF.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.PF.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

}
