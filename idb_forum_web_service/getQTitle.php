<?php
header('Access-Control-Allow-Origin: *');
  	include("connection.php");
// reading offset from get parameter
$offset = isset($_GET['offset']) && $_GET['offset'] != '' ? $_GET['offset'] : 0;

// page limit
$limit = 10;
  	$mysqli->query("SET NAMES 'utf8'");
  	$sql="SELECT ques_title
	FROM    question order by ques_title";
  	$result=$mysqli->query($sql);
  	while($e=mysqli_fetch_assoc($result)){
        		$output[]=$e; 
  			}
  	
  	print(json_encode($output)); 
  	$mysqli->close();
  ?>