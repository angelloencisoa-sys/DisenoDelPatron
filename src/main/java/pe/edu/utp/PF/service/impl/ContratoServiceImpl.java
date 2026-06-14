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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContratoServiceImpl implements ContratoService {

    private ContratoRepository repo;
    
    private GestorContrato gestorContrato; // Inyectamos tu Gestor

    @Transactional
    @Override
    public Optional<Contrato> getById(Integer id) {
        try {
            Optional<Contrato> contrato = repo.findById(id);
            if (contrato.isPresent()) {
                log.debug("Contrato encontrado: ID {}", id);
            } else {
                log.debug("Contrato no encontrado con ID: {}", id);
            }
            return contrato;
        } catch (DataAccessException e) {
            log.error("Error de acceso a datos al buscar contrato con ID {}: {}", id, e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error inesperado al buscar contrato con ID {}: {}", id, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public Contrato generarContratoDesdePlantilla(String tipoContrato, Integer idSolicitud) {
        try {
            // 1. Clonar el objeto prototipo desde el Gestor en memoria (Patrón Prototype)
            Contrato nuevoContrato = gestorContrato.obtenerContrato(tipoContrato);
            nuevoContrato.clonar(); // Log simulado del laboratorio

            // 2. Personalizar campos especificos del negocio
            SolicitudCredito solicitud = new SolicitudCredito();
            solicitud.setIdSolicitud(idSolicitud);

            nuevoContrato.setSolicitud(solicitud);
            nuevoContrato.setFechaFirma(LocalDate.now());

            // 3. Persistir el clon generado
            Contrato contratoGuardado = repo.save(nuevoContrato);
            log.info("Nuevo contrato de tipo {} generado exitosamente por Prototype con ID {}", tipoContrato, contratoGuardado.getIdContrato());

            return contratoGuardado;

        } catch (CloneNotSupportedException e) {
            log.error("Error de clonacion: El tipo {} no soporta Prototype: {}", tipoContrato, e.getMessage());
            throw new RuntimeException("Error al clonar el prototipo de contrato", e);
        } catch (DataAccessException e) {
            log.error("Error de acceso a datos al guardar contrato clonado: {}", e.getMessage());
            throw new RuntimeException("Error de base de datos al crear contrato", e);
        } catch (Exception e) {
            log.error("Error inesperado al generar contrato por clonacion: {}", e.getMessage(), e);
            throw new RuntimeException("Error interno del servidor", e);
        }
    }
}
