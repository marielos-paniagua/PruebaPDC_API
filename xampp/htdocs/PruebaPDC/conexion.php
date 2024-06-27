<?php
$mysql=new mysqli("localhost","root","","pruebapdc");
if($mysql->connect_error){
    die("Error de conexion");
}
else{
    //echo "Conexion exitosa";
}