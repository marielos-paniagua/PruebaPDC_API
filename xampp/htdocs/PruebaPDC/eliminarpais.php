<?php
if($_SERVER["REQUEST_METHOD"]=="POST"){
    require_once 'conexion.php';
    $pais=$_POST['pais'];
    $query="DELETE FROM pais WHERE pais='".$pais."'";
    $resultado=$mysql->query($query);
    if($mysql->affected_rows > 0){
        if($resultado==true){
            echo "País eliminado exitosamente";
        }
    }else{
        echo "Error al eliminar el país";
    }
    $mysql->close();
}