$(function(){
    $('.font-size span').click(function(){
		var val = $(this).text();
		$.changeFontSize('.content', val);
	});
	$.borderStyle();
    $('.print').print();
	$.hit(Number($('#id').val()));
    if ($('.content img').length != 0) {
		$('.content img').each(function (index, el) {
			el.onload = function () {
				if (this.width > 825) {
					$(this).css('width', '825px');
				}
			}
			if($(el).attr('src') == 'http://zq.xzedu.gov.cn/static/plugins/ueditor/1.4.3.1/dialogs/attachment/fileTypeImages/icon_doc.gif'){
				$(el).css('margin-top', '0');
			}
		})
	}
	$.changeImgSrc('education/jgsz/sxzzgzc-logo.png');
	$('#a').click(function(){
		window.location.href = '../index.html';
		window.name = 'zcwj';
	});
})
