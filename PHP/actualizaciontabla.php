<?php include 'conexion.php'; ?>


<!DOCTYPE html>
<html>
<head>
	<title>mostrar datos</title>
</head>
<body>

<br>

	<table border="1" >
		<tr>
			<td>Id</td>
			<td>FechaHr</td>
			<td>Estado</td>
			<td>CanADD</td>	
		</tr>

		<?php 
		$sql="SELECT * from t_persona";
		$result=mysqli_query($conexion,$sql);

		while($mostrar=mysqli_fetch_array($result)){
		 ?>

		<tr>
			<td><?php echo $mostrar['Id'] ?></td>
			<td><?php echo $mostrar['FechaHr'] ?></td>
			<td><?php echo $mostrar['Estado'] ?></td>
			<td><?php echo $mostrar['CanADD'] ?></td>
		</tr>
	<?php 
	}
	 ?>
	</table>

</body>
</html>