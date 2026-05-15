package pe.edu.utp.PF.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Credito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCredito;
    private Double capitalPrestado;
    private Double tasaInteresAnual;
    private LocalDate fechaDesembolso;
    private String estadoCredito;

    @OneToOne
    @JoinColumn(name = "contrato_id")
    private Contrato contrato;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cronograma_id")
    private Cronograma cronograma;

    @OneToMany(mappedBy = "credito")
    private List<SeguroDesgravamen> seguros;
}
