package pe.edu.utp.pf.app.patron.prototype;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.utp.pf.model.SolicitudCredito;
import pe.edu.utp.pf.service.patron.prototype.Contrato;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test unitario para la entidad Contrato (Patrón Prototype).
 * Diseñado para alcanzar el 100% de cobertura en líneas y ramas de la clase Contrato.
 *
 * @author Grupo 07
 * @version 2.0
 */
class ContratoTest {

    @DisplayName("Prototype - Debe clonar un contrato correctamente usando el constructor copia")
    @Test
    void constructorCopia_ShouldCloneContractCorrectly() {

        Contrato prototipo = new Contrato();
        prototipo.setIdContrato(99);
        prototipo.setTipo("Consumo");
        prototipo.setClausulasExtras("Cláusula de penalidad del 10%");


        prototipo.setFechaFirma(LocalDate.of(2026, Month.JULY, 16));

        SolicitudCredito solicitud = new SolicitudCredito();
        solicitud.setIdSolicitud(5);
        prototipo.setSolicitud(solicitud);


        Contrato clon = new Contrato(prototipo);
        clon.clonar();


        assertThat(clon).isNotNull();
        assertThat(clon.getTipo()).isEqualTo("Consumo");
        assertThat(clon.getClausulasExtras()).isEqualTo("Cláusula de penalidad del 10%");

        assertThat(clon.getIdContrato()).isNull();
        assertThat(clon.getSolicitud()).isNull();
        assertThat(clon.getFechaFirma()).isNull();
    }

    @DisplayName("Prototype - Constructor copia con objeto nulo no debe fallar")
    @Test
    void constructorCopia_WithNullTarget_ShouldNotThrowException() {

        Contrato clon = new Contrato((Contrato) null);


        assertThat(clon).isNotNull();
        assertThat(clon.getTipo()).isNull();
        assertThat(clon.getClausulasExtras()).isNull();
    }

    @DisplayName("Entidad - Validar getters, setters y constructor con argumentos")
    @Test
    void entity_TestGettersSettersAndConstructors() {
        SolicitudCredito solicitud = new SolicitudCredito();
        LocalDate fecha = LocalDate.now();

        Contrato completo = new Contrato(1, fecha, "Cláusula A", "Empresarial", solicitud);

        assertThat(completo.getIdContrato()).isEqualTo(1);
        assertThat(completo.getFechaFirma()).isEqualTo(fecha);
        assertThat(completo.getClausulasExtras()).isEqualTo("Cláusula A");
        assertThat(completo.getTipo()).isEqualTo("Empresarial");
        assertThat(completo.getSolicitud()).isEqualTo(solicitud);

        Contrato vacio = new Contrato();
        vacio.setIdContrato(2);
        vacio.setFechaFirma(fecha);
        vacio.setClausulasExtras("Cláusula B");
        vacio.setTipo("Consumo");
        vacio.setSolicitud(solicitud);

        assertThat(vacio.getIdContrato()).isEqualTo(2);
        assertThat(vacio.getFechaFirma()).isEqualTo(fecha);
        assertThat(vacio.getClausulasExtras()).isEqualTo("Cláusula B");
        assertThat(vacio.getTipo()).isEqualTo("Consumo");
        assertThat(vacio.getSolicitud()).isEqualTo(solicitud);
    }
}