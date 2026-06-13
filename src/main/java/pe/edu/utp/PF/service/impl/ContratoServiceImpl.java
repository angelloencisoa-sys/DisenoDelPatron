package pe.edu.utp.PF.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import pe.edu.utp.PF.model.SolicitudCredito;
import pe.edu.utp.PF.repository.ContratoRepository;
import pe.edu.utp.PF.service.ContratoService;
import pe.edu.utp.PF.service.patron.prototype.Contrato;
import pe.edu.utp.PF.service.patron.prototype.GestorContrato;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContratoServiceImpl implements ContratoService {

    private ContratoRepository repo;
    
    private GestorContrato gestorContrato; // Inyectamos tu Gestor

    @Transactional
    @Override
    public Contrato generarContratoDesdePlantilla(String tipoContrato, Integer idSolicitud) {
        try {
            // 1. Obtenemos el clon desde tu HashMap usando la lógica de tu laboratorio
            Contrato nuevoContrato = gestorContrato.obtenerContrato(tipoContrato);

            // Llamamos al metodo clonar para cumplir con el print de consola de tu clase base
            nuevoContrato.clonar();

            // 2. Personalizamos el clon para este cliente específico
            SolicitudCredito solicitud = new SolicitudCredito();
            solicitud.setIdSolicitud(idSolicitud);

            nuevoContrato.setSolicitud(solicitud);
            nuevoContrato.setFechaFirma(LocalDate.now());

            // 3. Guardamos el clon en la base de datos
            Contrato contratoGuardado = repo.save(nuevoContrato);
            log.info("Nuevo contrato de tipo {} generado por Prototype", tipoContrato);

            return contratoGuardado;

        } catch (CloneNotSupportedException e) {
            log.error("Error: El tipo de contrato {} no soporta clonación", tipoContrato, e);
            throw new RuntimeException("Error al clonar contrato: " + e.getMessage(), e);
        } catch (DataAccessException e) {
            log.error("Error de base de datos al guardar el contrato clonado: {}", e.getMessage());
            throw new RuntimeException("Error de base de datos", e);
        } catch (Exception e) {
            log.error("Error inesperado en la generación del contrato: {}", e.getMessage(), e);
            throw new RuntimeException("Error interno", e);
        }
    }
}
