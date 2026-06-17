package mx.uaemex.arquitectura.negocio.command;

import mx.uaemex.arquitectura.dao.SolicitudDAO;
import mx.uaemex.arquitectura.model.EstadoSolicitud;
import mx.uaemex.arquitectura.model.Solicitud;

/**
 * Comando concreto para aprobar manualmente una solicitud.
 */
public class AprobarSolicitudComando implements Comando {

    private final Solicitud solicitud;
    private final SolicitudDAO solicitudDAO;
    private final String aprobadoPor;

    public AprobarSolicitudComando(Solicitud solicitud, SolicitudDAO solicitudDAO, String aprobadoPor) {
        this.solicitud = solicitud;
        this.solicitudDAO = solicitudDAO;
        this.aprobadoPor = aprobadoPor;
    }

    @Override
    public void ejecutar() {
        solicitud.setEstado(EstadoSolicitud.APROBADO_MANUAL);
        solicitud.setResueltoPor(aprobadoPor);
        solicitudDAO.actualizar(solicitud);
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }
}
