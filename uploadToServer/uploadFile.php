<?php
    error_reporting(E_ALL);

    $target_path = $_FILES['file']['name'];
    if(move_uploaded_file($_FILES['file']['tmp_name'], $target_path)) {
        $msg = "File uploaded successfully!";
    } else{
        $msg = "Sorry, file not uploaded, please try again!";
    }

    $json = ["file" => $_FILES, "response" => $msg];
    header('content-type: application/json');
    echo json_encode($json);
?>