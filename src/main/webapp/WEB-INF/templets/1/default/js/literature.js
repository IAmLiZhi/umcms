$(function(){
    if(navigator.userAgent.indexOf('Trident')>0){
        $('div.left, div.right').css('height',$('div.left')[0].clientHeight + 25 + 'px');
        $('div.left>ul:first>li:last').css('marginBottom', 43 + 'px');
        // console.log('tag', $('.nav-left')[0].clientHeight);
    }else if(navigator.userAgent.indexOf('Firefox')>0){
        $('div.left, div.right').css('height',$('div.left')[0].clientHeight + 20 + 'px');
        $('div.left>ul:first>li:last').css('marginBottom', 38+ 'px');
    }
    $('.right .ul-nav a p').each(function(index, el){
        $(el).bind('click', function(){
            var $p = $(this);
            var $a = $p.parent();
            var $aIndex = $a.index();
            var $div = $a.parent();
            var $allA = $div.children('a');
            var $iframe = $('.iframe iframe');
            switch($aIndex){
                case 0:
                    $iframe.attr('src', './45/index.html');
                break;
                case 1:
                    $iframe.attr('src', './44/index.html');
                break;
                case 2:
                    $iframe.attr('src', './43/index.html');
                break;
            }
            for(var i = 0; i < $allA.length; i++){
                if(i == $aIndex){
                    $($allA[i]).children('p').addClass('active');
                }else{
                    $($allA[i]).children('p').removeClass('active');
                }
            }
        });
    });
})