package mx.uaemex.arquitectura.negocio.chain;

import mx.uaemex.arquitectura.model.EstadoSolicitud;
import mx.uaemex.arquitectura.model.Solicitud;
import mx.uaemex.arquitectura.model.TipoTramite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CadenaCompletaTest {

    private Manejador cadena;

    @BeforeEach
    void setUp() {
        Manejador validadorDatos = new ValidadorDatosCompletos();
        Manejador validadorCuenta = new ValidadorNumeroCuenta();
        Manejador aprobadorAuto = new AprobadorAutomatico();
        validadorDatos.setSiguiente(validadorCuenta).setSiguiente(aprobadorAuto);
        cadena = validadorDatos;
    }

    @Test
    void cadenaCompleta_solicitudValida_bajaTemporal_aprueba() {
        Solicitud solicitud = crearSolicitud("1234567", TipoTramite.BAJA_TEMPORAL);

        cadena.procesar(solicitud);

        assertEquals(EstadoSolicitud.APROBADO_AUTOMATICO, solicitud.getEstado());
    }

    @Test
    void cadenaCompleta_solicitudValida_cambioCarrera_enRevision() {
        Solicitud solicitud = crearSolicitud("1234567", TipoTramite.CAMBIO_CARRERA);

        cadena.procesar(solicitud);

        assertEquals(EstadoSolicitud.EN_REVISION, solicitud.getEstado());
    }

    @Test
    void cadenaCompleta_cuentaInvalida_rechaza() {
        Solicitud solicitud = crearSolicitud("123", TipoTramite.BAJA_TEMPORAL);

        cadena.procesar(solicitud);

        assertEquals(EstadoSolicitud.RECHAZADO, solicitud.getEstado());
    }

    @Test
    void cadenaCompleta_sinTipoTramite_rechaza() {
        Solicitud solicitud = new Solicitud();
        solicitud.setNumeroCuenta("1234567");
        solicitud.setTipoTramite(null);

        cadena.procesar(solicitud);

        assertEquals(EstadoSolicitud.RECHAZADO, solicitud.getEstado());
    }

    @Test
    void cadenaCompleta_sinNumeroCuenta_rechaza() {
        Solicitud solicitud = new Solicitud();
        solicitud.setNumeroCuenta(null);
        solicitud.setTipoTramite(TipoTramite.REINGRESO);

        cadena.procesar(solicitud);

        assertEquals(EstadoSolicitud.RECHAZADO, solicitud.getEstado());
    }

    @Test
    void manejador_setSiguiente_encadenaCorrectamente() {
        Manejador primero = new ValidadorDatosCompletos();
        Manejador segundo = new ValidadorNumeroCuenta();
        Manejador resultado = primero.setSiguiente(segundo);

        assertSame(segundo, resultado);
        assertSame(segundo, primero.getSiguiente());
    }

    private Solicitud crearSolicitud(String cuenta, TipoTramite tipo) {
        Solicitud solicitud = new Solicitud();
        solicitud.setNumeroCuenta(cuenta);
        solicitud.setTipoTramite(tipo);
        solicitud.setMotivo("Test");
        return solicitud;
    }
}
