package pe.edu.utp.PF.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Contrato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idContrato;
    private LocalDate fechaFirma;
    private String rutaArchivoDigital;
    private String clausulasExtras;

    @OneToOne
    @JoinColumn(name = "solicitud_id")
    private SolicitudCredito solicitud;

    @OneToOne(mappedBy = "contrato")
    private Credito credito;
}
