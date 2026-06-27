package pe.edu.utp.pf.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que consolida los antecedentes financieros y de endeudamiento del cliente
 * recabados de las centrales de riesgo públicas o privadas.
 *
 * @author Grupo 07
 * @version 2.0
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HistorialCrediticio {

    /**
     * Código de identificación único del historial del cliente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistorial;

    /**
     * Cantidad total de obligaciones financieras vigentes que el cliente mantiene en el sistema.
     */
    private Integer numeroCreditosActivos;

    /**
     * Flag de control que determina si el cliente posee obligaciones vencidas dadas por perdidas o castigadas.
     */
    private Boolean tieneDeudasCastigadas;
}
