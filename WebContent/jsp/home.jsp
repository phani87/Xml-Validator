<%@page import="com.cnsi.xml.util.XmlReader"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>XMLs</title>
<script type="text/javascript">
function makeEditable(){
	    $('textarea').removeAttr('disabled');
	    /* document.getElementById('saveForm').onsubmit = function() {
	        return false;
	    }; */
}

function save(){
	document.saveForm.submit;
}

function goBack(){
	document.saveForm.submit;
}
</script>

 <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  <link href="../resources/css/main.css" rel="stylesheet">
</head>
<body>
<% String name = (String)session.getAttribute("xml");
   String active_flag = (String)session.getAttribute("active_flag");
   System.out.println("aactive falg"+active_flag);
   String error = (String)session.getAttribute("error");
%>
<% 
		if((error != null) && (error.equalsIgnoreCase("error")) ){
			out.println("<div><p class=\"error\">Error : Malformed XML File, please correct</p></div>");
		}
%>




XML : 
<br>
<form action="../XmlProcessor" id="saveForm" name="saveForm">
<textarea rows="25" cols="100"  id="xml_text" name="xml_content" class="form-control"  style="width: 638px;height: 245px;" disabled><%=name%></textarea> 
<br><button id="save_xml" onclick="save()" name="get_xml" value="save" class="btn btn-primary">Save</button>
<button id="back" onclick="goBack()" class="btn btn-primary">Back</button>
<input type="hidden" id="back" name="get_xml" value="allxml">
<%
if(active_flag.equalsIgnoreCase("A")){
	out.println("<button id=\"edit_xml\" onclick=\"makeEditable()\" class=\"btn btn-primary\" type=\"button\">Edit</button>");
}
%>
</form>









</body>
</html>