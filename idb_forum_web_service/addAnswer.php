<?php
header('Access-Control-Allow-Origin: *');
if($_SERVER['REQUEST_METHOD']=='POST'){
 $abody = $_POST['abody'];
 $qid = $_POST['qid'];
 $uid = $_POST['uid'];
 
 require_once('dbConnect.php');
 
 
 $sql = "INSERT into ques_ans(ques_id, ans_desc, user_id, ans_time) values( ?, ?, (select user_id from user where user_name=?), now())";
 $stmt = $con->prepare($sql);
 $stmt->bind_param("sss", $qid, $abody, $uid);
 
 if($stmt->execute()){
     $sql2 = "SELECT * FROM question WHERE ques_id='$qid' ";
	$check = mysqli_query($con,$sql2);
	while($e=mysqli_fetch_assoc($check)){
		$comment_count=$e['comment_count'];
	}
	$comment_count = $comment_count+1;
	
		$sql3 = "update question set comment_count='$comment_count' where ques_id= '$qid'";
		mysqli_query($con,$sql3);
    
	
 echo "Successfully Given Answer";
 $stmt->close();
 }
 
 mysqli_close($con);
 }else{
 echo "Error";
 }
  ?>