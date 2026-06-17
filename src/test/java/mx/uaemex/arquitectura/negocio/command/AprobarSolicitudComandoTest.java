package mx.uaemex.arquitectura.negocio.command;

import mx.uaemex.arquitectura.dao.SolicitudDAO;
import mx.uaemex.arquitectura.model.EstadoSolicitud;
import mx.uaemex.arquitectura.model.Solicitud;
import mx.uaemex.arquitectura.model.TipoTramite;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AprobarSolicitudComandoTest {

    @Mock
    private SolicitudDAO solicitudDAO;

    @Test
    void ejecutar_cambiaEstadoAAprobadoManual() {
        Solicitud solicitud = crearSolicitudEnRevision();
        AprobarSolicitudComando comando = new AprobarSolicitudComando(solicitud, solicitudDAO, "ADMIN");

        comando.ejecutar();

        assertEquals(EstadoSolicitud.APROBADO_MANUAL, solicitud.getEstado());
    }

    @Test
    void ejecutar_asignaAprobadoPor() {
        Solicitud solicitud = crearSolicitudEnRevision();
        AprobarSolicitudComando comando = new AprobarSolicitudComando(solicitud, solicitudDAO, "DIRECTOR");

        comando.ejecutar();

        assertEquals("DIRECTOR", solicitud.getResueltoPor());
    }

    @Test
    void ejecutar_invocaActualizarEnDAO() {
        Solicitud solicitud = crearSolicitudEnRevision();
        AprobarSolicitudComando comando = new AprobarSolicitudComando(solicitud, solicitudDAO, "ADMIN");

        comando.ejecutar();

        verify(solicitudDAO, times(1)).actualizar(solicitud);
    }

    @Test
    void getSolicitud_retornaLaSolicitud() {
        Solicitud solicitud = crearSolicitudEnRevision();
        AprobarSolicitudComando comando = new AprobarSolicitudComando(solicitud, solicitudDAO, "ADMIN");

        assertSame(solicitud, comando.getSolicitud());
    }

    private Solicitud crearSolicitudEnRevision() {
        Solicitud solicitud = new Solicitud();
        solicitud.setNumeroCuenta("1234567");
        solicitud.setTipoTramite(TipoTramite.CAMBIO_CARRERA);
        solicitud.setEstado(EstadoSolicitud.EN_REVISION);
        return solicitud;
    }
}
