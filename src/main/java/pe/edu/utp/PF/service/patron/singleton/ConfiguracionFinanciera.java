package pe.edu.utp.PF.service.patron.singleton;

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
public class ConfiguracionFinanciera {

    // 1. Instancia estática y privada de la propia clase (Singleton)
    @Transient // Anotación JPA para que no intente guardar este atributo en la tabla
    private static ConfiguracionFinanciera instancia;

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

    // Metodo estático para obtener la instancia única
    public static synchronized ConfiguracionFinanciera getInstancia() {
        if (instancia == null) {
            instancia = new ConfiguracionFinanciera();
            // Valores por defecto al inicializar el sistema por primera vez
            instancia.idConfiguracion = 1;
            instancia.tasaInteresMaximaLegal = 83.4;
            instancia.porcentajeMoraDiaria = 0.5;
            instancia.igv = 18.0;
        }        return instancia;
    }
}
