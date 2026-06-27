package pe.edu.utp.pf.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


/**
 * Entidad que dictamina el nivel de confiabilidad comercial del cliente, calculado mediante
 * modelos estadísticos internos del banco.
 *
 * @author Grupo 07
 * @version 2.0
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PerfilRiesgo {

    /**
     * Identificador único del perfil de riesgo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPerfil;

    /**
     * Puntaje numérico predictivo sobre el comportamiento de pago.
     */
    private Integer scoreCrediticio;

    /**
     * Categorización cualitativa del nivel de peligro.
     */
    private String nivelRiesgo;

    /**
     * Fecha de la última actualización del cálculo del perfil del cliente.
     */
    private LocalDate fechaUltimaEvaluacion;
}
