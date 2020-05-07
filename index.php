<?php include 'conexion.php';?>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Yotrabajoconpecs</title>
    <base href="/">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" type="image/x-icon" href="favicon.ico">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>

  </head>

  <body>
    <!-- Begin page content -->
      <header class="fondo">
          <img src="img/logo.png">
          
          <style type="text/css">
          header{
            width: 100%;
            height: 250px;
            background: linear-gradient(180deg, rgba(174,235,238,1) 39%, rgba(255,255,255,0.6898876404494382) 76%);
          }

          img{
              margin-left: 30px;
              margin-top: 20px;
              width: 150px;
              height: 150px;
          }
          </style>
      </header> 
    
    <h1 class="container">
    <form method="post">
      <div class="row justify-content-md-center">
        <div class="col-md-auto">
          <p>YO + QUIERO + VERBO + ADJETIVO</p>
          <style type="text/css">
            p{
              font-size: small;
              color: lightslategrey;
            }
          </style>
        </div> 
      </div>
      
    
 
      <div class="row">
      

        <div class="col"> 
          <label class="sr-only" for="inlineFormInput">Curso</label>
          <Input id="inlineFormInput" required name="PalabraClave" type="text" class="form-control" aria-label="With textarea" placeholder="Inserte texto aqui" rows="1%" cols="80%" maxlength="50" autofocus="true" spellcheck="true" wrap="hard"></Input>
          <input name="buscar" type="hidden" class="form-control mb-2" id="inlineFormInput" value="v">
          <style type="text/css">
            textarea{
              width: 100%;
              height: 100%;
              font-size: x-large;
              resize: none;
              margin-right: 10px;
            }
          </style>
        
        </div>  
        
        <div class="col col-lg-2">
          <button type="submit" class="myButton">Buscar Ahora</button>
          <style type="text/css">
            .myButton {
              background-color:#e6d708;
              border-radius:10px;
              display:inline-block;
              cursor:pointer;
              color:#030303;
              font-family:'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif;
              font-size:17px;
              
              text-decoration:none;
              text-shadow:0px 1px 0px #2f6627;
            }

            .myButton:hover {
              background-color:#5cbf2a;
            }

            .myButton:active {
              position:relative;
              top:1px;
            }
          </style>
        </div> 
      </div>
      </form>
      <style type="text/css">
        h1{
          justify-content: center;
        }
      </style>

  

    <?php
    
    if(!empty($_POST))
    {
      $aKeyword = explode(" ", $_POST['PalabraClave']);
      $arr_length = count($aKeyword);
      echo "<p><br>Has buscado la palabra clave:<b> ". $_POST['PalabraClave']."</b></p>";
      
      for($i=0;$i<$arr_length;$i++)
      {

        $query ="SELECT * FROM repo2 WHERE Nombre like '" . $aKeyword[$i] . "'";
        $result = $db->query($query);
                        
        if(mysqli_num_rows($result) > 0) {
          $row_count=0;
          if($row = $result->fetch_assoc()) {                           
            echo "<img src=". $row['Imagen'] ." width='100' height='100'>";
          }
        }
        
        else {
          
          echo "<b> ". $aKeyword[$i]."</b>";
        
        }
      }
    }
    
    ?>
    </h1>

  </body>
</html>