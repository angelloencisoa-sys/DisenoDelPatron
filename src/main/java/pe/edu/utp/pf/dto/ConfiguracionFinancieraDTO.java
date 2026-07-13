package pe.edu.utp.pf.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfiguracionFinancieraDTO {
    private Double tasaMoraDiaria;
    private Double porcentajeIgv;
    private Double tasaInteresMaxima;
}