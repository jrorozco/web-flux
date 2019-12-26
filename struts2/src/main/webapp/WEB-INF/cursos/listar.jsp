<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>
<s:property  value="titulo"/>
<s:date name="fecha" format="dd/MM/yyyy"/>
</h1>

<table>
	<thead>
	<tr>
	<th>Cursos</th>
	</tr>
	</thead>
	<tbody>
	<tr>
	<td>
		<s:iterator value="cursos">
			<tr>
				<td><s:property/></td>	
			</tr>
		</s:iterator>
	</td>
	</tr>
	</tbody>
	
</table>
</body>
</html>