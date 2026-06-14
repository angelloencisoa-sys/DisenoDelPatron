package pe.edu.utp.PF.service;

import pe.edu.utp.PF.model.Pago;

import java.util.List;
import java.util.Optional;

public interface PagoService {

    Optional<Pago> getById(Integer id);
    List<Pago> getAll();
    Pago create(Pago pago);
    Pago update(Pago old, Pago pago);
    void deleteById(Integer id);
}
