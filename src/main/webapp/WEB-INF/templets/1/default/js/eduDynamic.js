$(function(){
    /**
     * ul切换
     */
    $('.top-left-new-list div a p').changeUl({
        eventType: 'mouseover'
    });
    $('.top-right-new-list div a p').changeUl({
        eventType: 'mouseover'
    });
    $('.bottom-left-new-list div a p').changeUl({
        eventType: 'mouseover'
    });
    $('.bottom-right-new-list div a p').changeUl({
        eventType: 'mouseover'
    });
    $('.pic-new li').lastLi();
    $('.vedio-new li').lastLi();
    $.hiddenMore({
        parent: '.important-talk',
    });
    $.hiddenMore({
        parent: '.important-active',
    });
    $.hiddenMore({
        parent: '.working-state',
    });
    $.hiddenMore({
        parent: '.policy-interpretation',
    });
    $.hiddenMore({
        parent: '.media-focus',
    });
    $('#announcement').click(function() {
        localStorage.setItem('key', 'announcement')
        window.open("http://edu.xizang.gov.cn/6/index.html")
    })
    $('#notice').click(function() {
        localStorage.setItem('key', 'notice')
        window.open("http://edu.xizang.gov.cn/6/index.html")
    })
})