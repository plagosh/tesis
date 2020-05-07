<?php include 'conexion.php'; ?>


<!DOCTYPE html>
<html>
<head>
	<title>mostrar datos</title>
</head>
<body>

<br>

	<table border="1">
		 <tr>
			<td>Id</td>
			<td>FechaHr</td>
			<td>Estado</td>
			<td>CanADD</td>	
		</tr>
<?php
		$sql="SELECT * FROM acutualizarepo ORDER BY acutualizarepo.Id DESC";
		
		$result= $db->query($sql);
		
		if(mysqli_num_rows($result) > 0) {
			$row_count=0;
			
			While($row = $result->fetch_assoc()) {   
				$row_count++;                         
				echo "<tr><td>".$row['Id']." </td><td>". $row['FechaHr'] . "</td><td>". $row['Estado'] . "</td><td>". $row['CanADD'] . "</td></tr>";
			}
			echo "</table>";
		}
	 ?>
	</table>

</body>
</html>