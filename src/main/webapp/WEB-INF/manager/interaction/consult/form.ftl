<@ms.html5>
	<@ms.nav title="政策咨询详情" back=true></@ms.nav>
	<@ms.panel>
		<@ms.form name="columnForm" isvalidation=true  action="" method="post" >
			<@ms.text name="name" readonly="readonly" colSm="2" width="200" label="姓名" title="姓名" size="5"  placeholder=""  value="${interaction.name?default('')}"/>
			<@ms.text name="name" readonly="readonly" colSm="2" width="200" label="联系手机" title="联系手机" size="5"  placeholder=""  value="${interaction.phone?default('')}"/>
			<@ms.text name="name" readonly="readonly" colSm="2" width="200" label="证件号码" title="证件号码" size="5"  placeholder=""  value="${interaction.cardNo?default('')}"/>
			<@ms.text name="name" readonly="readonly" colSm="2" width="200" label="咨询标题" title="证件号码" size="5"  placeholder=""  value="${interaction.title?default('')}"/>
			<@ms.textarea colSm="2" readonly="readonly" name="basicDescription" label="咨询内容" wrap="Soft" rows="4"  value="${interaction.publishContent?default('')}" />
			<@ms.text name="name" readonly="readonly" colSm="2" width="auto" label="附件" style="background-color: #ffffff;"  title="${interaction.fileUrl?default('无')}" size="5"  placeholder=""  value="下载附件"/>
			<@ms.textarea colSm="2" name="publishContent" label="回复内容" wrap="Soft" rows="4" placeholder="请输入回复内容" value="${interaction.replyContent?default('')}" />
			<@ms.radio name="isopen" 
			    list=[{"id":"0","value":"公开"},{"id":"1","value":"不公开"}] value="${interaction.isOpen?default(0)}" 
			    listKey="id" listValue="value" label="是否公开"
			/>	
			<#if interaction.state == 1>
		 		<button id="update" style="height: 34px; border: 0;line-height: 1;color: #fff;;margin-left: 198px;width: 70px;background-color: #5cb85c;border-color:#4cae4c;padding:6px 12px;border-radius: 4px;">修改</button>
		 	<#else>
		 		<button id="save" style="height: 34px; border: 0;line-height: 1;color: #fff;margin-left: 198px;width: 70px;background-color: #5cb85c;border-color:#4cae4c;padding:6px 12px;border-radius: 4px;">保存</button>
		 	</#if>
			<button id="print" style="height: 34px;line-height: 1;border-width: 0.5px;margin-left:5px;width:70px;background-color: #ffffff;padding: 6px 12px;border-radius: 4px;">打印</button>
			<input id ='interactionId' type = "text" style="display: none" value="${interaction.id}">
		</@ms.form>
	</@ms.panel>
	
	
</@ms.html5>	
<script>
	
$(function(){
	var param = window.location.search;
	if(param.length != 0){
		$('textarea[name="publishContent"]').attr('readonly', true);
		$('#ipdate,#save').css('display','none');
	}
	var download = $('input[value="下载附件"]');
	download.css('border-color', '#1862d0').css('color', '#1862d0').css('display', 'inline-block').css('cursor', 'pointer').css('width', '82px').css('text-align', 'center');
	if(!download.attr('title')) {
		download.val('无').css('border', '0').css('box-shadow', 'none');
	}
	else{
		var spantext = download.attr('title').substring(download.attr('title').lastIndexOf('/') + 1, download.attr('title').length);
		var span = $('<span></span>').css('display', 'inline-block').css('max-width', '500px').text(spantext).css('margin-left', '10px');
		download.parent().append(span);
		download.mouseover(function(){
			$(this).css('background-color', '#1862d0').css('color', '#ffffff');
		}).mouseout(function(){
			$(this).css('background-color', '#ffffff').css('color', '#1862d0');

		});
	}
	var state = 0;
	$('input[name="isopen"]').change(function(){
		state = $(this).val();
	});
	
	//栏目保存提交事件
	$("#save").click(function(){
		var publishContent = document.getElementsByName("publishContent")[0].value;
		var id = $('#interactionId').val();
		if(publishContent==""|| publishContent==null){
			<@ms.notify msg="回复内容不能为空" type="warning"/>
		}else{
			$.ajax({
				type: "post",
				url: "${managerPath}/hd/reply.do",
				data: JSON.stringify({'publishId':id,'replyContent':publishContent, isOpen: state }),
				dataType:"json",
				contentType: "application/json",
				success: function(msg){
					if (msg = true) {
						var ss = "${managerPath}/hd/zczx/index.do";
						location.href= ss;
					}else{
						alert("回复失败！");
					}
				}
			});
		}
	});
	$("#update").click(function(){
		var publishContent = document.getElementsByName("publishContent")[0].value;
		var id = $('#interactionId').val();
		if(publishContent==""|| publishContent==null){
			<@ms.notify msg="回复内容不能为空" type="warning"/>
		}else{
			$.ajax({
				type: "post",
				url: "${managerPath}/hd/update.do",
				data: JSON.stringify({'publishId':parseInt(id),'replyContent':publishContent, isOpen: state }),
				dataType:"json",
				contentType: "application/json",
				success: function(msg){
					if (msg = true) {
						var ss = "${managerPath}/hd/zczx/index.do";
						location.href= ss;
					}else{
						alert("回复失败！");
					}
				}
			});
		}
	});
	
	//打印
	$("#print").click(function(){
		window.print();
	});
	var first = 0;
	var next = 0;
	download.click(function(){
		if(first == 0){
			first = +new Date();
		}else{
			next = +new Date();
			if(next - first <= 3000){
				first = next;
				return;
			}
			first = next;
		}
		var self = $(this);
		if(self.val().length != 1){
			$.download('${managerPath}/file/gendownload', 'post', self.attr('title'));
		}
	});
	jQuery.download = function (url, method, filedir) {
		jQuery('<form action="' + url + '" method="' + (method || 'post') + '">' +  // action请求路径及推送方法
					'<input type="text" name="fileUrl" value="' + filedir + '"/>' + // 文件路径
				'</form>')
		.appendTo('body').submit().remove();
	};

});

</script>
