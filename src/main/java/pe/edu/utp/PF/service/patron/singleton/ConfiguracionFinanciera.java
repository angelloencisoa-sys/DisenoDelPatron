package pe.edu.utp.PF.service.patron.singleton;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConfiguracionFinanciera {

    // 1. Instancia estática y privada de la propia clase (Singleton)
    @Transient // Anotación JPA para que no intente guardar este atributo en la tabla
    private static ConfiguracionFinanciera instancia;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idConfiguracion;
    private Double tasaInteresMaximaLegal;
    private Double porcentajeMoraDiaria;
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
        }
        return instancia;
    }
}
