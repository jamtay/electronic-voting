var myVar=setInterval(function(){myTimer()},1);
var count = 0;
function myTimer() {
if(count < 100){
  $('.progress').css('width', count + "%");
  count += 0.05;
   document.getElementById("demo").innerHTML = Math.round(count) +"%";
   // code to do when loading
  }

  else if(count > 99){
      // code to do after loading
  count = 0;


  }
}