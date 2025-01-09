$(function(){
    $('.print').print();
	$.hit(Number($('#id').val()));
    if($('.content img').length != 0){
		$('.content img').each(function(index, el){
			el.onload = function(){
				if(this.width > 825 ){
					$(this).css('width', '825px');
				}
			}
		})
	}
	$.getCurrentDate();
    $('.input-box p').click(function(){
        $.searchClick($('.input-box input').val());
    });
})