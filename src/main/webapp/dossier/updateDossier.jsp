<%@ include file="/header.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="/wmesjsp/css/style.css">
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.2/css/all.css"
	integrity="sha384-fnmOCqbTlWIlj8LyTjo7mOUStjsKC4pOpQbqyi7RrhN7udi9RwhKkMHpvLbHG9Sr" crossorigin="anonymous">
<style>
.pre_contenitore {
	width: 320px;
	margin: auto;
	height: 50px;
	border: 1px solid black;
	border-radius: 40px 40px 40px 40px;
	background-color: rgba(0, 0, 0, 0.9);
	box-shadow: 20px 30px 20px #000000;
	padding: 20px;
}

.pre_contenitore p {
	color: white;
	text-align: center;
	font-size: 1.9em;
	font-family: arial;
	line-height: 2px;
}
</style>

<%
	DossierDTO updateDossier = (DossierDTO) request.getAttribute("updateDossier");
	int idAziendaCliente =  (Integer) request.getAttribute("idAzienda");
	final int idBO= (Integer) request.getAttribute("idBO");
%>
</head>

<body>
	<div class="center">

		<div class="pre_contenitore">

			<p>Dossier Update</p>

		</div>
		<br>
		<br>
		<form method="POST" action="/DossierController/updateRedirect"&idBO=<%=idBO%>">

		Anno Di Imposta: <input type="text" size="40" maxlength="40" name="periodoDiImposta" value="0"/>
		<input type="hidden" name="idAzienda" value="<%=idAziendaCliente %>"/>
		<input type="hidden" name="idBOe" value="<%=idBO %>"/>
		<br>
		<br>
		Costo Dipendenti: <input type="text" size="40" maxlength="40" name="costoDipendentiPeriodoDiImposta" value="0"/>
		<br>
		<br>
		fatturato: <input type="text" size="40" maxlength="40" name="fatturatoPeriodoDiImposta" value="0"/>
		<br>
		<br>
		Numero Totale Dipendenti: <input type="text" size="40" maxlength="40" name="numeroTotaleDipendenti" value="0"/>
		<br>
		<br>
		Id progetto: <input type="text" size="40" maxlength="40" name="idProgetto" value="0"/>
		<br>
		<br>
		Id Azienda Cliente: <input type="text" size="40" maxlength="40" name="idAziendaCliente" value="0"/>
		<br>
		<br>
		<input type="SUBMIT" value="Update">
		<br>
		<br>
		<a href="/DossierController/insert&idAzienda=<%=idAziendaCliente %>&idBO=<%=idBO%>"><i class="fas fa-arrow-alt-circle-left fa-lg"> Back</i></a>

		</form>

	</div>
</body>
</html>