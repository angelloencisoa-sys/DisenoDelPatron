package pe.edu.utp.pf.service.patron.prototype;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pe.edu.utp.pf.model.SolicitudCredito;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idContrato;

    private LocalDate fechaFirma;

    @Column(columnDefinition = "TEXT")
    private String clausulasExtras;

    private String tipo;

    @JsonIgnoreProperties({"contrato", "cliente", "asesor", "evaluacion", "garantia"})
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