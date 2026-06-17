package mx.uaemex.arquitectura.negocio.chain;

import mx.uaemex.arquitectura.model.EstadoSolicitud;
import mx.uaemex.arquitectura.model.Solicitud;
import mx.uaemex.arquitectura.model.TipoTramite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ValidadorNumeroCuentaTest {

    private ValidadorNumeroCuenta validador;

    @BeforeEach
    void setUp() {
        validador = new ValidadorNumeroCuenta();
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456", "12345678", "abcdefg", "12345ab", "123"})
    void manejar_numeroCuentaInvalido_rechaza(String cuenta) {
        Solicitud solicitud = new Solicitud();
        solicitud.setNumeroCuenta(cuenta);
        solicitud.setTipoTramite(TipoTramite.BAJA_TEMPORAL);

        boolean resultado = validador.procesar(solicitud);

        assertTrue(resultado);
        assertEquals(EstadoSolicitud.RECHAZADO, solicitud.getEstado());
        assertEquals("SISTEMA-VALIDACION", solicitud.getResueltoPor());
    }

    @Test
    void manejar_numeroCuentaValido_noRechaza() {
        Solicitud solicitud = new Solicitud();
        solicitud.setNumeroCuenta("1234567");
        solicitud.setTipoTramite(TipoTramite.BAJA_TEMPORAL);

        boolean resultado = validador.procesar(solicitud);

        assertFalse(resultado);
        assertNotEquals(EstadoSolicitud.RECHAZADO, solicitud.getEstado());
    }

    @Test
    void manejar_numeroCuentaSieteDigitos_pasa() {
        Solicitud solicitud = new Solicitud();
        solicitud.setNumeroCuenta("9999999");
        solicitud.setTipoTramite(TipoTramite.REINGRESO);

        boolean resultado = validador.procesar(solicitud);

        assertFalse(resultado);
    }

    @Test
    void manejar_numeroCuentaConEspacios_rechaza() {
        Solicitud solicitud = new Solicitud();
        solicitud.setNumeroCuenta("123 567");
        solicitud.setTipoTramite(TipoTramite.BAJA_TEMPORAL);

        boolean resultado = validador.procesar(solicitud);

        assertTrue(resultado);
        assertEquals(EstadoSolicitud.RECHAZADO, solicitud.getEstado());
    }
}
