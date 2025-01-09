$(function(){
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
                    $iframe.attr('src', './47/index.html');
                break;
                case 1:
                    $iframe.attr('src', './290/index.html');
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