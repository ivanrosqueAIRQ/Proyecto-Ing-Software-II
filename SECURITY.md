# Política de Seguridad — SGSA

## Dependencias

| Dependencia | Versión actual | Estado |
|-------------|---------------|--------|
| Apache Derby | 10.14.2.0 | **CVE-2022-46337** (LDAP injection, CVSS 9.8) — suprimido; no habilitar autenticación LDAP. Migrar a Java 11+ y Derby 10.15.2.1 cuando sea posible. |
| PrimeFaces | 12.0.0 | XSS en componentes Chip, CommandButton, etc. Actualizar a 12.0.10-LTS o superior. |

Ejecuta `mvn org.owasp:dependency-check-maven:check` para escanear CVEs.

## Credenciales

- **Nunca** hardcodees contraseñas en el código ni en archivos de configuración versionados.
- Usa variables de entorno (`DERBY_USER`, `DERBY_PASSWORD`) o un archivo `.env` local.
- El `.gitignore` ya excluye `.env*`, `*.secret`, `*.pem`, `*.key`, `*.p12`, `*.jks`.

## Desarrollo seguro

1. **SQL Injection:** Usa exclusivamente consultas JPA parametrizadas (`TypedQuery` con parámetros con nombre). Nunca concatenes input del usuario en queries.
2. **XSS:** Usa `h:outputText` (que escapa por defecto) y `EscapeUtils.forHtml` de PrimeFaces para cualquier output dinámico.
3. **CSRF:** JSF incluye protección CSRF por defecto (`javax.faces.ViewState`). No la deshabilites.
4. **Autenticación:** Configura security constraints en `web.xml` antes de desplegar. No dejes endpoints sin protección.
5. **CORS:** Si necesitas CORS, configura orígenes permitidos explícitamente. Nunca uses `Access-Control-Allow-Origin: *` en producción.
6. **Headers de seguridad:** Agrega un filtro servlet que establezca `X-Content-Type-Options: nosniff`, `X-Frame-Options: DENY`, `Strict-Transport-Security`, y `Content-Security-Policy`.
7. **HTTPS:** Usa TLS en producción. Nunca expongas la consola de administración de GlassFish (`:4848`) públicamente.

## Reportar vulnerabilidades

Si encuentras una vulnerabilidad, abre un Issue privado o contacta al autor directamente.
