package pe.edu.utp.pf.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditoDTO {
    private Double capitalPrestado;
    private Double tasaInteresAnual;
    private String estadoCredito;
}