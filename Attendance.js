/**
 * JavaScriptファイル
 */

 function ErrorMsg(Error){
	 let cssClass = document.getElementById('error-box');
	 alert('test')
	 cssClass.addEventListener('load', alert(Error));
}

function ShowLength(str){
	document.getElementById("inputlength").innerHTML = "入力文字数：" + str.length + "文字/30";
}

function completion(){
	let comp = document.getElementById('message');
	comp.addEventListener('load' ,function(){alert('講座申し込みの受付を完了しました。ありがとうございました。');})
}

const limitTextLength = () => {
  let maxLength = 50; // 文字数の上限
  let enteredCharacters = document.getElementById('entered-characters');
  let remainingCharacters = document.getElementById('remaining-characters');

  if (enteredCharacters.value.length > maxLength) {
    enteredCharacters.value = enteredCharacters.value.substr(0, maxLength);
    remainingCharacters.classList.add('max');
  } else {
    remainingCharacters.classList.remove('max');
  }
  
   remainingCharacters.textContent = maxLength - enteredCharacters.value.length;
};