package mx.uaemex.arquitectura.negocio;

import mx.uaemex.arquitectura.dao.SolicitudDAO;
import mx.uaemex.arquitectura.model.EstadoSolicitud;
import mx.uaemex.arquitectura.model.Solicitud;
import mx.uaemex.arquitectura.negocio.chain.AprobadorAutomatico;
import mx.uaemex.arquitectura.negocio.chain.Manejador;
import mx.uaemex.arquitectura.negocio.chain.ValidadorDatosCompletos;
import mx.uaemex.arquitectura.negocio.chain.ValidadorNumeroCuenta;
import mx.uaemex.arquitectura.negocio.command.AprobarSolicitudComando;
import mx.uaemex.arquitectura.negocio.command.InvocadorComandos;
import mx.uaemex.arquitectura.negocio.command.RechazarSolicitudComando;
import mx.uaemex.arquitectura.util.GeneradorFolios;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de negocio que orquesta el registro y procesamiento de solicitudes.
 * Usa Chain of Responsibility para validacion/aprobacion automatica
 * y Command para acciones manuales.
 */
@ApplicationScoped
public class SolicitudServicio {

    private final SolicitudDAO solicitudDAO;
    private final InvocadorComandos invocador;
    private final Manejador cadenaValidacion;

    @Inject
    public SolicitudServicio(SolicitudDAO solicitudDAO) {
        this.solicitudDAO = solicitudDAO;
        this.invocador = new InvocadorComandos();
        this.cadenaValidacion = construirCadena();
    }

    /**
     * Constructor para testing con inyeccion manual de dependencias.
     */
    public SolicitudServicio(SolicitudDAO solicitudDAO, InvocadorComandos invocador) {
        this.solicitudDAO = solicitudDAO;
        this.invocador = invocador;
        this.cadenaValidacion = construirCadena();
    }

    private Manejador construirCadena() {
        Manejador validadorDatos = new ValidadorDatosCompletos();
        Manejador validadorCuenta = new ValidadorNumeroCuenta();
        Manejador aprobadorAuto = new AprobadorAutomatico();

        validadorDatos.setSiguiente(validadorCuenta).setSiguiente(aprobadorAuto);
        return validadorDatos;
    }

    /**
     * Registra una nueva solicitud: asigna folio, ejecuta la cadena de validacion
     * y persiste el resultado.
     */
    public Solicitud registrar(Solicitud solicitud) {
        if (solicitud == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }

        solicitud.setFolio(GeneradorFolios.getInstancia().siguienteFolio());
        solicitud.setFechaRegistro(LocalDateTime.now());

        cadenaValidacion.procesar(solicitud);

        return solicitudDAO.guardar(solicitud);
    }

    /**
     * Aprueba manualmente una solicitud EN_REVISION.
     */
    public Solicitud aprobarManual(Solicitud solicitud, String aprobadoPor) {
        validarParaAccionManual(solicitud);
        AprobarSolicitudComando comando = new AprobarSolicitudComando(solicitud, solicitudDAO, aprobadoPor);
        invocador.ejecutar(comando);
        return solicitud;
    }

    /**
     * Rechaza manualmente una solicitud EN_REVISION.
     */
    public Solicitud rechazarManual(Solicitud solicitud, String rechazadoPor) {
        validarParaAccionManual(solicitud);
        RechazarSolicitudComando comando = new RechazarSolicitudComando(solicitud, solicitudDAO, rechazadoPor);
        invocador.ejecutar(comando);
        return solicitud;
    }

    public List<Solicitud> listarTodas() {
        return solicitudDAO.listarTodas();
    }

    public InvocadorComandos getInvocador() {
        return invocador;
    }

    private void validarParaAccionManual(Solicitud solicitud) {
        if (solicitud == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        if (solicitud.getEstado() != EstadoSolicitud.EN_REVISION) {
            throw new IllegalStateException(
                    "Solo se pueden aprobar/rechazar solicitudes EN_REVISION. Estado actual: " + solicitud.getEstado());
        }
    }
}
