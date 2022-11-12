window.onload = function (){
  if(is_mobile() === true){
    var logo = document.querySelector('#logo');
    logo.style.width = "100px";

    document.querySelector('header').style.padding = "0px";
    document.querySelector('header').style.height = "75px";
    document.querySelector('#user').style.display = "none";
    document.querySelector('header ul').style.margin = "0px";

    var used = document.getElementById('usedVinyl');
    var record = document.getElementById('recordVinyl');
    used.innerText = '중고거래';
    record.innerText = '가계부';
    used.style.margin = "5px";
    record.style.margin = "5px";

    var txt = document.getElementsByClassName('txt');
    for (var i = 0; i < txt.length; i++){
      txt[i].style.fontSize = "20px";
      txt[i].style.lineHeight = "20px";
    }

    var searchBox = document.querySelector('.searchBox');
    searchBox.style.display = "none";
    
    var name = document.querySelector('.name');
    name.style.padding = "20px";
    name.style.paddingBottom = "0px";
    name.style.height = "50px"
    var mainName = document.querySelector('.mainName');
    mainName.style.fontSize = "18px";
    mainName.style.lineHeight = "18px";
    mainName.style.marginRight = "10px";
    var detailName = document.querySelector('.detailName');
    detailName.style.fontSize = "14px";
    detailName.style.lineHeight = "14px";

    var postSearch = document.querySelector('.postSearch');
    postSearch.style.width = "130px";
    
    var postList = document.querySelector('.postList');
    postList.style.padding = "0px";

    var postImg = document.querySelectorAll('.post img');
    var title = document.getElementsByClassName('title');
    for (var i = 0; i < postImg.length; i++){
      // postImg[i].style.width = "125px";
      // postImg[i].style.height = "125px";
      title[i].style.fontSize = "16px";
      title[i].style.lineHeight = "16px";
      title[i].style.marginTop = "10px";
    }
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