<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=request.getContextPath()%>/html/js/jquery-3.3.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/html/js/jquery-ui.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/html/css/jquery-ui.min.css">
<script>
	$( function() {
		$( "#startDate" ).datepicker({dateFormat : "yy-mm-dd"});
		$( "#endDate" ).datepicker({dateFormat : "yy-mm-dd"});
	} );
 </script>
<title>ReportDownload</title>
</head>
<body>
	<form method="Post" action="/JasperReportExample/ReportDownload">
		<p>起始時間：<input type="text" id="startDate" name="startDate" autocomplete="off"></p>
		<p>結束時間：<input type="text" id="endDate" name="endDate" autocomplete="off"></p>
		<p>報表類型：
			<select name="reportType">
				<option value="PDF">PDF</option>
				<option value="Excel">Excel</option>
			</select>
		</p>
		<input type="submit" value="下載">
	</form>
</body>
</html>