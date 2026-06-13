package pe.edu.utp.PF.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.PF.service.patron.singleton.ConfiguracionFinanciera;

public interface ConfiguracionFinancieraRepository extends JpaRepository<ConfiguracionFinanciera, Integer> {
}
