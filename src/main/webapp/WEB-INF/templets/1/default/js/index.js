$(function(){
    /**
     * 设置当前日期
     */
    $('.welcome span').text($.getCurrentDateFormat());
    $('.news-list-top-left div a p').changeUl({
        eventType: 'mouseover'
    });
    $('.news-list-top-right div a p').changeUl({
        eventType: 'mouseover'
    });
    $('.bottomnav div a p').changeUl({
        eventType: 'mouseover'
    });
    $('.vedio-view li').lastLi();
    $('#banner3').myscroll({
        picEl: $('#move2'), //图片父级，不传默认为banner内第1个div
        ctrlEl: $('#ctrl2'), //控制条父级，包括小圆点和左右箭头，不传默认为banner内第2个div
        libs: true, //是否创建底部小圆点，true || false,不传默认true
        arrows: true, //是否创建左右箭头，true || false,不传默认true
        autoPlay: true, //是否自动播放，true || false,不传默认true
        time: 6000, //自动播放间隔时间，true || false,不传默认2000
        speed: 400, //图片切换速度，不传默认400
        effect: 'fade' //轮播的改变方式 top||left||fade，不传默认left
    });
    $.hiddenMore({
        parent: '.important-talk'
    });
    $.hiddenMore({
        parent: '.important-active'
    });
    $.hiddenMore({
        parent: '.important-notice'
    });
    $.hiddenMore({
        parent: '.important-file'
    });
    $('.pic-new li').lastLi();
    $('a.to-notice').click(function(){
        window.localStorage.setItem('key', 'notice');
        $(this).attr('href', '../../6/index.html');
    });
    $('a.to-announcement').click(function(){
        window.localStorage.setItem('key', 'announcement');
        $(this).attr('href', '../../6/index.html');
    });
    $.new(['#mainBox>.new-list>div ul']);
})
