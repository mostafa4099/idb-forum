<?php
header('Access-Control-Allow-Origin: *');
  	include("connection.php");
// reading offset from get parameter
$offset = isset($_GET['offset']) && $_GET['offset'] != '' ? $_GET['offset'] : 0;
$qid= $_GET['qid'];
// page limit
$limit = 10;
  	$mysqli->query("SET NAMES 'utf8'");
  	$sql="SELECT ques_ans.ans_id,
       ques_ans.ans_desc,
       ques_ans.up_vote,
       user.user_name as user_id,
       ques_ans.ans_time
  FROM user
       INNER JOIN
          ques_ans
       ON (user.user_id = ques_ans.user_id)
 WHERE (ques_ans.ques_id = '$qid') and ques_ans.isactive = 1 order by up_vote, ans_time ";
  	$result=$mysqli->query($sql);
  	while($e=mysqli_fetch_assoc($result)){
        		$output[]=$e; 
  			}
  	
  	print(json_encode($output)); 
  	$mysqli->close();
  ?>