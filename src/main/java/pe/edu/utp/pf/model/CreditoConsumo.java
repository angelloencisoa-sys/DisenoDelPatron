package pe.edu.utp.pf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad especializada que describe préstamos personales dirigidos a la adquisición
 * de bienes de consumo o servicios por parte de personas naturales.
 *
 * @author Grupo 07
 * @version 2.0
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties
public class CreditoConsumo extends Credito {

    /**
     * Justificación o destino del dinero solicitado.
     */
    private String propositoCompra;
}
