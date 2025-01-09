
$(document).ready(function(e) {		
	window.console.log($('.fixed').offset())	
	t = $('.fixed').offset().top;
	mh = $('.main-scroll').height();
	fh = $('.fixed').height();
	$(window).scroll(function(e){
		s = $(document).scrollTop();	
		if(s > t - 10){
			$('.fixed').css('position','fixed');
			if(s + fh > mh){
				$('.fixed').css('top',mh-s-fh+'px');	
			}				
		}else{
			$('.fixed').css('position','');
		}
	})
});
