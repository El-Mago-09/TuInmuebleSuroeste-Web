<%-- Lista de inmuebles. Recibe del servlet: inmuebles, municipioBuscado (opcional). --%>
<%@ include file="encabezado.jspf" %>

<c:if test="${not empty param.mensaje}">
    <p class="aviso"><c:out value="${param.mensaje}"/></p>
</c:if>

<div class="barra">
    <h1>Inmuebles publicados</h1>
    <%-- Formulario HTML por GET: la búsqueda no cambia datos y se puede compartir en la URL --%>
    <form action="${pageContext.request.contextPath}/inmuebles" method="get" class="buscador en-linea">
        <input type="hidden" name="accion" value="buscar">
        <input type="text" name="municipio" placeholder="Buscar por municipio" maxlength="100"
               value="<c:out value='${municipioBuscado}'/>">
        <button type="submit">Buscar</button>
        <c:if test="${not empty municipioBuscado}">
            <a class="boton secundario" href="${pageContext.request.contextPath}/inmuebles">Ver todos</a>
        </c:if>
    </form>
</div>

<c:choose>
    <c:when test="${empty inmuebles}">
        <p class="vacio">No hay inmuebles<c:if test="${not empty municipioBuscado}"> en <c:out value="${municipioBuscado}"/></c:if>.</p>
    </c:when>
    <c:otherwise>
        <p class="total">${inmuebles.size()} inmueble(s)</p>
        <table class="tabla">
            <thead>
            <tr><th>Id</th><th>Título</th><th>Tipo</th><th>Operación</th><th>Precio</th><th>Municipio</th><th>Estado</th><th>Acciones</th></tr>
            </thead>
            <tbody>
            <c:forEach var="inmueble" items="${inmuebles}">
                <tr>
                    <td>${inmueble.idInmueble}</td>
                    <td><c:out value="${inmueble.titulo}"/></td>
                    <td><c:out value="${inmueble.tipo}"/></td>
                    <td><c:out value="${inmueble.operacion}"/></td>
                    <td class="precio">$ <fmt:formatNumber value="${inmueble.precio}" pattern="#,##0"/></td>
                    <td><c:out value="${inmueble.municipio}"/></td>
                    <td><span class="estado ${inmueble.estado}"><c:out value="${inmueble.estado}"/></span></td>
                    <td class="acciones">
                        <a class="boton secundario" href="${pageContext.request.contextPath}/inmuebles?accion=editar&amp;id=${inmueble.idInmueble}">Editar</a>
                        <%-- Eliminar cambia datos: se envía por POST, nunca con un enlace GET --%>
                        <form action="${pageContext.request.contextPath}/inmuebles" method="post"
                              onsubmit="return confirm('¿Eliminar el inmueble ${inmueble.idInmueble}?');">
                            <input type="hidden" name="accion" value="eliminar">
                            <input type="hidden" name="id" value="${inmueble.idInmueble}">
                            <button type="submit" class="peligro">Eliminar</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>

<a class="boton" href="${pageContext.request.contextPath}/inmuebles?accion=nuevo">+ Publicar inmueble</a>

<%@ include file="pie.jspf" %>
