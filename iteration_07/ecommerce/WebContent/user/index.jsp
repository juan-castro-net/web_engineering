<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
<title>eCommerce</title>
</head>
<body>
	<!-- Navigation -->
	<jsp:include page="../portal/menu.jsp" />
	<!-- Page Content -->
	<div class="container">
		<br /> <br /> <br />
		<div class="list-group">
			<%
				String usuario = request.getUserPrincipal().getName();
				out.println("<h3>Bienvenido: " + usuario + "</h3>");
				if (request.isUserInRole("CLIENT")) {
			%>
			<a href="../order" class="list-group-item list-group-item-action">
				Pedidos</a>
			<%
				}
				if (request.isUserInRole("ADMINISTRATOR")) {
			%>
			<a href="../category" class="list-group-item list-group-item-action">
				Gestión de Categorías</a>
			<%
				}
			%>
		</div>
		<br /> <br />
	</div>
	<!-- /.container -->
	<!-- Footer -->
	<jsp:include page="../portal/footer.jsp" />
	<!-- jQuery first, then Popper.js, then Bootstrap JS -->
	<script src="https://code.jquery.com/jquery-3.3.1.js"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>