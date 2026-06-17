package mx.uaemex.arquitectura.controller;

import mx.uaemex.arquitectura.model.Solicitud;
import mx.uaemex.arquitectura.model.TipoTramite;
import mx.uaemex.arquitectura.negocio.SolicitudServicio;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class TramiteBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private SolicitudServicio servicio;

    private Solicitud nuevaSolicitud = new Solicitud();

    public void registrarTramite() {
        try {
            servicio.registrar(nuevaSolicitud);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito",
                            "Solicitud registrada con folio: " + nuevaSolicitud.getFolio()));
            nuevaSolicitud = new Solicitud();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public void aprobar(Solicitud solicitud) {
        servicio.aprobarManual(solicitud, "ADMIN");
    }

    public void rechazar(Solicitud solicitud) {
        servicio.rechazarManual(solicitud, "ADMIN");
    }

    public List<Solicitud> getSolicitudes() {
        return servicio.listarTodas();
    }

    public TipoTramite[] getTiposTramite() {
        return TipoTramite.values();
    }

    public Solicitud getNuevaSolicitud() {
        return nuevaSolicitud;
    }

    public void setNuevaSolicitud(Solicitud nuevaSolicitud) {
        this.nuevaSolicitud = nuevaSolicitud;
    }
}
