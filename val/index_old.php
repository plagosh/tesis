<?php include 'conexion.php';?>
<!doctype html>
<html lang="es">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="favicon.ico">
<title>Unir dos tablas y buscar con PHP & MySQL - BaulPHP</title>
<!-- Bootstrap core CSS -->
<link href="dist/css/bootstrap.min.css" rel="stylesheet">
<!-- Custom styles for this template -->
<link href="assets/css/sticky-footer-navbar.css" rel="stylesheet">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css" />


  </head>

  <body>

    

    <!-- Begin page content -->

<div class="container">
 <h4 class="mt-5">Buscador avanzado con PHP & MySQL</h4>
 <hr>

<div class="row">
  <div class="col-12 col-md-12">
<!-- Contenido -->   



<ul class="list-group">
  <li class="list-group-item">
<form method="post">
  <div class="form-row align-items-center">
    <div class="col-auto">
      <label class="sr-only" for="inlineFormInput">Curso</label>
      <input required name="PalabraClave" type="text" class="form-control mb-2" id="inlineFormInput" placeholder="Ingrese palabra clave">  
      <input name="buscar" type="hidden" class="form-control mb-2" id="inlineFormInput" value="v">
    </div>
   
    <div class="col-auto">
      <button type="submit" class="btn btn-primary mb-2">Buscar Ahora</button>
    </div>
  </div>
</form>
  </li>

</ul>


<?php
 
if(!empty($_POST))
{
	$aKeyword = explode(" ", $_POST['PalabraClave']);
	$arr_length = count($aKeyword);
	for($i=0;$i<$arr_length;$i++)
	{

		$query ="SELECT * FROM repo WHERE Nombre like '" . $aKeyword[$i] . "'";
		$result = $db->query($query);
		echo "<br>Has buscado la palabra clave:<b> ". $_POST['PalabraClave']."</b>";
                     
		if(mysqli_num_rows($result) > 0) {
			$row_count=0;
			echo "<br><br>Resultados encontrados: ";
			echo "<br><table class='table table-striped'>";
			if($row = $result->fetch_assoc()) {                           
				echo "<tr><td>".$i." </td><td>". $row['Nombre'] . "</td><td><img src=". $row['Imagen'] . "></td></tr>";
			}
			echo "</table>";
		}
    
		else {
			echo "<br>Resultados encontrados: Ninguno";
		
		}
	}
}
 
?>




<!-- Fin Contenido --> 
</div>
</div><!-- Fin row -->
</div><!-- Fin container -->
    

 <!--   ================================================== -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script>window.jQuery || document.write('<script src="assets/js/vendor/jquery-slim.min.js"><\/script>')</script>
    <script src="assets/js/vendor/popper.min.js"></script>
    <script src="dist/js/bootstrap.min.js"></script>
    </body>
</html>