package mx.uaemex.arquitectura.negocio;

import mx.uaemex.arquitectura.dao.SolicitudDAO;
import mx.uaemex.arquitectura.model.EstadoSolicitud;
import mx.uaemex.arquitectura.model.Solicitud;
import mx.uaemex.arquitectura.model.TipoTramite;
import mx.uaemex.arquitectura.negocio.command.InvocadorComandos;
import mx.uaemex.arquitectura.util.GeneradorFolios;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitudServicioTest {

    @Mock
    private SolicitudDAO solicitudDAO;

    private InvocadorComandos invocador;
    private SolicitudServicio servicio;

    @BeforeEach
    void setUp() {
        GeneradorFolios.resetInstancia();
        invocador = new InvocadorComandos();
        servicio = new SolicitudServicio(solicitudDAO, invocador);
    }

    // --- registrar() ---

    @Test
    void registrar_solicitudValida_bajaTemporal_apruebaAutomatico() {
        Solicitud solicitud = crearSolicitud("1234567", TipoTramite.BAJA_TEMPORAL);
        when(solicitudDAO.guardar(any(Solicitud.class))).thenAnswer(i -> i.getArgument(0));

        Solicitud resultado = servicio.registrar(solicitud);

        assertEquals(EstadoSolicitud.APROBADO_AUTOMATICO, resultado.getEstado());
        assertNotNull(resultado.getFolio());
        verify(solicitudDAO).guardar(solicitud);
    }

    @Test
    void registrar_solicitudValida_reingreso_apruebaAutomatico() {
        Solicitud solicitud = crearSolicitud("9876543", TipoTramite.REINGRESO);
        when(solicitudDAO.guardar(any(Solicitud.class))).thenAnswer(i -> i.getArgument(0));

        Solicitud resultado = servicio.registrar(solicitud);

        assertEquals(EstadoSolicitud.APROBADO_AUTOMATICO, resultado.getEstado());
    }

    @Test
    void registrar_solicitudValida_cambioCarrera_enRevision() {
        Solicitud solicitud = crearSolicitud("1234567", TipoTramite.CAMBIO_CARRERA);
        when(solicitudDAO.guardar(any(Solicitud.class))).thenAnswer(i -> i.getArgument(0));

        Solicitud resultado = servicio.registrar(solicitud);

        assertEquals(EstadoSolicitud.EN_REVISION, resultado.getEstado());
    }

    @Test
    void registrar_solicitudValida_asignaFolio() {
        Solicitud solicitud = crearSolicitud("1234567", TipoTramite.BAJA_TEMPORAL);
        when(solicitudDAO.guardar(any(Solicitud.class))).thenAnswer(i -> i.getArgument(0));

        Solicitud resultado = servicio.registrar(solicitud);

        assertNotNull(resultado.getFolio());
        assertTrue(resultado.getFolio().startsWith("SGSA-"));
    }

    @Test
    void registrar_solicitudNula_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> servicio.registrar(null));
    }

    @Test
    void registrar_cuentaInvalida_rechaza() {
        Solicitud solicitud = crearSolicitud("123", TipoTramite.BAJA_TEMPORAL);
        when(solicitudDAO.guardar(any(Solicitud.class))).thenAnswer(i -> i.getArgument(0));

        Solicitud resultado = servicio.registrar(solicitud);

        assertEquals(EstadoSolicitud.RECHAZADO, resultado.getEstado());
    }

    @Test
    void registrar_sinTipoTramite_rechaza() {
        Solicitud solicitud = new Solicitud();
        solicitud.setNumeroCuenta("1234567");
        solicitud.setTipoTramite(null);
        when(solicitudDAO.guardar(any(Solicitud.class))).thenAnswer(i -> i.getArgument(0));

        Solicitud resultado = servicio.registrar(solicitud);

        assertEquals(EstadoSolicitud.RECHAZADO, resultado.getEstado());
    }

    @Test
    void registrar_asignaFechaRegistro() {
        Solicitud solicitud = crearSolicitud("1234567", TipoTramite.BAJA_TEMPORAL);
        when(solicitudDAO.guardar(any(Solicitud.class))).thenAnswer(i -> i.getArgument(0));

        Solicitud resultado = servicio.registrar(solicitud);

        assertNotNull(resultado.getFechaRegistro());
    }

    // --- aprobarManual() ---

    @Test
    void aprobarManual_solicitudEnRevision_aprueba() {
        Solicitud solicitud = crearSolicitudEnRevision();

        Solicitud resultado = servicio.aprobarManual(solicitud, "DIRECTOR");

        assertEquals(EstadoSolicitud.APROBADO_MANUAL, resultado.getEstado());
        assertEquals("DIRECTOR", resultado.getResueltoPor());
    }

    @Test
    void aprobarManual_agregaAlHistorialDeComandos() {
        Solicitud solicitud = crearSolicitudEnRevision();

        servicio.aprobarManual(solicitud, "ADMIN");

        assertEquals(1, invocador.getTotalEjecutados());
    }

    @Test
    void aprobarManual_solicitudNula_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> servicio.aprobarManual(null, "ADMIN"));
    }

    @Test
    void aprobarManual_estadoPendiente_lanzaExcepcion() {
        Solicitud solicitud = new Solicitud();
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);

        assertThrows(IllegalStateException.class, () -> servicio.aprobarManual(solicitud, "ADMIN"));
    }

    @Test
    void aprobarManual_yaAprobada_lanzaExcepcion() {
        Solicitud solicitud = new Solicitud();
        solicitud.setEstado(EstadoSolicitud.APROBADO_AUTOMATICO);

        assertThrows(IllegalStateException.class, () -> servicio.aprobarManual(solicitud, "ADMIN"));
    }

    // --- rechazarManual() ---

    @Test
    void rechazarManual_solicitudEnRevision_rechaza() {
        Solicitud solicitud = crearSolicitudEnRevision();

        Solicitud resultado = servicio.rechazarManual(solicitud, "COORDINADOR");

        assertEquals(EstadoSolicitud.RECHAZADO, resultado.getEstado());
        assertEquals("COORDINADOR", resultado.getResueltoPor());
    }

    @Test
    void rechazarManual_agregaAlHistorialDeComandos() {
        Solicitud solicitud = crearSolicitudEnRevision();

        servicio.rechazarManual(solicitud, "ADMIN");

        assertEquals(1, invocador.getTotalEjecutados());
    }

    @Test
    void rechazarManual_solicitudNula_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> servicio.rechazarManual(null, "ADMIN"));
    }

    @Test
    void rechazarManual_estadoPendiente_lanzaExcepcion() {
        Solicitud solicitud = new Solicitud();
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);

        assertThrows(IllegalStateException.class, () -> servicio.rechazarManual(solicitud, "ADMIN"));
    }

    // --- listarTodas() ---

    @Test
    void listarTodas_delegaAlDAO() {
        List<Solicitud> lista = Arrays.asList(new Solicitud(), new Solicitud());
        when(solicitudDAO.listarTodas()).thenReturn(lista);

        List<Solicitud> resultado = servicio.listarTodas();

        assertEquals(2, resultado.size());
        verify(solicitudDAO).listarTodas();
    }

    // --- getInvocador() ---

    @Test
    void getInvocador_noEsNulo() {
        assertNotNull(servicio.getInvocador());
    }

    // --- Helpers ---

    private Solicitud crearSolicitud(String cuenta, TipoTramite tipo) {
        Solicitud solicitud = new Solicitud();
        solicitud.setNumeroCuenta(cuenta);
        solicitud.setTipoTramite(tipo);
        solicitud.setMotivo("Motivo de prueba");
        return solicitud;
    }

    private Solicitud crearSolicitudEnRevision() {
        Solicitud solicitud = new Solicitud();
        solicitud.setNumeroCuenta("1234567");
        solicitud.setTipoTramite(TipoTramite.CAMBIO_CARRERA);
        solicitud.setEstado(EstadoSolicitud.EN_REVISION);
        return solicitud;
    }
}
