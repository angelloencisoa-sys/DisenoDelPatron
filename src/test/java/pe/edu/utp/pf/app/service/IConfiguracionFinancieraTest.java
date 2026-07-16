package pe.edu.utp.pf.app.service;

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
 * Clase de prueba unitaria para ConfiguracionFinancieraService (Patrón Singleton)
 * @author Grupo 07
 * @version 2.0
 */
@ExtendWith(MockitoExtension.class)
class IConfiguracionFinancieraTest {

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

        // Configuración guardada (con ID = 1, única instancia Singleton)
        saveConfig = new ConfiguracionFinanciera();
        saveConfig.setIdConfiguracion(1);
        saveConfig.setTasaInteresMaximaLegal(83.4);
        saveConfig.setPorcentajeMoraDiaria(0.5);
        saveConfig.setIgv(18.0);
    }

    @DisplayName("Service - Obtener instancia única (Singleton)")
    @Test
    void service_GetConfiguracion_ReturnSingletonInstance() {
        // 1. Preparación
        when(this.repoMock.findById(1)).thenReturn(Optional.of(saveConfig));
        ConfiguracionFinanciera result = null;

        // 2. Ejecución
        try {
            result = this.serviceMock.getConfiguracionUnica();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 3. Comparación
        assertThat(result).isNotNull();
        assertThat(result.getIdConfiguracion()).isEqualTo(1);
        assertThat(result.getTasaInteresMaximaLegal()).isEqualTo(83.4);
        assertThat(result.getPorcentajeMoraDiaria()).isEqualTo(0.5);
        assertThat(result.getIgv()).isEqualTo(18.0);
    }

    @DisplayName("Service - Actualizar Configuración (Singleton)")
    @Test
    void service_UpdateConfiguracion_ReturnUpdatedValues() {
        // 1. Preparación
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

        // 2. Ejecución
        try {
            result = this.serviceMock.updateConfiguracion(nuevoConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 3. Comparación
        assertThat(result).isNotNull();
        assertThat(result.getIdConfiguracion()).isEqualTo(1);
        assertThat(result.getTasaInteresMaximaLegal()).isEqualTo(85.0);
        assertThat(result.getPorcentajeMoraDiaria()).isEqualTo(0.75);
        assertThat(result.getIgv()).isEqualTo(19.0);
    }

    @DisplayName("Service - Calcular mora por retraso")
    @Test
    void service_CalcularMora_ReturnCorrectValue() {
        // 1. Preparación
        Double montoCapital = 500.0;
        Integer diasRetraso = 10;
        // Fórmula: 500 * (0.5 / 100.0 * 10) = 500 * 0.05 = 25.0

        when(this.repoMock.findById(1)).thenReturn(Optional.of(saveConfig));

        // 2. Ejecución
        Double mora = null;
        try {
            mora = this.serviceMock.calcularMoraPorRetraso(montoCapital, diasRetraso);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 3. Comparación
        assertThat(mora).isNotNull().isEqualTo(25.0);
    }

    @DisplayName("Service - Calcular mora con valores nulos")
    @Test
    void service_CalcularMora_WithNullValues_ReturnZero() {
        // 1. Preparación
        // 2. Ejecución
        Double mora = this.serviceMock.calcularMoraPorRetraso(null, 5);
        // 3. Comparación
        assertThat(mora).isEqualTo(0.0);
    }

    @DisplayName("Service - Calcular mora con días negativo")
    @Test
    void service_CalcularMora_WithNegativeDays_ReturnZero() {
        // 1. Preparación
        Double montoCapital = 500.0;
        Integer diasRetraso = -5;

        // 2. Ejecución
        Double mora = this.serviceMock.calcularMoraPorRetraso(montoCapital, diasRetraso);

        // 3. Comparación
        assertThat(mora).isEqualTo(0.0);
    }

    @DisplayName("Service - Configuración falla al acceder a BD")
    @Test
    void service_GetConfiguracion_HandleDatabaseException() {
        when(this.repoMock.findById(1)).thenThrow(RuntimeException.class);

        // Debe retornar la instancia del Singleton sin lanzar excepción
        try {
            ConfiguracionFinanciera result = this.serviceMock.getConfiguracionUnica();
            assertThat(result).isNotNull();
        } catch (Exception e) {
            // El servicio debe manejar la excepción internamente
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Service - Instancia Singleton es única")
    @Test
    void service_Singleton_ReturnsSameInstance() {
        when(this.repoMock.findById(1)).thenReturn(Optional.of(saveConfig));

        try {
            ConfiguracionFinanciera result1 = this.serviceMock.getConfiguracionUnica();
            ConfiguracionFinanciera result2 = this.serviceMock.getConfiguracionUnica();

            // Ambas instancias deben tener el mismo ID (1)
            assertThat(result1.getIdConfiguracion()).isEqualTo(result2.getIdConfiguracion());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

