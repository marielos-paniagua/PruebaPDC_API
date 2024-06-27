<?php
if($_SERVER["REQUEST_METHOD"]=="POST"){
    require_once 'conexion.php';
    $idpersona=$_POST['idpersona'];
    $query="DELETE FROM persona WHERE idpersona='".$idpersona."'";
    $resultado=$mysql->query($query);
    if($mysql->affected_rows > 0){
        if($resultado==true){
        echo "Persona eliminada exitosamente";
        }
    }else{
        echo "Error al eliminar a la persona";
    }
    $mysql->close();
}