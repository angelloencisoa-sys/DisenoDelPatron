package pe.edu.utp.PF.service.patron.prototype;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.utp.PF.model.Credito;
import pe.edu.utp.PF.model.SolicitudCredito;

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
public class Contrato implements Cloneable{

    /** Identificador único del documento contractual. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idContrato;

    /** Fecha exacta en la que se legaliza y firma el documento. */
    private LocalDate fechaFirma;

    /** Ruta o URI donde se almacena el documento digitalizado firmado (PDF) en el servidor. */
    private String rutaArchivoDigital;

    /** Cláusulas especiales, excepciones o condiciones adicionales pactadas para este crédito. */
    private String clausulasExtras;

    /** Expediente de la solicitud origen que desencadenó la generación de este contrato. */
    @OneToOne
    @JoinColumn(name = "solicitud_id")
    private SolicitudCredito solicitud;

    /** Crédito activo que se encuentra amparado bajo los términos de este contrato. */
    @OneToOne(mappedBy = "contrato")
    private Credito credito;
}
