<%@ include file="/common/taglib.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<link rel="icon" type="image/x-icon" href="${contextPath}/resources/img/favicon.ico" />
<title>Home</title>

<link rel='stylesheet' href='${contextPath}/resources/assets/bootstrap-3.3.7-dist/css/bootstrap.min.css' type='text/css' media='all' />
<link rel='stylesheet' href='${contextPath}/resources/assets/font-awesome-4.7.0/css/font-awesome.min.css' type='text/css' media='all' />
<link rel='stylesheet' href='${contextPath}/resources/css/all.css?ver=0.1' type='text/css' media='all' />

<link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">
<link href="https://fonts.googleapis.com/css?family=PT+Sans" rel="stylesheet">

</head>

<body class="modiphius-bg">
	<div class="header"></div>

	<div id="chart" class="chartClass" style="width:50%;height:50%;margin-left:25%;"></div>

	<div class="footer"></div>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.6/moment.min.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.37/js/bootstrap-datetimepicker.min.js"></script>

	<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
	<script type='text/javascript' src='${contextPath}/resources/js/script.js?ver=0.1'></script>
	<script type="text/javascript">
		google.charts.load('current', {
			packages : [ 'corechart', 'line' ]
		});
		google.charts.setOnLoadCallback(drawBasic);

		var u = window.location.href;

		var i = u.indexOf("byDate");

		if (i !== -1) {
			u = 'B' + u.substring(i + 1);
		} else {
			u="";
		}

		function drawBasic() {
			$.ajax({
				url : "http://localhost:8080/account/rest/getChart" + u,
				type : 'get',
				dataType : "json",
				success : function(jsonData) {
					var data = new google.visualization.DataTable();
					// assumes "word" is a string and "count" is a number
					data.addColumn('string', 'date');
					data.addColumn('number', 'value');

					for (var i = jsonData.length-1; i >= 0; --i) {
						data.addRow([ jsonData[i].date, jsonData[i].value ]);
					}

					var options = {
						title : 'Stock quates',
						legend : {
							position : 'none'
						},
						colors : [ '#58bec7' ],
						histogram : {
							lastBucketPercentile : 5
						},
						explorer : {
							actions : [ 'dragToZoom', 'rightClickToReset' ],
							axis : 'horizontal',
							keepInBounds : true
						},
						hAxis : {
							title : 'Date'
						},
						pointSize : 3,
						vAxis : {
							title : 'Value'
						}
					};

					var chart = new google.visualization.AreaChart(document
							.getElementById('chart'));
					chart.draw(data, options);
				},
				error : function() {
					alert('error!');
				}
			});
		}
	</script>

	<script type="text/javascript">
		$(function() {
			$('#datetimepicker6').datetimepicker({
				format : 'YYYY-MM-DD'
			});
			$('#datetimepicker7').datetimepicker({
				useCurrent : false,
				format : 'YYYY-MM-DD'
			//Important! See issue #1075
			});
			$("#datetimepicker6").on("dp.change", function(e) {
				$('#datetimepicker7').data("DateTimePicker").minDate(e.date);
			});
			$("#datetimepicker7").on("dp.change", function(e) {
				$('#datetimepicker6').data("DateTimePicker").maxDate(e.date);
			});
		});
	</script>

	<script type="text/javascript">
		function getByDate() {
			var start = $('#start_date').val();
			var end = $('#end_date').val();
			window.location.href = 'http://localhost:8080/account/stock/byDate?start='
					+ start + '&end=' + end;
		}
	</script>
</body>
</html>