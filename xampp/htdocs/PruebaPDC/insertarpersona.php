<?php
if($_SERVER["REQUEST_METHOD"]=="POST"){
    require_once 'conexion.php';
    $nombre=$_POST["nombre"];
    $pais=$_POST["pais"];
    $departamento=$_POST["departamento"];
    $direccion=$_POST["direccion"];
    $query="INSERT INTO persona (nombre,pais,departamento,direccion) VALUES ('".$nombre."','".$pais."','".$departamento."','".$direccion."')";
    $resultado=$mysql->query($query);
    if($resultado==true){
        echo "La persona se inserto de forma exitosa";
    }else{
        echo "Error al insertar a la persona";
    }
}