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

    @Column(columnDefinition = "TEXT")
    private String clausulasExtras;

    public String tipo;

    /** Expediente de la solicitud origen que desencadenó la generación de este contrato. */
    @OneToOne
    @JoinColumn(name = "solicitud_id")
    private SolicitudCredito solicitud;

    public void clonar() {
        System.out.println("Clonando un Contrato de tipo " + this.tipo);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
