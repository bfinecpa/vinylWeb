window.onload = function (){
  if(is_mobile() === true){
    var logo = document.querySelector('#logo');
    logo.style.width = "150px";
    logo.style.height = "150px";

    document.querySelector('header').style.paddingTop = "20px";
    document.querySelector('header').style.paddingBottom = "20px";
    document.querySelector('#user').style.display = "none";

    var searchBox = document.querySelector('.searchBox');
    var search = document.querySelector('#search');
    var searchImg = document.querySelector('.searchBox img');
    var buttons = document.querySelector('.buttons');
    
    searchBox.style.width = "250px";
    searchBox.style.height ="45px";
    search.style.padding = "15px";
    search.style.height ="45px";

    searchImg.style.width = "20px";
    searchImg.style.height = "20px";
    searchImg.style.top = "15px";
    searchImg.style.right = "45px";

    buttons.style.width = "250px";
    buttons.style.height = "35px";
    buttons.style.margin = "0px";

    var txt = document.getElementsByClassName('txt');
    for (var i = 0; i < txt.length; i++){
      txt[i].style.fontSize = "14px";
      txt[i].style.lineHeight = "14px";
    }

    var used = document.getElementById('usedVinyl');
    var record = document.getElementById('recordVinyl');
    used.innerText = '중고 거래';
    record.innerText = '바이닐 기록';
    used.style.padding = "10px";
    record.style.padding = "10px";
    
  }
}

function is_mobile(){
  if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
    return true;
  }
  if (typeof window.orientation !== 'undefined') {
    return true;
  }  
  var iOSios = !!navigator.platform && /iPad|iPhone|iPod/.test(navigator.platform);
  if(iOSios) return true;
  return false;
}