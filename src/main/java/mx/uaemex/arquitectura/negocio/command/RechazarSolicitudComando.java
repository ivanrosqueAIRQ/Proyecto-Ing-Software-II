package mx.uaemex.arquitectura.negocio.command;

import mx.uaemex.arquitectura.dao.SolicitudDAO;
import mx.uaemex.arquitectura.model.EstadoSolicitud;
import mx.uaemex.arquitectura.model.Solicitud;

/**
 * Comando concreto para rechazar manualmente una solicitud.
 */
public class RechazarSolicitudComando implements Comando {

    private final Solicitud solicitud;
    private final SolicitudDAO solicitudDAO;
    private final String rechazadoPor;

    public RechazarSolicitudComando(Solicitud solicitud, SolicitudDAO solicitudDAO, String rechazadoPor) {
        this.solicitud = solicitud;
        this.solicitudDAO = solicitudDAO;
        this.rechazadoPor = rechazadoPor;
    }

    @Override
    public void ejecutar() {
        solicitud.setEstado(EstadoSolicitud.RECHAZADO);
        solicitud.setResueltoPor(rechazadoPor);
        solicitudDAO.actualizar(solicitud);
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }
}
