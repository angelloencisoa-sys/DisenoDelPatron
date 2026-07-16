package pe.edu.utp.pf.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionFinancieraDTO {

    private Double tasaInteresMaximaLegal;
    private Double porcentajeMoraDiaria;
    private Double igv;
}