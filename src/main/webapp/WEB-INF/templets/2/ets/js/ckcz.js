$(function(){
    $.getCurrentDate();
    $('.input-box p').click(function(){
        $.searchClick($('.input-box input').val());
    });
    $.height();
    $.new(['.main>.middle>ul']);
})
    