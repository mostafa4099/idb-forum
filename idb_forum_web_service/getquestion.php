<?php
header('Access-Control-Allow-Origin: *');
  	include("connection.php");
// reading offset from get parameter
$offset = isset($_GET['offset']) && $_GET['offset'] != '' ? $_GET['offset'] : 0;

// page limit
$limit = 10;
  	$mysqli->query("SET NAMES 'utf8'");
	
	$quesList_array =array();
	$ques_array = array();
	$tag_array = array();
	
  	$sql="SELECT question.ques_id,
       question.ques_title,
       question.ques_desc,
       question.up_vote,
       question.comment_count,
       question.ques_date,
       user.user_name as user_id,
       question.image_url
       FROM   user
       INNER JOIN
          question
       ON (user.user_id = question.user_id)
       WHERE question.isactive = 1
ORDER BY question.ques_date DESC";
  	$result=$mysqli->query($sql);
  	while($e=mysqli_fetch_assoc($result)){
				$ques_array['ques_id'] = $e['ques_id'];
				$ques_array['ques_title'] = $e['ques_title'];
				$ques_array['ques_desc'] = $e['ques_desc'];
				$ques_array['up_vote'] = $e['up_vote'];
				$ques_array['comment_count'] = $e['comment_count'];
				$ques_array['ques_date'] = $e['ques_date'];
				$ques_array['user_id'] = $e['user_id'];
				$ques_array['image_url'] = $e['image_url'];
				$ques_array['tags'] = array();
				
				$sql_tags="SELECT ques_tag.tag_id, ques_tag.tag_name, ques_tag.tag_color
				FROM ques_tag_link
				INNER JOIN
				ques_tag
				ON (ques_tag_link.tag_id = ques_tag.tag_id)
				WHERE ques_id  = ".$e['ques_id']."";
				$result2=$mysqli->query($sql_tags);
				while ($row_tags = mysqli_fetch_assoc($result2)) {
					$tag_array['tag_id'] = $row_tags['tag_id'];
					$tag_array['tag_name'] = $row_tags['tag_name'];
					$tag_array['tag_color'] = $row_tags['tag_color'];
					array_push($ques_array['tags'],$tag_array);
				}
        		//$output[]=$e; 
				array_push($quesList_array,$ques_array);
  			}
  	
  	print(json_encode($quesList_array)); 
	//$jsonData = json_encode($quesList_array);
	//echo $jsonData;
  	$mysqli->close();
  ?>