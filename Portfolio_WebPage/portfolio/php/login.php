<?php
    // 일반 페이지에서 넘어왔을 시  차단
    //if(!isset($_POST['ACCESS'])){
        if(!isset($_POST['ACCESS'])){
            echo json_encode(array('ACCESS'=>'denied'));

            //$arr = json_decode(file_get_contents('20190206.json'), true);

            //$json_string = file_get_contents('20190206.json');

            //echo $json_string;

            //echo json_encode($json_string);
            //$json_string = file_get_contents('weather.json');
            //$R = json_decode($json_string, true); // 

            //echo json_encode($R);
            //print_r($R);
            //echo $arr;


            exit;
    }

    // 아이디 또는 비밀번호를 입력하지 않았을 시 
    if(empty($_POST['id'])||empty($_POST['pw'])){
            echo json_encode(array('result'=>'f','msg'=>'아이디 또는 비밀번호를 입력해주세요'));
            exit;
    }

    // 데이터베이스값을 대체하는 변수  
    $get_id = 'user'; // 임의로 정의된 아이디 
    $get_pw = sha1('1234'); // 임의로 정의된 비밀번호 

    // 아이디 불일 치시 
    if($get_id!=$_POST['id']){
            echo json_encode(array('result'=>'f','msg'=>'아이디가 일치하지 않습니다.'));
            exit;
    }

    // 비밀번호 불일치 시 
    if($get_pw!=sha1($_POST['pw'])){
            echo json_encode(array('result'=>'f','msg'=>'비밀번호 일치하지 않습니다.'));
            exit;
    }

    // 로그인이 성공했을 시 
    echo json_encode(array('result'=>'success','msg'=>'로그인에 성공하였습니다.','id'=>$_POST['id']));
    exit;
?>