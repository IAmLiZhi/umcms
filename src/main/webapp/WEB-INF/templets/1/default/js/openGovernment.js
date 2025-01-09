$(function(){
    if(navigator.userAgent.indexOf('Trident')>0){
        $('.nav-left, div.iframe').css('height',$('.nav-left')[0].clientHeight+25+'px')
    }else if(navigator.userAgent.indexOf('Firefox')>0){
        $('.nav-left, div.iframe').css('height',$('.nav-left')[0].clientHeight+18+'px')
    }
    var iframeArr = [
        './25/index.html',
        './348/index.html',
        './27/index.html',
        './28/index.html',
        './29/index.html',
        './30/index.html',
        './31/index.html',
        './32/index.html',
        './33/index.html',
        './34/index.html',
        './35/index.html',
        './36/index.html',
    ];
    var $iframe = $('.iframe iframe');

    // var param = window.location.search;
    
    var param = window.localStorage.getItem('key');
    window.localStorage.removeItem('key');
    // param = param.substring(1, param.length);
    if(param){
        var $index = 0;
        var $text = '';
        $('.nav-left li').each(function(index, el){
            var $img = $(el).children('img');
            if($(el).hasClass(param)){
                $(el).addClass('active');
                $img.attr('src', $img.attr('src').replace(/-out/, '-on'));
                $index = index;
                $text = $(el).text();
            }else{
                $(el).removeClass('active');
                $img.attr('src', $img.attr('src').replace(/-on/, '-out'));
            }
        });
        $iframe.attr('src', iframeArr[$index]);
        $('.current-position span').text($text);
    }else{
        $iframe.attr('src', iframeArr[0]);
        $('.current-position span').text('信息公开相关');
    }
    $('.nav-left li').each(function(index, el){
        $(el).bind('click', function(){
            var $li = $(this);
            if(!$li.hasClass('active')){
                var $liIndex = $li.index();
                var $allLi = $li.parent().children('li');
                $allLi.each(function(index, el){
                    var $img = $(el).children('img');
                    if(index == $liIndex){
                        $iframe.attr('src', iframeArr[index]);
                        $(el).addClass('active');
                        $img.attr('src', $img.attr('src').replace(/-out/, '-on'));
                        $('.current-position span').text($(el).text());
                    }else if($(el).hasClass('active')){
                        $(el).removeClass('active');
                        $img.attr('src', $img.attr('src').replace(/-on/, '-out'));
                    }
                })
            }
        })
    })
});