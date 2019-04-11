var song = new Audio();
var musicList;
var isSuffling = false;


var main_title = document.getElementById("main_title");
var playlist = document.getElementById("playlist");
var songTitle = document.getElementById("songTitle");
var songArtist = document.getElementById("songArtist");
var songPlayTime = document.getElementById("songPlayTime");
var songDurationTime = document.getElementById("songDurationTime");
var progressBar = document.getElementById('progress_bar');
var suffle = document.getElementById('suffle');

function blockRightClick() {
    alert("오른쪽 버튼은 사용할 수 없습니다.");
    return false;
}

function blockSelect() {
    alert("내용을 선택할 수 없습니다.");
    return false;
}

function loadTxt() {
    var xmlhttp = window.XMLHttpRequest ? new XMLHttpRequest() : window.ActiveXObject ? new ActiveXObject("Microsoft.XMLHTTP") : alert("XMLHTTP를 지원하지 않는 브라우저입니다");
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            musicList = eval(xmlhttp.response);
            ready();
        }
    }
    xmlhttp.open("GET", "./data/" + file_name, true);
    xmlhttp.send();
}

function ready() {
    $.each(musicList, function(key, value) {
        playlist.innerHTML += '<li class="list_item" onclick="playSong(' + key + ') "style="background-color:orange; max-width: 40rem;border-style: inset">' + value.artist + ' - ' + value.title + '</li>';
    });
}

song.addEventListener('timeupdate', function() {
    progressBar.value = song.currentTime;
    var s = song.currentTime % 60;
    var m = song.currentTime / 60;
    songPlayTime.textContent = Math.floor(m) + ' 분 ' + Math.floor(s) + ' 초 ';
});

song.addEventListener('loadedmetadata', function() {
    progressBar.max = song.duration;
    var s = song.duration % 60;
    var m = song.duration / 60;
    songDurationTime.textContent = Math.floor(m) + ' 분 ' + Math.floor(s) + ' 초 ';
});

song.addEventListener("ended", function() {
    next();
});

function playSong(n) {
    currentSong = n;
    song.src = './contents/' + musicList[n].fileRes;
    songTitle.textContent = "Title : " + musicList[n].title; // set the title of song
    songTitle.style.fontSize = "3rem";
    songArtist.textContent = "Artist : " + musicList[n].artist;
    songArtist.style.fontSize = "1.5rem";
    playOrPauseSong();
}

function playOrPauseSong() {
    if (song.paused) {
        song.play();
        $("#play img").attr("src", "./img/pause.png");
    } else {
        song.pause();
        $("#play img").attr("src", "./img/play.png");
    }
}

function next() {
    currentSong++;
    if (currentSong > musicList.length - 1) {
        currentSong = 0;
    }
    if (isSuffling) {
        currentSong = Math.floor(Math.random() * musicList.length);
    }
    playSong(currentSong);
}


function pre() {
    currentSong--;
    if (currentSong < 0) {
        currentSong = musicList.length - 1;
    }
    playSong(currentSong);
}

function clickingSuffle() {
    isSuffling = !isSuffling;
    suffle.textContent = isSuffling ? "Suffle On" : "Suffle Off";
}