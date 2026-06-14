package pe.edu.utp.PF.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase que almacena las variables institucionales y los parámetros globales de negocio
 * que rigen los cálculos financieros del sistema.
 *
 * @author Grupo 07
 * @version 2.0
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionFinanciera {

    /** Código de registro de la configuración vigente. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idConfiguracion;

    /** Tasa máxima de interés permitida por ley o por los entes reguladores del país. */
    private Double tasaInteresMaximaLegal;

    /** Factor o porcentaje aplicado diariamente sobre cuotas que caigan en estado de mora. */
    private Double porcentajeMoraDiaria;

    /** Impuesto General a las Ventas aplicable a ciertos cargos administrativos de las cuotas. */
    private Double igv;


}
