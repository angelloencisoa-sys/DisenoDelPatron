package pe.edu.utp.PF.service;

import pe.edu.utp.PF.model.Credito;

import java.util.List;
import java.util.Optional;

public interface CreditoService {
    Optional<Credito> getById(Integer id);
    List<Credito> getAll();
    Credito create(Credito credito);
    Credito update(Credito old, Credito credito);
    void deleteById(Integer id);
}
