package pe.edu.utp.PF.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EvaluacionRiesgo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEvaluacion;
    private LocalDate fechaEvaluacion;
    private Boolean esAprobado;
    private String observacionesAnalista;

    @OneToOne(mappedBy = "evaluacion")
    private SolicitudCredito solicitud;
}
