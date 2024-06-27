<?php
if($_SERVER["REQUEST_METHOD"]=="POST"){
    require_once 'conexion.php';
    $pais=$_POST["pais"];
    $nompais=$_POST["nompais"];
    $query="UPDATE pais SET nompais='".$nompais."' WHERE pais='".$pais."'";
    $resultado=$mysql->query($query);
    if($mysql->affected_rows > 0 && $resultado==true){
        echo "El pais se edito de forma exitosa";
    }else{
        echo "Error al editar el pais";
    }
    $mysql->close();
}