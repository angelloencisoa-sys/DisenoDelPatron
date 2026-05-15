package pe.edu.utp.PF.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class Garantia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idGarantia;
    private Double valorTasacion;
    private String descripcion;

    @OneToOne(mappedBy = "garantia")
    private SolicitudCredito solicitud;
}