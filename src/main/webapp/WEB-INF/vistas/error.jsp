<%-- Página de error: errores de base de datos (mensajeError) y errores HTTP 400/404. --%>
<%@ page isErrorPage="true" %>
<%@ include file="encabezado.jspf" %>

<div class="errores">
    <h1>No se pudo completar la operación</h1>
    <c:choose>
        <c:when test="${not empty mensajeError}">
            <p><c:out value="${mensajeError}"/></p>
        </c:when>
        <c:otherwise>
            <p>Error ${pageContext.errorData.statusCode}: la página o acción solicitada no existe.</p>
        </c:otherwise>
    </c:choose>
</div>
<a class="boton" href="${pageContext.request.contextPath}/inmuebles">Volver a la lista</a>

<%@ include file="pie.jspf" %>
