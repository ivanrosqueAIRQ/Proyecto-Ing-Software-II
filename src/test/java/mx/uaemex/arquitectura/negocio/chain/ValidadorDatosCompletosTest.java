package mx.uaemex.arquitectura.negocio.chain;

import mx.uaemex.arquitectura.model.EstadoSolicitud;
import mx.uaemex.arquitectura.model.Solicitud;
import mx.uaemex.arquitectura.model.TipoTramite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidadorDatosCompletosTest {

    private ValidadorDatosCompletos validador;

    @BeforeEach
    void setUp() {
        validador = new ValidadorDatosCompletos();
    }

    @Test
    void manejar_numeroCuentaNulo_rechaza() {
        Solicitud solicitud = new Solicitud();
        solicitud.setNumeroCuenta(null);
        solicitud.setTipoTramite(TipoTramite.BAJA_TEMPORAL);

        boolean resultado = validador.procesar(solicitud);

        assertTrue(resultado);
        assertEquals(EstadoSolicitud.RECHAZADO, solicitud.getEstado());
        assertEquals("SISTEMA-VALIDACION", solicitud.getResueltoPor());
    }

    @Test
    void manejar_numeroCuentaVacio_rechaza() {
        Solicitud solicitud = new Solicitud();
        solicitud.setNumeroCuenta("");
        solicitud.setTipoTramite(TipoTramite.BAJA_TEMPORAL);

        boolean resultado = validador.procesar(solicitud);

        assertTrue(resultado);
        assertEquals(EstadoSolicitud.RECHAZADO, solicitud.getEstado());
    }

    @Test
    void manejar_numeroCuentaEspacios_rechaza() {
        Solicitud solicitud = new Solicitud();
        solicitud.setNumeroCuenta("   ");
        solicitud.setTipoTramite(TipoTramite.BAJA_TEMPORAL);

        boolean resultado = validador.procesar(solicitud);

        assertTrue(resultado);
        assertEquals(EstadoSolicitud.RECHAZADO, solicitud.getEstado());
    }

    @Test
    void manejar_tipoTramiteNulo_rechaza() {
        Solicitud solicitud = new Solicitud();
        solicitud.setNumeroCuenta("1234567");
        solicitud.setTipoTramite(null);

        boolean resultado = validador.procesar(solicitud);

        assertTrue(resultado);
        assertEquals(EstadoSolicitud.RECHAZADO, solicitud.getEstado());
    }

    @Test
    void manejar_datosCompletos_noRechaza() {
        Solicitud solicitud = new Solicitud();
        solicitud.setNumeroCuenta("1234567");
        solicitud.setTipoTramite(TipoTramite.BAJA_TEMPORAL);

        boolean resultado = validador.procesar(solicitud);

        assertFalse(resultado);
        assertNotEquals(EstadoSolicitud.RECHAZADO, solicitud.getEstado());
    }

    @Test
    void manejar_datosCompletos_delegaAlSiguiente() {
        Solicitud solicitud = new Solicitud();
        solicitud.setNumeroCuenta("1234567");
        solicitud.setTipoTramite(TipoTramite.CAMBIO_CARRERA);

        // Sin siguiente, retorna false (no procesada)
        boolean resultado = validador.procesar(solicitud);
        assertFalse(resultado);
    }
}
