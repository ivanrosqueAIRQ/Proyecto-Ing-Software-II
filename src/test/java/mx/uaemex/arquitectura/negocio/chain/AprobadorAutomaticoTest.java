package mx.uaemex.arquitectura.negocio.chain;

import mx.uaemex.arquitectura.model.EstadoSolicitud;
import mx.uaemex.arquitectura.model.Solicitud;
import mx.uaemex.arquitectura.model.TipoTramite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AprobadorAutomaticoTest {

    private AprobadorAutomatico aprobador;

    @BeforeEach
    void setUp() {
        aprobador = new AprobadorAutomatico();
    }

    @Test
    void manejar_bajaTemporal_apruebaAutomaticamente() {
        Solicitud solicitud = crearSolicitudValida(TipoTramite.BAJA_TEMPORAL);

        boolean resultado = aprobador.procesar(solicitud);

        assertTrue(resultado);
        assertEquals(EstadoSolicitud.APROBADO_AUTOMATICO, solicitud.getEstado());
        assertEquals("SISTEMA-AUTO", solicitud.getResueltoPor());
    }

    @Test
    void manejar_reingreso_apruebaAutomaticamente() {
        Solicitud solicitud = crearSolicitudValida(TipoTramite.REINGRESO);

        boolean resultado = aprobador.procesar(solicitud);

        assertTrue(resultado);
        assertEquals(EstadoSolicitud.APROBADO_AUTOMATICO, solicitud.getEstado());
        assertEquals("SISTEMA-AUTO", solicitud.getResueltoPor());
    }

    @Test
    void manejar_cambioCarrera_enRevision() {
        Solicitud solicitud = crearSolicitudValida(TipoTramite.CAMBIO_CARRERA);

        boolean resultado = aprobador.procesar(solicitud);

        assertTrue(resultado);
        assertEquals(EstadoSolicitud.EN_REVISION, solicitud.getEstado());
    }

    @Test
    void manejar_cambioPlantel_enRevision() {
        Solicitud solicitud = crearSolicitudValida(TipoTramite.CAMBIO_PLANTEL);

        boolean resultado = aprobador.procesar(solicitud);

        assertTrue(resultado);
        assertEquals(EstadoSolicitud.EN_REVISION, solicitud.getEstado());
    }

    @Test
    void manejar_bajaDefinitiva_enRevision() {
        Solicitud solicitud = crearSolicitudValida(TipoTramite.BAJA_DEFINITIVA);

        boolean resultado = aprobador.procesar(solicitud);

        assertTrue(resultado);
        assertEquals(EstadoSolicitud.EN_REVISION, solicitud.getEstado());
    }

    @Test
    void manejar_siempreRetornaTrue() {
        for (TipoTramite tipo : TipoTramite.values()) {
            Solicitud solicitud = crearSolicitudValida(tipo);
            assertTrue(aprobador.procesar(solicitud));
        }
    }

    private Solicitud crearSolicitudValida(TipoTramite tipo) {
        Solicitud solicitud = new Solicitud();
        solicitud.setNumeroCuenta("1234567");
        solicitud.setTipoTramite(tipo);
        solicitud.setMotivo("Motivo de prueba");
        return solicitud;
    }
}
