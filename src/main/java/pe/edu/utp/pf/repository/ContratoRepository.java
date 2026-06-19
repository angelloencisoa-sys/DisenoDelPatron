package pe.edu.utp.pf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.pf.service.patron.prototype.Contrato;

public interface ContratoRepository extends JpaRepository<Contrato, Integer> {
}
