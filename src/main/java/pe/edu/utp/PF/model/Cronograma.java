package pe.edu.utp.PF.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cronograma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCronograma;
    private LocalDate fechaGeneracion;

    @OneToOne(mappedBy = "cronograma")
    private Credito credito;

    @OneToMany(mappedBy = "cronograma", cascade = CascadeType.ALL)
    private List<Cuota> cuotas;
}
