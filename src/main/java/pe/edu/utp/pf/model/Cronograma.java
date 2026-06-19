package pe.edu.utp.pf.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * Entidad que representa la estructura general del plan de pagos y amortización
 * generado para un crédito específico.
 *
 * @author Grupo 07
 * @version 2.0
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cronograma {

    /** Identificador único del cronograma de pagos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCronograma;

    /** Fecha exacta en la que el sistema calculó y emitió el plan de pagos. */
    private LocalDate fechaGeneracion;

    /** Crédito activo al cual pertenece y rige este calendario de pagos. */
    @OneToOne(mappedBy = "cronograma")
    private Credito credito;

    /** Listado detallado de las cuotas periódicas que componen la amortización total. */
    @OneToMany(mappedBy = "cronograma", cascade = CascadeType.ALL)
    private List<Cuota> cuotas;
}
