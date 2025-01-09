$(function(){
    $('.siteul li').lastLi();
    $.hiddenLimit({
        length: 9,
        pageSize: 9
    });
    $('.go').click($.goPage);
})