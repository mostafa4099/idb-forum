<?php
 header('Access-Control-Allow-Origin: *');
 if($_SERVER['REQUEST_METHOD']=='POST'){
 
 $image = $_POST['image'];
 $qbody = $_POST['qbody'];
 $qtitle = $_POST['qtitle'];
 $uid = $_POST['uid'];
 $receiver_tag=$_POST['tagarray'];
 $tag_array=json_decode($receiver_tag,true);
 //$qtitle=json_decode($qtitle,true);
 $id;
 $sql;
 $stmt;
 $flag;
 require_once('dbConnect.php');
 
 //$sql ="SELECT id FROM user_info ORDER BY id ASC";
 
 //$res = mysqli_query($con,$sql);
 
 //$id = 0;
 
 //while($row = mysqli_fetch_array($res)){
 //$id = $row['id'];
 //}
 $now = round(microtime(true) * 1000);
 
 $path = "uploads/$now.jpg";
 
 $actualpath = "http://www.javaknowledge.info/idb_forum/$path";
 
 //$sql = "INSERT into question(ques_title, ques_desc, ques_date, user_id, image_url) values( '$qtitle', '$qbody', now(), 1, '$actualpath' )";
 if((!isset($image) || trim($image) === '')){
 $sql = "INSERT into question(ques_title, ques_desc, ques_date, user_id) values( ?, ?, now(), (select user_id from user where user_name=?))";
 $stmt = $con->prepare($sql);
 $stmt->bind_param("sss", $qtitle, $qbody, $uid);
 }
 else{
 $sql = "INSERT into question(ques_title, ques_desc, ques_date, user_id, image_url) values( ?, ?, now(), (select user_id from user where user_name=?), ? )";
 $stmt = $con->prepare($sql);
 $stmt->bind_param("ssss", $qtitle, $qbody, $uid, $actualpath);
 $flag=2;
 }
 
 
 if($stmt->execute()){
 if($flag==2){
 file_put_contents($path,base64_decode($image));
 }
    $sql2 = "SELECT * FROM question WHERE ques_title='$qtitle' ";
	$check = mysqli_query($con,$sql2);
	while($e=mysqli_fetch_assoc($check)){
		$id=$e['ques_id'];
	}
	foreach($tag_array as $row)
	{
		//$row;
		$sql3 = "insert into ques_tag_link(ques_id, tag_id) values ('$id',(select tag_id from ques_tag where tag_name='$row'))";
		mysqli_query($con,$sql3);
	}	
	
 echo "Successfully Uploaded";
 $stmt->close();
 }
 
 mysqli_close($con);
 }else{
 echo "Error";
 }