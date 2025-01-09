<@ms.html5 style="height: 100%">

	<@ms.panel style="width: 996px; margin: 50px auto; background-color: #ffffff; padding: 0; border: 1px solid #CCCCCC; height: auto; min-height: 0;">
		<div style=" color: #333333; font-weight: bold; border-bottom: 2px solid #CCCCCC; background-color: #F0F6FD; padding: 15px 0;">
			<p style="margin: 0 0 0 91px; display: inline-block;">状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;态:<span style="margin-left: 30px; color: #FF3E3E; display: inline-block;">
				<#switch articleEntity.articleState>
					<#case 0>
						编辑中
						<#break/>
					<#case 1>
						审核中
						<#break/>
					<#case 2>
						已通过
						<#break/>
					<#case 3>
						未通过
						<#break/>
					<#case 4>
						待发布
						<#break/>
					<#case 5>
						已发布
						<#break/>
				</#switch>
			</span></p>
			<p style="margin: 0 0 0 90px; display: inline-block;">审核意见: <span style="width: 721px; margin-left: 26px; color: #FF6666; font-weight: normal; display: inline-block; vertical-align: top;">${articleEntity.articleNote?default('')}</span></p>
		</div>
		<#if articleEntity.articleType != 'p' && articleEntity.basicPic != ''>
			<img src="${articleEntity.basicPic?default('')}" style="width: 865px; margin: 26px auto; display: block;"/>
			<hr style="border:0;  margin: 0; width: 100%; height: 2px; background-color: #E9E9E9;"/>
		</#if>
		<#if articleEntity.articleType == 'w' || articleEntity.articleType == 'f'>
			<p style="width: 865px; margin: 50px auto; font-weight: bold; font-size: 26px; color: #333333; text-align: center; line-height: 30px;">${articleEntity.basicTitle?default('')}</p>
			<div style="width: 865px; background-color: #E9E9E9; height: 28px; font-size: 14px; line-height: 28px; color: #666666; margin: 0 auto; padding: 0 216px; overflow: hidden;">
				${articleEntity.basicDateTime?string('yyyy年MM月dd日')}
				<span style="float: right;">来源:${articleEntity.articleSource?default('')}</span>
			</div>
		</#if>
		<#if articleEntity.articleType == 'p'>
			<div style="width: 865px; margin: 43px auto; overflow: hidden;">
				<img src="${articleEntity.basicPic?default('')}" style="width: 108px; float: left; height: 154px; margin-right: 22px;">
				<p style="float: left; width: 735px; height: 100%; font-size: 17px; color: #333333;">
					<span style="font-weight: bold; display: block; margin-top: 22px;">${articleEntity.basicTitle?default('')}</span>
					<span style="display: block; margin-top: 36px;">${articleEntity.basicDescription?default('')}</span>
				</p>
			</div>
			<hr style="width: 100%; height: 2px; background-color: #e9e9e9;"/>
			<p style="width: 865px; margin: 49px auto 26px; text-align: center; font-size: 26px; font-weight: bold; color: #333333;">个人简历</p>
		</#if>
		<#if articleEntity.articleType != 'h'>
			<div class="content" style="width: 865px; margin: 30px auto 0;">${articleEntity.articleContent?default('')}</div>
			<div style="width: 100%; height: 28px; background-color: #e9e9e9; font-size: 14px; line-height: 28px; color: #666666; margin-top: 120px; overflow: hidden;">
				<span class="print" style="cursor: pointer; float: right; margin: 0 86px 0 55px;">【打印页面】</span>
				<span style="float: right;">责任编辑：${articleEntity.articleAuthor?default('')}</span>
			</div>
		</#if>
		<#if articleEntity.articleType == 'h'>
			<div style="width: 865px; margin: 41px auto; color: #333333;">
				<p>内容标题：</p>
				<p style="margin: 13px 0 26px; font-size: 26px; font-weight: bold;">${articleEntity.basicTitle?default('')}</p>
				<p>链接地址：</p>
				<p style="margin-top: 13px; font-size: 17px; font-weight: bold; ">${articleEntity.articleUrl?default('')}</p>
			</div>
			<hr style="width: 100%; height: 2px; background-color: #e9e9e9;" />
			<div style="margin: 26px auto 20px; width: 865px;">
				<img id="img" src="${articleEntity.basicPic}" />
			</div>
		</#if>
	</@ms.panel>
</@ms.html5>
<script>
$(function(){
	$('.print').click(function(){
		window.print();
	});
	if($('img').length != 0){
		$('img').each(function(index, el){
			el.onload = function(){
				if(this.width > 865 ){
					this.width = 865;
				}
			}
		})
	}
	$('div.content').children('table').attr('border', '1').css('width', 'auto').css('color', '#000000');
})

</script>