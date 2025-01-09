$(function(){
    console.log('tag', navigator.userAgent)
    //gg  Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36
    //ff  Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0
    //ie  Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729; LCTE; rv:11.0) like Gecko
    if(navigator.userAgent.indexOf('Trident')>0){
        $('.nav-left, div.iframe').css('height',$('.nav-left')[0].clientHeight+25+'px')
    }else if(navigator.userAgent.indexOf('Firefox')>0){
        $('.nav-left, div.iframe').css('height',$('.nav-left')[0].clientHeight+18+'px')
    }
    /**
     * nav切换
     */
    var iframeArr = [
        './308/index.html',
        './16/index.html',
        './15/index.html',
        './14/index.html',
        './13/index.html',
        './376/index.html'
    ]
    var $iframe = $('.iframe iframe');
    $('.nav-left li').each(function(index, el){
        $(el).bind('click', function(){
            var $li = $(this);
            if(!$li.hasClass('active')){
                var $liIndex = $li.index();
                var $allLi = $li.parent().children('li');
                $iframe.attr('src', iframeArr[$liIndex]);
                if($liIndex != 1) {
                    $('.iframe').css('height', '788px')
                    $('.nav-left').css('height', '788px')
                }
                $allLi.each(function(index, el){
                    var $img = $(el).children('img');
                    if(index == $liIndex){
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
    });
});
    
