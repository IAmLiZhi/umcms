$(function(){
    $('.print').print();

    if($('img').length != 0){
		$('img').each(function(index, el){
			el.onload = function(){
				if(this.width > 865 ){
					this.width = 865;
				}
			}
		});
	}
    $.getCurrentDate();
    $('.input-box p').click(function(){
        $.searchClick($('.input-box input').val());
    });
    var $allLi =  $('.main ul li');
    $allLi.click(function(){
        var self = $(this);
        var $index = self.index();
        $allLi.each(function(index, el){
            if(index == $index){
                if(!$(el).hasClass('active')){
                    $(el).addClass('active');
                    var $img = $(el).children('img');
                    $img.attr('src', $img.attr('src').replace(/-out/, '-on'));

                }
            }else{
                if($(el).hasClass('active')){
                    $(el).removeClass('active');
                    var $img = $(el).children('img');
                    $img.attr('src', $img.attr('src').replace(/-on/, '-out'));
                }
            }
        });
        $('.main .content-box>div').each(function(index, el){
            if(index == $index){
                if($(el).hasClass('none'))
                    $(el).removeClass('none');
            }else{
                if(!$(el).hasClass('none')) $(el).addClass('none');
            }
        });
    })
})
    