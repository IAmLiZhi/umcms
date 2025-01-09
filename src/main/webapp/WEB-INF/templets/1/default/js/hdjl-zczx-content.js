$(function(){
    $('.font-size span').click(function(){
		var val = $(this).text();
		$.changeFontSize('.content', val);
	});
	$('.print').print();
$.hit(Number($('#id').val()));
	// var param = window.location.search;
	var param = window.localStorage.getItem('key');
	window.localStorage.removeItem('key');
	if(param.length != 0){
		// param = param.substring(1, param.length);
		$.ajax({
			url: '/app/interaction/show.do',
			type: 'post',
			data: JSON.stringify({'id': param}),
			contentType: "application/json;charset=UTF-8",
			dataType: 'json',
			success: function(data){
				var span = $('.current-position').children('span');
				switch(data.type){
					case 1:
					span.text('政策咨询');
					break;
					case 2:
					span.text('投诉举报');
					break;
					case 3:
					span.text('意见反馈');
					break;
					case 4:
					span.text('书记信箱');
					break;
				}
				$('.content-box')
				.children('h2').children('span').text(data.title)
				.parent().next().children('p').children('span').text(data.createTime)
				.parents('div.date-setting').next().children('p').text(data.publishContent)
				.parent().next().children('p').children('span').text(data.replyTime)
				.parents('div.date-setting').next().children('p').text(data.replyContent);
			},
			error: function(e){
				console.log('e', e)
			}
		});
	}
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