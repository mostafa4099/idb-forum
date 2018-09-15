<?php
header('Access-Control-Allow-Origin: *');
if($_SERVER['REQUEST_METHOD']=='POST'){
$name = $_POST['name'];
$pass = $_POST['pass'];
$stmt;
$role_name;
require_once('dbConnect.php');

   $stmt = $con->prepare("SELECT role_name
				FROM user INNER JOIN user_role
				ON (user.role_id = user_role.role_id)
				WHERE (user.user_name = ? and password= ? and isactive=1)"); 

   $stmt->bind_param("ss", $name, $pass); 
   if($stmt->execute()){ 
   $stmt->bind_result($role_name);

   while ($stmt->fetch()) {
     echo "$role_name";
   }

   $stmt->close();

}else{
echo 'error';
}
}
?>