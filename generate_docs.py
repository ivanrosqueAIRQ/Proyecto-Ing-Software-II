import logging
import os
import sys

try:
    from docx import Document
    from docx.shared import Pt, Inches
    from docx.enum.text import WD_ALIGN_PARAGRAPH
except ImportError:
    sys.exit(
        "Error: la librería 'python-docx' no está instalada.\n"
        "Instálala con:  pip install python-docx"
    )

logger = logging.getLogger(__name__)


def add_heading(doc, text, level=1):
    h = doc.add_heading(text, level=level)
    h.alignment = WD_ALIGN_PARAGRAPH.LEFT


def generate_doc():
    """Genera Documentacion_SGSA.docx junto al script.

    Returns:
        str: Ruta absoluta del archivo generado.

    Raises:
        OSError: Si no se puede escribir el archivo de salida.
        RuntimeError: Si la generación del documento falla.
    """
    doc = Document()
    
    # Title
    title = doc.add_heading('Documentación del Proyecto SGSA', 0)
    title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    doc.add_paragraph('Sistema de Gestión de Solicitudes Académicas (Java EE)').alignment = WD_ALIGN_PARAGRAPH.CENTER
    
    doc.add_page_break()

    # 1. Introducción
    add_heading(doc, '1. Introducción')
    doc.add_paragraph('Este documento contiene la documentación técnica del proyecto SGSA, incluyendo diagramas UML y el modelo de base de datos relacional. El sistema está construido con arquitectura N-Capas usando JSF, EJB/CDI y JPA.')
    
    # 2. Diagramas UML
    add_heading(doc, '2. Diagramas UML')
    
    add_heading(doc, '2.1 Diagrama de Clases', level=2)
    doc.add_paragraph('Representación de las capas de Modelo, Controlador y Negocio.')
    mermaid_class = """
classDiagram
    class Solicitud {
        -Long id
        -String numeroCuenta
        -TipoTramite tipoTramite
        -String motivo
        -EstadoSolicitud estado
        -String resueltoPor
        -LocalDateTime fechaRegistro
    }
    class SolicitudDAO {
        <<interface>>
        +guardar(Solicitud) Solicitud
        +actualizar(Solicitud) Solicitud
        +buscarPorId(Long) Solicitud
        +listarTodas() List~Solicitud~
    }
    class SolicitudDAOImpl {
        -EntityManager em
    }
    class SolicitudServicio {
        -SolicitudDAO solicitudDAO
        -InvocadorComandos invocador
        +registrar(Solicitud) Solicitud
        +aprobarManual(Solicitud) Solicitud
        +rechazarManual(Solicitud) Solicitud
    }
    class TramiteBean {
        -SolicitudServicio servicio
        -Solicitud nuevaSolicitud
        +registrarTramite()
        +aprobar(Solicitud)
        +rechazar(Solicitud)
    }
    
    SolicitudDAO <|.. SolicitudDAOImpl
    SolicitudServicio --> SolicitudDAO
    SolicitudServicio --> Solicitud
    TramiteBean --> SolicitudServicio
    TramiteBean --> Solicitud
"""
    doc.add_paragraph(mermaid_class)

    add_heading(doc, '2.2 Diagrama de Secuencia (Registro de Trámite)', level=2)
    mermaid_seq = """
sequenceDiagram
    actor Usuario
    participant TramiteBean
    participant SolicitudServicio
    participant GeneradorFolios
    participant CadenaResponsabilidad
    participant SolicitudDAO
    participant BaseDeDatos
    
    Usuario->>TramiteBean: registrarTramite()
    TramiteBean->>SolicitudServicio: registrar(Solicitud)
    SolicitudServicio->>GeneradorFolios: siguienteFolio()
    GeneradorFolios-->>SolicitudServicio: folio
    SolicitudServicio->>CadenaResponsabilidad: procesar(Solicitud)
    CadenaResponsabilidad-->>SolicitudServicio: validacion
    SolicitudServicio->>SolicitudDAO: guardar(Solicitud)
    SolicitudDAO->>BaseDeDatos: persist()
    BaseDeDatos-->>SolicitudDAO: success
    SolicitudDAO-->>SolicitudServicio: Solicitud persistida
    SolicitudServicio-->>TramiteBean: success
    TramiteBean-->>Usuario: Muestra Mensaje (FacesMessage)
"""
    doc.add_paragraph(mermaid_seq)
    
    doc.add_page_break()

    # 3. Base de Datos
    add_heading(doc, '3. Modelo de Base de Datos')
    doc.add_paragraph('El sistema utiliza Apache Derby. La tabla principal es SOLICITUD_TRAMITE que persiste la entidad Solicitud.')
    
    add_heading(doc, 'Tabla: SOLICITUD_TRAMITE', level=2)
    
    table = doc.add_table(rows=1, cols=4)
    table.style = 'Table Grid'
    hdr_cells = table.rows[0].cells
    hdr_cells[0].text = 'Columna'
    hdr_cells[1].text = 'Tipo de Dato'
    hdr_cells[2].text = 'Nulo'
    hdr_cells[3].text = 'Descripción'
    
    columns = [
        ('ID', 'BIGINT (PK)', 'No', 'Identificador autoincremental'),
        ('NUM_CUENTA', 'VARCHAR(7)', 'No', 'Número de cuenta del alumno'),
        ('TIPO_TRAMITE', 'VARCHAR(20)', 'No', 'Tipo de trámite solicitado'),
        ('MOTIVO', 'VARCHAR(255)', 'Sí', 'Descripción o justificación'),
        ('ESTADO', 'VARCHAR(30)', 'No', 'PENDIENTE, APROBADO, etc.'),
        ('RESUELTO_POR', 'VARCHAR(60)', 'Sí', 'Usuario que procesó la solicitud'),
        ('FECHA_REGISTRO', 'TIMESTAMP', 'No', 'Fecha de creación')
    ]
    
    for col in columns:
        row_cells = table.add_row().cells
        row_cells[0].text = col[0]
        row_cells[1].text = col[1]
        row_cells[2].text = col[2]
        row_cells[3].text = col[3]
        
    doc.add_paragraph('\nDiagrama ER (Mermaid):')
    mermaid_er = """
erDiagram
    SOLICITUD_TRAMITE {
        BIGINT ID PK
        VARCHAR NUM_CUENTA
        VARCHAR TIPO_TRAMITE
        VARCHAR MOTIVO
        VARCHAR ESTADO
        VARCHAR RESUELTO_POR
        TIMESTAMP FECHA_REGISTRO
    }
"""
    doc.add_paragraph(mermaid_er)

    output_path = os.path.join(
        os.path.dirname(os.path.abspath(__file__)),
        'Documentacion_SGSA.docx',
    )

    try:
        doc.save(output_path)
    except OSError as exc:
        raise OSError(
            f"No se pudo guardar el documento en '{output_path}': {exc}"
        ) from exc

    logger.info("Documento generado en: %s", output_path)
    return output_path


if __name__ == '__main__':
    logging.basicConfig(
        level=logging.INFO,
        format='%(levelname)s: %(message)s',
    )
    try:
        generate_doc()
    except Exception as exc:
        logger.error("%s", exc)
        sys.exit(1)
