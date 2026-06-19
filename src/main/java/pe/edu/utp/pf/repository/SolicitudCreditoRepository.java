package pe.edu.utp.pf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.pf.model.SolicitudCredito;

import java.util.List;

public interface SolicitudCreditoRepository extends JpaRepository<SolicitudCredito, Integer> {

    List<SolicitudCredito> findByEstado(String estado);
}
