$(function(){
    $('.content-box li').lastLi({
        dividend: 4,
        remainder: 3
    });
    // $.hiddenLimit({
    //     length: 12,
    // });
    $('.go').click($.goPage);
})