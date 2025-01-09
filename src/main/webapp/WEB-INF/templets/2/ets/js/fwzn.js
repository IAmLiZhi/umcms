$(function(){
    $.height();
    $.getCurrentDate();
    $('.input-box p').click(function(){
        $.searchClick($('.input-box input').val());
    });
})
    