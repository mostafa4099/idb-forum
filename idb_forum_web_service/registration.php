<?php
 header('Access-Control-Allow-Origin: *');
 if($_SERVER['REQUEST_METHOD']=='POST'){
 
 $image = $_POST['image'];
 $uname = $_POST['uname'];
 $pass = $_POST['pass'];
 $fname = $_POST['fname'];
 $email = $_POST['email'];
 $subject = $_POST['subject'];
 $tag = $_POST['tag'];
 $role='pc';
 $iid=''; $tspname=''; $tid=''; $batchname='';
	if ($tag === 'instructor') {
		$iid = $_POST['iid'];
		$tspname = $_POST['tspname'];
		$role = 'instructors';
	}
	if ($tag === 'trainee') {
		$tid = $_POST['tid'];
		$batchname = $_POST['batchname'];
		$role = 'trainees';
	}
 $id;
 $sql;
 $stmt;
 $stmt2;
 $flag;
 require_once('dbConnect.php');
	$sql2 = "SELECT user_name
				FROM user WHERE user_name = '$uname'";   
	 $check = mysqli_query($con,$sql2);
		while($e=mysqli_fetch_assoc($check)){
			$id=$e['user_name'];
		}
   
	
	if((!isset($id) || trim($id) === '')){
		 $now = round(microtime(true) * 1000); 
		 $path = "profile_image/$now.jpg"; 
		 $actualpath = "http://www.javaknowledge.info/idb_forum/$path";
		 
		 if((!isset($image) || trim($image) === '')){
		 $sql = "INSERT into user(role_id, user_name, password, isactive, full_name, email, subject, instructor_id, tsp_name, trainee_id, batch_name) values( (select role_id from user_role where role_name=?), ?, ?, 1, ?, ?, ?, ?, ?, ?, ?)";
		 $stmt2 = $con->prepare($sql);
		 $stmt2->bind_param("ssssssssss", $role, $uname, $pass, $fname, $email, $subject, $iid, $tspname, $tid, $batchname);
		 }
		 else{
		 $sql = "INSERT into user(role_id, user_name, password, profile_pic, isactive, full_name, email, subject, instructor_id, tsp_name, trainee_id, batch_name) values( (select role_id from user_role where role_name=?), ?, ?, ?, 1, ?, ?, ?, ?, ?, ?, ?)";
		 $stmt2 = $con->prepare($sql);
		 $stmt2->bind_param("sssssssssss", $role, $uname, $pass, $actualpath, $fname, $email, $subject, $iid, $tspname, $tid, $batchname);
		 $flag=2;
		 } 
		 
		 if($stmt2->execute()){
			 if($flag==2){
			 file_put_contents($path,base64_decode($image));
			 }
				
			 echo "Successfully Registered! please login";
			 //$stmt->close();
			 $stmt2->close();
		 } 
		 mysqli_close($con);
	}
	else{
	echo "please choose another user name";
	}
 
 }else{
	echo "Error";
 }