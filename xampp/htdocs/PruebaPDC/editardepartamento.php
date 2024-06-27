<?php
if($_SERVER["REQUEST_METHOD"]=="POST"){
    require_once 'conexion.php';
    $pais=$_POST["pais"];
    $depto=$_POST["depto"];
    $nomdepto=$_POST["nomdepto"];
    $query="UPDATE departamento SET nomdepto='".$nomdepto."' WHERE pais='".$pais."' and depto='".$depto."'";
    $resultado=$mysql->query($query);
    if($mysql->affected_rows > 0 && $resultado==true){
        echo "El departamento se edito de forma exitosa";
    }else{
        echo "Error al editar el departamento";
    }
    $mysql->close();
}