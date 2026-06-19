package pe.edu.utp.pf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.pf.model.Credito;

import java.util.List;

public interface CreditoRepository extends JpaRepository<Credito, Integer> {

    List<Credito> findByEstadoCredito(String estadoCredito);
}
