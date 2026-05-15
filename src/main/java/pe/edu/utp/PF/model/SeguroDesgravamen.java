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
public class SeguroDesgravamen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSeguro;
    private String codigoPoliza;
    private Double costoMensual;

    @ManyToOne
    @JoinColumn(name = "credito_id")
    private Credito credito;
}
