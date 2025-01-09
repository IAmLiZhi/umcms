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
});
$('a.to-notice').click(function(){
	window.localStorage.setItem('key', 'notice');
	$(this).attr('href', '../../6/index.html');
});