package pe.edu.utp.pf.app.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pe.edu.utp.pf.model.ConfiguracionFinanciera;
import pe.edu.utp.pf.repository.ConfiguracionFinancieraRepository;
import pe.edu.utp.pf.service.impl.ConfiguracionFinancieraServiceImpl;

/**
 * Test unitario para ConfiguracionFinancieraServiceImpl
 * @author Grupo 07
 * @version 2.0
 */
@ExtendWith(MockitoExtension.class)
class IConfiguracionFinancieraServiceImplTest {

    @Mock
    private ConfiguracionFinancieraRepository repoMock;

    @InjectMocks
    private ConfiguracionFinancieraServiceImpl serviceMock;

    private ConfiguracionFinanciera config;
    private ConfiguracionFinanciera saveConfig;

    @BeforeEach
    void setup() {
        // Configuración sin guardar
        config = new ConfiguracionFinanciera();
        config.setTasaInteresMaximaLegal(83.4);
        config.setPorcentajeMoraDiaria(0.5);
        config.setIgv(18.0);

        // Configuración guardada (con ID)
        saveConfig = new ConfiguracionFinanciera();
        saveConfig.setIdConfiguracion(1);
        saveConfig.setTasaInteresMaximaLegal(83.4);
        saveConfig.setPorcentajeMoraDiaria(0.5);
        saveConfig.setIgv(18.0);
    }

    @DisplayName("ConfiguracionFinancieraServiceImpl - Obtener instancia única Singleton")
    @Test
    void testGetConfiguracionUnica_ReturnSingleton() {
        when(this.repoMock.findById(1)).thenReturn(Optional.of(saveConfig));
        ConfiguracionFinanciera result = null;

        try {
            result = this.serviceMock.getConfiguracionUnica();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertThat(result).isNotNull();
        assertThat(result.getIdConfiguracion()).isEqualTo(1);
        assertThat(result.getTasaInteresMaximaLegal()).isEqualTo(83.4);
        assertThat(result.getPorcentajeMoraDiaria()).isEqualTo(0.5);
        assertThat(result.getIgv()).isEqualTo(18.0);
    }

    @DisplayName("ConfiguracionFinancieraServiceImpl - Actualizar Configuración")
    @Test
    void testUpdateConfiguracion_ReturnUpdatedValues() {
        ConfiguracionFinanciera nuevoConfig = new ConfiguracionFinanciera();
        nuevoConfig.setTasaInteresMaximaLegal(85.0);
        nuevoConfig.setPorcentajeMoraDiaria(0.75);
        nuevoConfig.setIgv(19.0);

        ConfiguracionFinanciera configActualizada = new ConfiguracionFinanciera();
        configActualizada.setIdConfiguracion(1);
        configActualizada.setTasaInteresMaximaLegal(85.0);
        configActualizada.setPorcentajeMoraDiaria(0.75);
        configActualizada.setIgv(19.0);

        when(this.repoMock.save(any())).thenReturn(configActualizada);
        ConfiguracionFinanciera result = null;

        try {
            result = this.serviceMock.updateConfiguracion(nuevoConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertThat(result).isNotNull();
        assertThat(result.getIdConfiguracion()).isEqualTo(1);
        assertThat(result.getTasaInteresMaximaLegal()).isEqualTo(85.0);
        assertThat(result.getPorcentajeMoraDiaria()).isEqualTo(0.75);
        assertThat(result.getIgv()).isEqualTo(19.0);
    }

    @DisplayName("ConfiguracionFinancieraServiceImpl - Calcular mora por retraso")
    @Test
    void testCalcularMoraPorRetraso_ReturnCorrectValue() {
        Double montoCapital = 500.0;
        Integer diasRetraso = 10;

        when(this.repoMock.findById(1)).thenReturn(Optional.of(saveConfig));

        Double mora = null;
        try {
            mora = this.serviceMock.calcularMoraPorRetraso(montoCapital, diasRetraso);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Fórmula: 500 * (0.5 / 100.0 * 10) = 25.0
        assertThat(mora).isNotNull().isEqualTo(25.0);
    }

    @DisplayName("ConfiguracionFinancieraServiceImpl - Calcular mora con valores nulos")
    @Test
    void testCalcularMoraPorRetraso_WithNullValues_ReturnZero() {
        Double mora = this.serviceMock.calcularMoraPorRetraso(null, 5);
        assertThat(mora).isEqualTo(0.0);
    }

    @DisplayName("ConfiguracionFinancieraServiceImpl - Calcular mora con días negativos")
    @Test
    void testCalcularMoraPorRetraso_WithNegativeDays_ReturnZero() {
        Double montoCapital = 500.0;
        Integer diasRetraso = -5;

        Double mora = this.serviceMock.calcularMoraPorRetraso(montoCapital, diasRetraso);

        assertThat(mora).isEqualTo(0.0);
    }

    @DisplayName("ConfiguracionFinancieraServiceImpl - Calcular mora con monto cero")
    @Test
    void testCalcularMoraPorRetraso_WithZeroAmount_ReturnZero() {
        Double mora = this.serviceMock.calcularMoraPorRetraso(0.0, 10);
        assertThat(mora).isEqualTo(0.0);
    }

    @DisplayName("ConfiguracionFinancieraServiceImpl - Obtener configuración maneja excepción")
    @Test
    void testGetConfiguracionUnica_HandleException() {
        when(this.repoMock.findById(1)).thenThrow(RuntimeException.class);

        try {
            ConfiguracionFinanciera result = this.serviceMock.getConfiguracionUnica();
            assertThat(result).isNotNull();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("ConfiguracionFinancieraServiceImpl - Update maneja excepción")
    @Test
    void testUpdateConfiguracion_HandleException() {
        ConfiguracionFinanciera nuevoConfig = new ConfiguracionFinanciera();
        nuevoConfig.setTasaInteresMaximaLegal(85.0);

        when(this.repoMock.save(any())).thenThrow(RuntimeException.class);

        try {
            this.serviceMock.updateConfiguracion(nuevoConfig);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("ConfiguracionFinancieraServiceImpl - Singleton retorna misma instancia")
    @Test
    void testSingleton_ReturnsSameInstance() {
        when(this.repoMock.findById(1)).thenReturn(Optional.of(saveConfig));

        try {
            ConfiguracionFinanciera result1 = this.serviceMock.getConfiguracionUnica();
            ConfiguracionFinanciera result2 = this.serviceMock.getConfiguracionUnica();

            assertThat(result1.getIdConfiguracion()).isEqualTo(result2.getIdConfiguracion());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("ConfiguracionFinancieraServiceImpl - Calcular mora con monto y días válidos")
    @Test
    void testCalcularMoraPorRetraso_WithValidValues() {
        Double montoCapital = 1000.0;
        Integer diasRetraso = 5;

        when(this.repoMock.findById(1)).thenReturn(Optional.of(saveConfig));

        Double mora = this.serviceMock.calcularMoraPorRetraso(montoCapital, diasRetraso);

        // Fórmula: 1000 * (0.5 / 100.0 * 5) = 25.0
        assertThat(mora).isEqualTo(25.0);
    }
}