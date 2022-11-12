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

  
    var vinylInfo = document.querySelector('.vinylInfo');
    vinylInfo.style.padding = "0px 20px";
    vinylInfo.style.marginTop = "20px";
    vinylInfo.style.justifyContent = "space-between";
    var upPicBox = document.querySelector('.upPicBox');
    upPicBox.style.marginRight = "10px";

    var fontSub = document.getElementsByClassName('fontSub');
    for (var i = 0; i < fontSub.length; i++){
      fontSub[i].style.fontSize = "16px";
      fontSub[i].style.lineHeight = "16px";
    }
    var detailLabel = document.getElementsByClassName('detailLabel');
    for (var i = 0; i < detailLabel.length; i++){
      detailLabel[i].style.marginRight = "10px";
    }
    var detailInput = document.querySelectorAll('.detailInfo li input');
    for (var i = 0; i < detailInput.length; i++){
      detailInput[i].style.width = "100px";
      detailInput[i].style.height = "20px";
    }
    var grade = document.querySelector('#grade');
    grade.style.width = "100px";
    grade.style.height = "20px";
    var option = document.querySelectorAll('option');
    for (var i = 0; i < option.length; i++){
      option[i].style.fontSize = "8px";
      option[i].style.lineHeight = "8px";
    }

    var vinylNote = document.querySelector('.vinylNote');
    vinylNote.style.padding = "20px";

  }
}
function loadImage(input){

  if(!input.files[0]) return;
  else{
    var upPicBox = document.querySelector('.upPicBox');
    var showPic = document.querySelector('.showPicBox');
    upPicBox.style.display = "none";

    var newImage = document.createElement("img");
    newImage.setAttribute("id", 'newPic');

    var file = input.files[0];
    //original_image_file = file;
    newImage.src = URL.createObjectURL(file);
    showPic.appendChild(newImage);

    //원본 이미지의 정보 저장
    // var fileReader = new FileReader();
    // fileReader.onload = function(e){
    //   originalImage = new Image();
    //   originalImage.src = e.target.result;
    // }
    // fileReader.readAsDataURL(file);

    showPic.style.display = "block";

    newImage.style.width = "100%";
    newImage.style.height = "100%";
    newImage.style.objectFit = "contain";
    newImage.style.borderRadius = "10px";

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