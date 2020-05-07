<?php
$servidor= "localhost";
$usuario= "paulina";
$password = "4bd65f797b*";
$nombreBD= "accionin_yotrabajoconpecs";
$db = new mysqli($servidor, $usuario, $password, $nombreBD);
if ($db->connect_error) {
    die("la conexión ha fallado: " . $db->connect_error);
}
if (!$db->set_charset("utf8")) {
    printf("Error al cargar el conjunto de caracteres utf8: %s\n", $db->error);
    exit();
} else {
    #printf("Conjunto de caracteres actual: %s\n", $db->character_set_name());
}
?>