$(function(){
    $.hiddenLimit({
        parent: '.content-box',
        length: 18,
        num: 6
    });
    $.getCurrentDate();
    $('.input-box p').click(function(){
        $.searchClick($('.input-box input').val());
    });
    $.goPage();
})