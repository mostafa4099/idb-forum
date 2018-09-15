<?php 

	define('HOST','localhost');
	define('USER','root');
	define('PASS','123');
	define('DB','idb_forum');
	
	$con = mysqli_connect(HOST,USER,PASS,DB) or die('unable to connect to db');