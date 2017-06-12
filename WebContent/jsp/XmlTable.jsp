<%@page import="com.cnsi.xml.util.XmlReader"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.*"%>
<%@page import="com.cnsi.xml.util.XmlDao"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<title>Xml</title>
<script type="text/javascript">
	function submitFormForXml(event) {
		var e = event.target.innerHTML;
		var k = event.target;
		
		document.getElementById("xml_id").value = e;
		document.getElementById("active_flag").value = e;
		document.forms["XmlViewForm"].submit();
	}
	
	function getRowForXML(row){
		var x=row.cells;
		document.getElementById("xml_id").value = x[0].innerHTML;
		document.getElementById("active_flag").value =x[1].innerHTML;
		document.forms["XmlViewForm"].submit();
	}
</script>
</head>
<body>
	<%
		ArrayList<XmlDao> allXML = new ArrayList<XmlDao>();
		allXML = (ArrayList<XmlDao>) session.getAttribute("allXML");
		
	%>
	
	
	<form action="../XmlProcessor" name="XmlViewForm">
		<div class="container">
			<div class="table-responsive">
				<table class="table table-hover" id="xml_table">
					<thead class="thead-default">
						<tr>
							<th>XML ID</th>
							<th>XML ENABLED</th>
							<th>CREATED DATE</th>
							<th>MODIFIED DATE</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${allXML}" var="xml" varStatus="status">
							<tr id="row" onclick="javascript:getRowForXML(this)">
								<td id="x_id"><a href="#">Pipeline ${xml.xml_id}</a></td>
								<td id="a_flag">${xml.xml_enabled}</td>
								<td>${xml.xml_created_date}</td>
								<td>${xml.xml_modified_date}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<input type="hidden" id="xml_id" value="" name="xml_id"> 
			<input type="hidden" id="xml_get_id" value="xml" name="get_xml">
			<input type="hidden" id="active_flag" value="" name="active_flag">
		</div>
	</form>
</body>
</html>