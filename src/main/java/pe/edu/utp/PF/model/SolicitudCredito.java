package pe.edu.utp.PF.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SolicitudCredito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSolicitud;
    private Double montoSolicitado;
    private Integer plazoMeses;
    private String estado;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "asesor_id")
    private AsesorFinanciero asesor;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "evaluacion_id")
    private EvaluacionRiesgo evaluacion;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "garantia_id")
    private Garantia garantia;

    @OneToOne(mappedBy = "solicitud")
    private Contrato contrato;
}
