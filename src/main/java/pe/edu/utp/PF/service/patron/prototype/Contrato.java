package pe.edu.utp.PF.service.patron.prototype;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.utp.PF.model.Credito;
import pe.edu.utp.PF.model.SolicitudCredito;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Contrato implements Cloneable{
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
