$(function(){
    if(navigator.userAgent.indexOf('Trident')>0){
        $('.nav-left, div.iframe').css('height',$('.nav-left')[0].clientHeight+25+'px')
        console.log('tag', $('.nav-left')[0].clientHeight);
    }else if(navigator.userAgent.indexOf('Firefox')>0){
        $('.nav-left, div.iframe').css('height',$('.nav-left')[0].clientHeight+18+'px')
    }
    var iframeArr = [
        './357/index.html',
        './52/index.html',
        './53/index.html',
        './54/index.html',
        './55/index.html',
    ];
    var textArr = [
        '改进作风狠抓落实监督信箱',
        '政策咨询',
        '投诉举报',
        '意见反馈',
        '厅长信箱',
    ];
    $('.nav-left li').each(function(index, el){
        console.log(index)
        $(el).bind('click', function(){
            console.log(el)
            var $li = $(this);
            if(!$li.hasClass('active')){
                var $liIndex  = $li.index();
                var $iframe = $('.iframe iframe');
                var $allLi = $li.parent().children('li');
                $allLi.each(function(index, el){
                    var $img = $(el).children('img');
                    if(index == $liIndex){
                        $(el).addClass('active');
                        $iframe.attr('src', iframeArr[index]);
                        $img.attr('src', $img.attr('src').replace(/-out/, '-on'));
                        $('.current-position span').text(textArr[index])
                    }else if($(el).hasClass('active')){
                        $(el).removeClass('active');
                        $img.attr('src', $img.attr('src').replace(/-on/, '-out'));
                    }
                });
            }
        });
    });
});
