<?php
if($_SERVER["REQUEST_METHOD"]=="POST"){
    require_once 'conexion.php';
    $idpersona=$_POST["idpersona"];
    $nombre=$_POST["nombre"];
    $pais=$_POST["pais"];
    $departamento=$_POST["departamento"];
    $direccion=$_POST["direccion"];
    $query="UPDATE persona SET nombre='".$nombre."',pais='".$pais."',departamento='".$departamento."',direccion='".$direccion."' WHERE idpersona='".$idpersona."'";
    $resultado=$mysql->query($query);
    if($mysql->affected_rows > 0 && $resultado==true){
        echo "La persona se edito de forma exitosa";
    }else{
        echo "Error al editar a la persona";
    }
    $mysql->close();
}