<?php
if($_SERVER["REQUEST_METHOD"]=="POST"){
    require_once 'conexion.php';
    $pais=$_POST['pais'];
    $depto=$_POST['depto'];
    $query="DELETE FROM departamento WHERE pais='".$pais."' and depto='".$depto."'";
    $resultado=$mysql->query($query);
    if($mysql->affected_rows > 0){
        if($resultado==true){
            echo "Departamento eliminado exitosamente";
        }
    }else{
        echo "Error al eliminar el departamento";
    }
    $mysql->close();
}