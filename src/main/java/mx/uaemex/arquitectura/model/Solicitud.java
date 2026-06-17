package mx.uaemex.arquitectura.model;

import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "SOLICITUD_TRAMITE")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = "\\d{7}", message = "El numero de cuenta debe tener 7 digitos")
    @Column(name = "NUM_CUENTA", nullable = false, length = 7)
    private String numeroCuenta;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_TRAMITE", nullable = false, length = 20)
    private TipoTramite tipoTramite;

    @Size(max = 255)
    @Column(name = "MOTIVO", length = 255)
    private String motivo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false, length = 30)
    private EstadoSolicitud estado;

    @Column(name = "RESUELTO_POR", length = 60)
    private String resueltoPor;

    @Column(name = "FECHA_REGISTRO", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "FOLIO", length = 20)
    private String folio;

    public Solicitud() {
        this.estado = EstadoSolicitud.PENDIENTE;
        this.fechaRegistro = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public String getResueltoPor() {
        return resueltoPor;
    }

    public void setResueltoPor(String resueltoPor) {
        this.resueltoPor = resueltoPor;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }
}
