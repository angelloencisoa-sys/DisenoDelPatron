package pe.edu.utp.pf.service.patron.prototype;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pe.edu.utp.pf.model.SolicitudCredito;

import java.time.LocalDate;

/**
 * Entidad que formaliza jurídicamente los acuerdos, plazos y obligaciones legales
 * pactados entre el cliente y el banco.
 *
 * @author Grupo 07
 * @version 2.0
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class Contrato {

    /**
     * Identificador único del documento contractual.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idContrato;

    /**
     * Fecha exacta en la que se legaliza y firma el documento.
     */
    private LocalDate fechaFirma;


    @Column(columnDefinition = "TEXT")
    private String clausulasExtras;


    private String tipo;

    /**
     * Expediente de la solicitud origen que desencadenó la generación de este contrato.
     */
    @OneToOne
    @JoinColumn(name = "solicitud_id")
    private SolicitudCredito solicitud;


    public void clonar() {
        log.info("Clonando un Contrato de tipo " + this.tipo);
    }

    public Contrato(Contrato target) {
        if (target != null) {
            this.clausulasExtras = target.getClausulasExtras();
            this.tipo = target.getTipo();
            this.idContrato = null;
            this.solicitud = null;
        }
    }
}
