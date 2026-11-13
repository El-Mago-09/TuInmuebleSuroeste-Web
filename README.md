# Tu Inmueble Suroeste – Módulo web de inmuebles (Servlets y JSP)

Evidencia **GA7-220501096-AA2-EV02 – Módulos de software codificados y probados**
Aprendiz: **Ometh Martinez Potes** · Análisis y Desarrollo de Software (ADSO) · Ficha 3336217 · SENA 2026

Módulo web de gestión de inmuebles de la plataforma *Tu Inmueble Suroeste*. Usa
formularios HTML que envían datos a un **servlet** por **GET** y **POST**, y muestra los
resultados en páginas **JSP** con JSTL. Reutiliza la capa de datos JDBC de la evidencia
GA7-220501096-AA2-EV01 (`InmuebleDAO`) sobre la base de datos MySQL del proyecto.

## Tecnologías

| Elemento | Versión |
|---|---|
| Java (JDK) | 21 |
| Servidor web | Apache Tomcat 10.1 (Jakarta Servlet 6.0, JSP 3.1) |
| Vistas | JSP + JSTL 3.0 (`jakarta.tags.core`, `jakarta.tags.fmt`) |
| Base de datos | MySQL Server 8.0 + MySQL Workbench 8.0 |
| Conector JDBC | MySQL Connector/J 26.10.0 |
| Versionamiento | Git + GitHub |

## Estructura

```
TuInmuebleSuroeste-Web/
├── sql/                                      scripts de la base de datos
├── src/main/java/com/tuinmueble/
│   ├── conexion/ConexionBD.java              conexión JDBC (lee db.properties)
│   ├── conexion/CierreConexionesListener.java libera el driver al detener la app
│   ├── controlador/InmuebleServlet.java      servlet: recibe GET y POST
│   ├── dao/InmuebleDAO.java                  interfaz CRUD
│   ├── dao/InmuebleDAOImpl.java              CRUD con JDBC y PreparedStatement
│   └── modelo/Inmueble.java                  entidad de la tabla inmuebles
├── src/main/resources/db.properties.example  plantilla de configuración
├── src/main/webapp/
│   ├── index.html                            página inicial con formulario HTML (GET)
│   ├── css/estilos.css
│   └── WEB-INF/
│       ├── web.xml                           UTF-8, página de inicio y páginas de error
│       ├── lib/                              conector MySQL y JSTL
│       └── vistas/                           lista.jsp, formulario.jsp, error.jsp,
│                                             encabezado.jspf, pie.jspf
└── construir.bat                             compila y genera dist/tuinmueble.war
```

## Formularios HTML con servlets: GET y POST

Todas las peticiones llegan a `InmuebleServlet` (`/inmuebles`), que decide qué hacer con el
parámetro `accion`:

| Método | Formulario o enlace | `accion` | Parámetros | Qué hace |
|---|---|---|---|---|
| GET | enlace "Inmuebles" | (ninguna) | – | Lista todos los inmuebles |
| GET | buscador de `index.html` y `lista.jsp` | `buscar` | `municipio` | Consulta por municipio |
| GET | botón "Publicar" | `nuevo` | – | Muestra el formulario vacío |
| GET | botón "Editar" | `editar` | `id` | Muestra el formulario con los datos |
| POST | `formulario.jsp` | `insertar` | título, tipo, operación, precio, municipio… | `INSERT` |
| POST | `formulario.jsp` | `actualizar` | `idInmueble` + los mismos campos | `UPDATE` |
| POST | botón "Eliminar" de `lista.jsp` | `eliminar` | `id` | `DELETE` |

Criterio usado: **GET** para consultar (no cambia datos, la búsqueda queda en la URL y se
puede compartir) y **POST** para todo lo que modifica la base de datos. Después de cada
POST el servlet redirige a la lista (patrón *Post/Redirect/Get*), así recargar la página no
repite la operación.

## Elementos de JSP utilizados

| Elemento | Dónde |
|---|---|
| Directiva `page` (`contentType`, `isErrorPage`) | `encabezado.jspf`, `error.jsp` |
| Directiva `include` (encabezado y pie comunes) | todas las vistas |
| Directiva `taglib` (JSTL core y fmt) | `encabezado.jspf` |
| Expression Language `${...}` | atributos enviados por el servlet, `param`, `pageContext` |
| `<c:forEach>`, `<c:if>`, `<c:choose>`, `<c:set>` | lista, formulario y opciones de los `select` |
| `<c:out>` (escapa HTML y evita inyección de código) | todo texto escrito por el usuario |
| `<fmt:setLocale>` y `<fmt:formatNumber>` | precios con formato colombiano: $ 450.000.000 |
| Comentarios JSP `<%-- --%>` | todas las vistas |

Las JSP están dentro de `WEB-INF/vistas`, por lo que no se pueden abrir directamente:
solo se llega a ellas a través del servlet (patrón MVC: modelo `Inmueble`, vista JSP,
controlador `InmuebleServlet`).

## Estándar de codificación

| Elemento | Convención | Ejemplos |
|---|---|---|
| Paquetes | minúsculas, dominio invertido | `com.tuinmueble.controlador` |
| Clases | PascalCase, sustantivos | `InmuebleServlet`, `ConexionBD` |
| Métodos | camelCase, verbo + complemento | `listarInmuebles()`, `guardarInmueble()` |
| Variables y atributos | camelCase, nombres descriptivos | `idInmueble`, `municipioBuscado` |
| Constantes | MAYÚSCULAS_CON_GUION_BAJO | `VISTA_LISTA`, `SQL_INSERTAR` |
| Parámetros HTTP y vistas JSP | camelCase / minúsculas | `idInmueble`, `formulario.jsp` |
| Commits | prefijo `feat:`, `fix:`, `docs:`, `style:`, `refactor:` en minúsculas | `feat: agrega servlet de inmuebles` |

Sangría de 4 espacios, UTF-8, finales de línea LF y sin credenciales en el código.

## Configuración y ejecución

1. Crear la base de datos con los scripts de `sql/` en MySQL Workbench (ver la evidencia
   AA2-EV01: `00_crear_usuario_app.sql`, `01_Tu_Inmueble_Suroeste.sql`, `02_datos_prueba.sql`).
2. Copiar `src/main/resources/db.properties.example` como `db.properties` y escribir la
   contraseña del usuario `tuinmueble_app`.
3. Descargar Apache Tomcat 10.1 (https://tomcat.apache.org) y descomprimirlo.
4. Compilar: `construir.bat "C:\ruta\apache-tomcat-10.1.61"` → genera `dist\tuinmueble.war`.
5. Copiar `dist\tuinmueble.war` en la carpeta `webapps` de Tomcat y ejecutar `bin\startup.bat`.
6. Abrir http://localhost:8080/tuinmueble/

## Pruebas realizadas (13/11/2026, Tomcat 10.1.61, MySQL 8.0.46)

| # | Prueba | Resultado |
|---|---|---|
| 1 | GET `/inmuebles` | Lista los 2 inmuebles de prueba con precios $ 450.000.000 y $ 1.200.000 |
| 2 | POST insertar "Casa finca en Jardín con vista a la montaña" desde el formulario | Redirige con "Inmueble registrado con id 7"; tildes y ñ guardadas bien |
| 3 | GET buscar `municipio=Jardín` | Devuelve solo el inmueble 7 |
| 4 | GET editar `id=7` | Formulario lleno con los datos guardados |
| 5 | POST actualizar precio 595.000.000 y estado reservado | "Inmueble 7 actualizado", cambio visible en la lista |
| 6 | POST eliminar `id=7` | "Inmueble 7 eliminado", vuelven a quedar 2 |
| 7 | POST sin título y precio −5 | No guarda; muestra "El título es obligatorio" y "El precio debe ser mayor que cero" |
| 8 | POST precio "abc" | No guarda; muestra "Escriba un precio válido" |
| 9 | Título con `<script>` | Se muestra escapado (`&lt;script&gt;`), no se ejecuta |
| 10 | GET editar `id=999` y `id=abc` | Redirige con "El inmueble solicitado no existe" |
| 11 | POST eliminar `id=999` | "El inmueble no existe; no se eliminó" |
| 12 | GET `accion=eliminar` | No borra nada: eliminar solo se acepta por POST |
| 13 | POST con acción desconocida | Error 400 con la página de error del proyecto |
| 14 | URL inexistente | Error 404 con la página de error del proyecto |

## Repositorio

https://github.com/El-Mago-09/TuInmuebleSuroeste-Web
