<?php
if($_SERVER["REQUEST_METHOD"]=="GET"){
    require_once 'conexion.php';
    $query="SELECT p.Pais,d.Depto,p.NomPais,d.NomDepto FROM pais p INNER JOIN departamento d ON p.pais=d.pais ORDER BY p.pais";
    $resultado=$mysql->query($query);
    if($mysql->affected_rows > 0){
        $json="{\"data\":[";
        while($row=$resultado->fetch_assoc()){
            $json=$json.json_encode($row);
            $json=$json.",";
        }
        $json=substr(trim($json),0,-1);
        $json=$json."]}"; 
    }
    echo $json;
    $resultado->close();
    $mysql->close();
}