<?php
if($_SERVER["REQUEST_METHOD"]=="POST"){
    require_once 'conexion.php';
    $pais=$_POST["pais"];
    $nompais=$_POST["nompais"];
    $query="INSERT INTO pais (pais,nompais) VALUES ('".$pais."','".$nompais."')";
    $resultado=$mysql->query($query);
    if($resultado==true){
        echo "El pais se inserto de forma exitosa";
    }else{
        echo "Error al insertar el pais";
    }
}