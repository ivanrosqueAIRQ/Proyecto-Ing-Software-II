# SGSA — Sistema de Gestión de Solicitudes Académicas

Proyecto final de **Ingeniería de Software II** (UAEMex, 2026A).
Aplicación web Java EE que automatiza la aprobación de trámites estudiantiles
aplicando arquitectura **N-Capas** y los patrones de diseño **Chain of
Responsibility**, **Command** y **Singleton**.

---

## ⚙️ Tecnologías

| Componente | Versión |
|------------|---------|
| Java JDK | 8 (recomendado) — compatible hasta 11 |
| JSF | 2.3 |
| PrimeFaces | 12 |
| JPA | 2.2 (EclipseLink) |
| Base de datos | Apache Derby |
| Servidor | GlassFish 5 / 6 |
| Build | Maven |
| Pruebas | JUnit 5 + Mockito + JaCoCo |

---

## 📦 IMPORTANTE — Lo que debes instalar y configurar en tu laptop

Este proyecto **sí requiere un servidor de aplicaciones y una base de datos**.
Aquí está todo lo que necesitas dejar listo (te lo dejo explícito como pediste):

### 1. JDK 8
Instala el JDK 8 de Oracle o Temurin. Verifica con:
```bash
java -version
```

### 2. Servidor GlassFish
Descarga GlassFish 5 (para Java EE 8) o 6 y descomprímelo. NetBeans normalmente
lo trae integrado; si usas NetBeans 12+, agrégalo en *Services → Servers*.

### 3. Base de datos Apache Derby
GlassFish ya **trae Derby integrado** (JavaDB). No necesitas instalar nada extra.
Solo tienes que crear el *Connection Pool* y el *Data Source*. Hay dos formas:

#### Opción A — Automática (recomendada): usar el script
Arranca Derby y GlassFish, y desde la carpeta del proyecto ejecuta:
```bash
# 1. Arranca la base de datos Derby (incluida en GlassFish)
asadmin start-database

# 2. Arranca el servidor
asadmin start-domain

# 3. Crea el pool y el datasource con el script incluido
asadmin multimode --file scripts/setup-derby.asadmin
```

#### Opción B — Manual (desde la consola de administración)
1. Entra a `http://localhost:4848` (consola de GlassFish).
2. Ve a *Resources → JDBC → JDBC Connection Pools → New*.
   - **Pool Name:** `DerbyPool`
   - **Resource Type:** `javax.sql.DataSource`
   - **Database Driver Vendor:** `Derby` (selecciona el cliente de red)
   - Propiedades adicionales:
     - `serverName = localhost`
     - `portNumber = 1527`
     - `databaseName = sgsaDB`
     - `connectionAttributes = ;create=true`
     - `user = APP`
     - `password = APP`
   - Da clic en **Ping** para verificar la conexión.
3. Ve a *Resources → JDBC → JDBC Resources → New*.
   - **JNDI Name:** `jdbc/sgsaDB`
   - **Pool Name:** `DerbyPool`

> El `persistence.xml` ya apunta a `jdbc/sgsaDB`. Las tablas se crean solas al
> desplegar (gracias a `schema-generation.action = create`).

---

## ▶️ Cómo ejecutar el proyecto

### Si usas NetBeans (lo más sencillo)
1. Abre NetBeans → *File → Open Project* → selecciona la carpeta `SGSA`.
   (NetBeans reconoce el `pom.xml` como proyecto Maven Web.)
2. Asegúrate de tener GlassFish y Derby configurados (pasos de arriba).
3. Clic derecho en el proyecto → **Run**.
4. Se abrirá el navegador en `http://localhost:8080/SGSA/`.

### Si usas la línea de comandos con Maven
```bash
# Compilar y empaquetar el WAR
mvn clean package

# El WAR queda en target/SGSA.war
# Despliégalo en GlassFish:
asadmin deploy target/SGSA.war
```
Luego abre `http://localhost:8080/SGSA/`.

---

## 🧪 Pruebas unitarias y cobertura (Criterio 3 de la rúbrica)

```bash
# Ejecutar las pruebas y generar el reporte de cobertura
mvn clean test

# Verificar que se cumple el umbral del 80% (falla el build si no)
mvn verify
```

El reporte HTML de JaCoCo se genera en:
```
target/site/jacoco/index.html
```
Ábrelo en el navegador para ver el porcentaje de cobertura por clase.

> **Nota:** el umbral del 80% está configurado en el `pom.xml` sobre la lógica
> de negocio (paquetes `negocio` y `util`). Las capas de UI, controladores y
> persistencia están excluidas del cómputo porque dependen del contenedor y no
> se prueban por unidad.

---

## 📁 Estructura del proyecto

```
SGSA/
├── pom.xml                          → Configuración Maven + JaCoCo
├── README.md
├── .gitignore
├── docs/
│   └── DOCUMENTACION.md             → Planteamiento, requerimientos, casos de uso, manual, plan
├── scripts/
│   └── setup-derby.asadmin          → Script para crear el pool y datasource
└── src/
    ├── main/
    │   ├── java/mx/uaemex/arquitectura/
    │   │   ├── model/               → Entidad Solicitud + enums (Capa de Datos)
    │   │   ├── dao/                  → SolicitudDAO + impl JPA (Capa de Datos)
    │   │   ├── negocio/              → SolicitudServicio (Capa de Negocio)
    │   │   │   ├── chain/            → Patrón Chain of Responsibility
    │   │   │   └── command/          → Patrón Command
    │   │   ├── controller/           → Backing Beans JSF (Capa de Control)
    │   │   └── util/                 → GeneradorFolios (Patrón Singleton)
    │   ├── resources/
    │   │   ├── i18n/                 → messages_es / messages_en (Internacionalización)
    │   │   └── META-INF/persistence.xml
    │   └── webapp/
    │       ├── index.xhtml           → Vista principal (Capa de Presentación)
    │       ├── resources/css/        → Estilos
    │       └── WEB-INF/              → template, web.xml, faces-config, beans.xml
    └── test/
        └── java/mx/uaemex/arquitectura/ → Pruebas unitarias JUnit 5
```

---

## ✅ Mapeo con la rúbrica de evaluación

| Criterio | Punto de la rúbrica | Dónde se cumple |
|----------|---------------------|-----------------|
| **C1 — Modelo** | Mapeo de componentes de BD | `model/Solicitud.java` (`@Entity`, `@Table`, `@Column`) |
| | Funcionalidad de componentes de BD | `dao/SolicitudDAOImpl.java` (CRUD con JPA) |
| | Componentes de negocio | `negocio/SolicitudServicio.java` |
| | Funcionalidad de negocio | Orquestación de patrones en el servicio |
| | Patrones de diseño | `chain/`, `command/`, `util/GeneradorFolios` |
| **C2 — Vista-Controlador** | Componentes UI | `index.xhtml` (PrimeFaces) |
| | Funcionalidad UI | Formulario + tabla + acciones |
| | Manejo de datos/eventos | `controller/TramiteBean.java` |
| | Validaciones | Bean Validation en la entidad + `<p:message>` |
| | Internacionalización | `i18n/` + `MensajeBean` + `faces-config.xml` |
| | Templates | `WEB-INF/template.xhtml` |
| **C3 — CICD** | Versionamiento Git | `.gitignore` + repositorio |
| | Pruebas unitarias + JaCoCo 80% | `src/test/` + plugin JaCoCo en `pom.xml` |
| | Documentación | `docs/DOCUMENTACION.md` |

---

## 👤 Autor
**AIRQ** — Ingeniería en Computación, UAEMex.
Proyecto final ISII 2026A. Profesor: Ing. Julio César Sarandingua Quintero.
