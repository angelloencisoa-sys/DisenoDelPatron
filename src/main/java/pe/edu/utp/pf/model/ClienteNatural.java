package pe.edu.utp.pf.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad especializada que representa a un cliente que actúa a título individual
 * y personal natural ante la entidad financiera.
 *
 * @author Grupo 07
 * @version 2.0
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClienteNatural extends Cliente {

    /**
     * Documento Nacional de Identidad del cliente natural.
     */
    private String dni;

    /**
     * Estado civil actual del cliente (importante para evaluaciones de riesgo).
     */
    private String estadoCivil;

    /**
     * Puntaje acumulado en programas de fidelización financiera de la entidad.
     */
    private Integer puntosFidelidad;
}
