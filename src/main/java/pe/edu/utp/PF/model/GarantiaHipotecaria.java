package pe.edu.utp.PF.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad especializada que describe bienes inmuebles (casas, terrenos, locales)
 * que quedan gravados a favor de la entidad financiera como garantía.
 *
 * @author Grupo 07
 * @version 2.0
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GarantiaHipotecaria extends Garantia{

    /** Número de partida electrónica del inmueble inscrita en los Registros Públicos. */
    private String numeroPartidaRegistral;

    /** Ubicación física exacta y legal de la propiedad inmueble en garantía. */
    private String direccionInmueble;
}
