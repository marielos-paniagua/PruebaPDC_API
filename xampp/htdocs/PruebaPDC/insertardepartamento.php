<?php
if($_SERVER["REQUEST_METHOD"]=="POST"){
    require_once 'conexion.php';
    $pais=$_POST["pais"];
    $depto=$_POST["depto"];
    $nomdepto=$_POST["nomdepto"];
    $query="INSERT INTO departamento (pais,depto,nomdepto) VALUES ('".$pais."','".$depto."','".$nomdepto."')";
    $resultado=$mysql->query($query);
    if($resultado==true){
        echo "El departamento se inserto de forma exitosa";
    }else{
        echo "Error al insertar el departamento";
    }
}