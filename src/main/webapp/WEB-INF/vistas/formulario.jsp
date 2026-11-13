<%-- Formulario para registrar o editar un inmueble. Recibe del servlet: inmueble, errores. --%>
<%@ include file="encabezado.jspf" %>

<%-- Si el inmueble ya tiene id, el formulario es de edición --%>
<c:set var="esEdicion" value="${inmueble.idInmueble > 0}"/>

<h1>${esEdicion ? 'Editar inmueble ' += inmueble.idInmueble : 'Publicar inmueble'}</h1>

<c:if test="${not empty errores}">
    <div class="errores">
        <b>Revise los datos:</b>
        <ul>
            <c:forEach var="error" items="${errores}">
                <li><c:out value="${error}"/></li>
            </c:forEach>
        </ul>
    </div>
</c:if>

<%-- Formulario HTML por POST: crea o modifica datos en la base de datos --%>
<form class="formulario" action="${pageContext.request.contextPath}/inmuebles" method="post">
    <input type="hidden" name="accion" value="${esEdicion ? 'actualizar' : 'insertar'}">
    <c:if test="${esEdicion}">
        <input type="hidden" name="idInmueble" value="${inmueble.idInmueble}">
    </c:if>

    <label>Título *
        <input type="text" name="titulo" required maxlength="150" value="<c:out value='${inmueble.titulo}'/>">
    </label>

    <label>Descripción
        <textarea name="descripcion" rows="3"><c:out value="${inmueble.descripcion}"/></textarea>
    </label>

    <div class="fila">
        <label>Tipo *
            <select name="tipo" required>
                <option value="">Seleccione</option>
                <c:forEach var="opcion" items="casa,apartamento,finca,lote,local">
                    <option value="${opcion}" ${inmueble.tipo == opcion ? 'selected' : ''}>${opcion}</option>
                </c:forEach>
            </select>
        </label>
        <label>Operación *
            <select name="operacion" required>
                <option value="">Seleccione</option>
                <c:forEach var="opcion" items="venta,arriendo">
                    <option value="${opcion}" ${inmueble.operacion == opcion ? 'selected' : ''}>${opcion}</option>
                </c:forEach>
            </select>
        </label>
    </div>

    <div class="fila">
        <label>Precio (COP) *
            <input type="number" name="precio" required min="1" step="1" value="${inmueble.precio}">
        </label>
        <label>Estado
            <select name="estado">
                <c:forEach var="opcion" items="disponible,reservado,vendido,arrendado">
                    <option value="${opcion}" ${inmueble.estado == opcion ? 'selected' : ''}>${opcion}</option>
                </c:forEach>
            </select>
        </label>
    </div>

    <div class="fila">
        <label>Municipio *
            <input type="text" name="municipio" required maxlength="100" value="<c:out value='${inmueble.municipio}'/>">
        </label>
        <label>Dirección
            <input type="text" name="direccion" maxlength="200" value="<c:out value='${inmueble.direccion}'/>">
        </label>
    </div>

    <div class="botones">
        <button type="submit">${esEdicion ? 'Guardar cambios' : 'Publicar'}</button>
        <a class="boton secundario" href="${pageContext.request.contextPath}/inmuebles">Cancelar</a>
    </div>
</form>

<%@ include file="pie.jspf" %>
