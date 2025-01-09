$(function(){
    if(navigator.userAgent.indexOf('Trident')>0){
        $('div.left, div.right').css('height',$('div.left')[0].clientHeight + 25 + 'px');
        $('div.left>ul:first>li:last').css('marginBottom', 43 + 'px');
        // console.log('tag', $('.nav-left')[0].clientHeight);
    }else if(navigator.userAgent.indexOf('Firefox')>0){
        $('div.left, div.right').css('height',$('div.left')[0].clientHeight + 20 + 'px');
        $('div.left>ul:first>li:last').css('marginBottom', 38+ 'px');
    }
    
    $.changeImgSrc('education/jgsz/jsgzc-logo.png');
    
})