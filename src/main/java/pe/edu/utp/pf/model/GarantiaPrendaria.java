package pe.edu.utp.pf.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad especializada que describe bienes muebles, comúnmente vehículos motorizados,
 * prendados como resguardo legal del crédito.
 *
 * @author Grupo 07
 * @version 2.0
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GarantiaPrendaria extends Garantia {

    /**
     * Placa única de rodaje vehicular del automotor prendado.
     */
    private String placaVehiculo;

    /**
     * Empresa fabricante del vehículo en garantía.
     */
    private String marca;

    /**
     * Denominación de la línea o diseño específico del automotor.
     */
    private String modelo;
}