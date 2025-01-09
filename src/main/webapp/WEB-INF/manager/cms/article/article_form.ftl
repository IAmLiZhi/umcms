<@ms.html5>
	<@ms.nav title="文章管理" back=true>
		<#if article.basicId == 0>
	 		<@ms.saveButton id="saveUpdate" value="保存"/>
	 	<#else>
	 		<@ms.updateButton id="saveUpdate" value="更新"/>
	 	</#if>
	</@ms.nav>
	<@ms.panel>
		<#if articleType?has_content>
			<@ms.radio colSm="2" labelStyle="text-align: right; padding-right: 23px; margin-top: 8px;" style="" name="radio" label="内容属性" list=articleType listKey="dictValue"  listValue="dictLabel"  value=""/>
		</#if>
		<@ms.form isvalidation=true name="articleForm" style="display: none;" action="${managerPath}/cms/article/${autoCURD}.do">
			<#if !isEditCategory><!-- 如果不是单篇 -->
	            <@ms.formRow colSm="2" label="所属栏目" width="300">
	            <#if categoryTitle=="">
	            	<@ms.treeInput treeId="inputTree" json="${listColumn?default('')}" jsonId="categoryId" jsonPid="categoryCategoryId" jsonName="categoryTitle" inputName="basicCategoryId" inputValue="${categoryId}" buttonText="选择栏目" clickZtreeId="clickZtreeId(event,treeId,treeNode);" expandAll="true"  showIcon="true"/>
	            <#else>
	            	<@ms.treeInput treeId="inputTree" json="${listColumn?default('')}" jsonId="categoryId" jsonPid="categoryCategoryId" jsonName="categoryTitle" inputName="basicCategoryId" inputValue="${categoryId}" buttonText="${(categoryTitle)!}" clickZtreeId="clickZtreeId(event,treeId,treeNode);" expandAll="true"  showIcon="true"/>
				</#if>
				</@ms.formRow>
            </#if>
			<@ms.hidden id="articleType" name="articleType" value="${article.articleType?default('')}"/>
			<@ms.text name="basicTitle" colSm="2" width="400" label="文章标题"	title="文章标题" size="5"  placeholder="请输入文章标题"  value="${article.basicTitle?default('')}"  validation={"maxlength":"300","required":"true", "data-bv-notempty-message":"文章标题不能为空","data-bv-stringlength-message":"标题在300个字符以内!", "data-bv-notempty-message":"必填项目"}/>
			<@ms.text name="basicSort"  colSm="2" width="200" label="自定义顺序" title="自定义顺序" size="5"  placeholder="请输入文章顺序" value="${article.basicSort?c?default(0)}" validation={"data-bv-between":"true","required":"true", "data-bv-between-message":"自定义顺序必须大于0","data-bv-between-min":"0", "data-bv-between-max":"99999999","data-bv-notempty-message":"自定义顺序不能为空"}/>
			<#--  <@ms.formRow colSm="2" label="文章缩略图" width="400" >
				<@ms.uploadImg path="article" uploadFloderPath="${articleImagesUrl?default('')}" inputName="basicThumbnails" size="1" msg="提示:文章缩略图，支持jpg格式"  imgs="${article.basicThumbnails?default('')}"  />
			</@ms.formRow>  -->
			<div class="form-group ms-form-group has-feedback">
				<label class="col-sm-2 control-label" for="inputFile">文章缩略图</label>
				<div style="overflow: hidden;" class="col-sm-9 ms-from-group-input ms-form-input">
					<input id="fileUpload" type="file" accept=".jpg,.png"/>
					<img style="display: block; width: 200px;"/>
				</div>
			</div>
			<@ms.hidden id="articlePic" name="articlePic" value="${article.basicPic?default('')}"/>
			<@ms.text name="articleSource" colSm="2" width="200" label="文章来源" title="文章来源" size="5"  placeholder="请输入文章来源"  value="${article.articleSource?default('')}" validation={"maxlength":"300", "data-bv-stringlength-message":"文章来源在300个字符以内!"} />
			<@ms.text name="articleAuthor" colSm="2" width="200" label="文章作者" title="文章作者" size="5"  placeholder="请输入文章作者"  value="${article.articleAuthor?default('')}" validation={"maxlength":"12", "data-bv-stringlength-message":"文章作者在12个字符以内!"} />
			<#-- <@ms.text name="articleSend" colSm="2" width="200" label="发布人员" title="发布人员" size="5"  placeholder="请输入发布人员"  value="${article.articleAuthor?default('')}" validation={"maxlength":"12", "data-bv-stringlength-message":"发布人员在12个字符以内!"} />
             --><@ms.hidden id="basicDateTime" name="basicDateTime" value=""/>
            <@ms.hidden id="articleState" name="articleState" value="${article.articleState?default(0)}"/>
            <#--  <@ms.date id="articleDateTime" name="articleDateTime" time=true label="发布时间" single=true readonly="readonly" width="300" value="${(article.basicDateTime?default(.now))?string('yyyy-MM-dd HH:mm')}" validation={"required":"true", "data-bv-notempty-message":"必填项目"} placeholder="点击该框选择时间段"  />  -->
			<#--  <div class="form-group ms-form-group has-feedback">
				<label class="col-sm-2 control-label" for="inputFile">发布时间</label>
				<div style="overflow: hidden; position: relative;" class="col-sm-9 ms-from-group-input ms-form-input">
					<input type="text"  id="articleDateTime" value=""/>
					<input placeholder="请输入日期" readonly id="articleDateTime" class="laydate-icon timeUstyle stateUTime" onclick="laydate({istime: true,format:'YYYY-MM-DD hh:mm'})">
				</div>
			</div>  -->
			<@ms.text name="articleDateTime" colSm="2" id="articleDateTime" width="200" label="发布时间" class="laydate-icon timeUstyle stateUTime" title="发布时间" size="5"  placeholder="请输入日期"  value="${(article.basicDateTime?default(.now))?string('yyyy-MM-dd HH:mm')}" />
			<@ms.textarea colSm="2" name="basicDescription" label="描述" wrap="Soft" rows="4"  size=""  value="${article.basicDescription?default('')}" placeholder="请输入对该文章的简短描述，以便用户查看文件简略" validation={"maxlength":"400","data-bv-stringlength-message":"文章描述在400个字符以内!"}/>
			<@ms.textarea colSm="2" name="articleKeyword" label="关键字" wrap="Soft" rows="4"  size="" placeholder="请输入文章关键字"   value="${article.articleKeyword?default('')}" validation={"maxlength":"155", "data-bv-stringlength-message":"文章关键字在155个字符以内!"}/>
			<@ms.textarea colSm="2" name="articleUrl" label="链接地址" wrap="Soft" rows="4"  size="" placeholder="请输入链接地址"   value="${article.articleKeyword?default('')}" validation={"maxlength":"155","required":"true", "data-bv-notempty-message":"链接地址不能为空","data-bv-stringlength-message":"链接在300个字符以内!", "data-bv-notempty-message":"必填项目"}/>
			<!--新填字段内容开始-->
			<div id="addFieldForm">		
			</div>
			<@ms.editor colSm="2" name="articleContent" label="文章内容" content="${article.articleContent?default('')}"  appId="${appId?default(0)}" uploadServerUrl="${uploadServerUrl?default('')}"/>		
			<@ms.hidden name="modelId"  value="${Session.model_id_session?default('0')}" />
		</@ms.form>
	</@ms.panel>
</@ms.html5>
<script type="" src="../../../../static/skin/manager/4.6.4/js/laydate.js"></script>
<script>
//重写时间控件
<#--  $('#articleDateTime').daterangepicker({
	format:'YYYY-MM-DD HH:mm',
	singleDatePicker: true,
	showDropdowns: true,
	timePickerIncrement: 1,
	timePicker: true,
	timePicker12Hour: true,
	startDate: moment().hours(0).minutes(0).seconds(0),
	showDropdowns: true,
	showWeekNumbers: true,
});
$('#articleDateTime').on('apply.daterangepicker', function(ev, picker) {
	$('#articleDateTime').parents("form:first").data('bootstrapValidator').revalidateField('articleDateTime');
});  -->
$('#articleDateTime').click(function(){
	laydate({istime: true,format:'YYYY-MM-DD hh:mm'});
	var mylaydate = $('#laydate_box');
	mylaydate.remove();
	mylaydate.css('left', '15px').css('top', '34px');
	$('#articleDateTime').parent().append(mylaydate);
})
var articleBasicId=0;
$(function(){
	
	$('#fileUpload').next().attr('src', '${article.articleImagesUrl?default('')}');
	var articleAttr = 'w';
	var param = window.location.search;
	var $title = $('label[for="basicTitle"]').parent(); // 标题
	var $pic = $('#fileUpload').parent().parent(); // 缩略图
	var $from = $('label[for="articleSource"]').parent(); // 来源
	var $author = $('label[for="articleAuthor"]').parent(); // 作者
	var $desc = $('label[for="basicDescription"]').parent(); // 描述
	var $keyWord = $('label[for="articleKeyword"]').parent(); // 关键字
	var $content = $('label[for="articleContent"]').parent(); // 内容
	var $articleUrl = $('label[for="articleUrl"]').parent(); // 链接地址
	var $articleSend = $('label[for="articleSend"]').parent(); // 发布人
	$('.ms-radio input[name="radio"]:first').attr('checked', true);
	$desc.css('display', 'none');
	$keyWord.css('display', 'none');
	$articleUrl.css('display', 'none').children('div').css('display','');
	if(param.length == 2){
		param = param.substring(1);
		$('.ms-radio input[name="radio"]').each(function(index, el){
			if(param == $(el).val()){
				$(el).attr('checked', true);
				var $index = $(el).parent().index();
				attr($index);
			} 
			else $(el).removeAttr('checked');
		});
	}else{
		if($('#articleType').val() != ''){
			$('.ms-radio input[name="radio"]').each(function(index, el){
				if($('#articleType').val() == $(el).val()){
					$(el).attr('checked', true);
					var $index = $(el).parent().index();
					attr($index);
				} 
				else $(el).removeAttr('checked');
			});
		}
	}
	if($('#articlePic').val() != ''){
		$('#fileUpload').next().attr('src', $('#articlePic').val());
	}
	
	var fileObj = '';
	/*
		上传图片
	*/
	$('#fileUpload').change(function(){
		var self = $(this);
		if(self.val() == '') {
			fileObj = '';
			self.next().attr('src','');
			return;
		};
        var fileName = self[0].files[0].name;
        var suffix = fileName.substring(fileName.lastIndexOf('.'), fileName.length);
        var suffixRegExp = /^.(jpg|png)$/
        if(!suffixRegExp.test(suffix.toLowerCase())){
            <@ms.notify msg="仅支持jpg/png类型图片" type="warning"/>
            return ;
        }
        var fileSize = self[0].files[0].size;
        if(fileSize > (5 * 1024 * 1024)){
            <@ms.notify msg="仅支持5M以下的图片" type="warning"/>
            return ;
        }
        $('span.file-prompt').addClass('none');
        fileObj = self[0].files[0];
		self.next().attr('src', getObjectURL(fileObj));
	});
	function getObjectURL (file) {
		var url = null ;
		if (window.createObjectURL!=undefined) { // basic
			url = window.createObjectURL(file) ;
		}else if (window.webkitURL!=undefined) { // webkit or chrome
			url = window.webkitURL.createObjectURL(file) ;
		}else if (window.URL!=undefined) { // mozilla(firefox)
			url = window.URL.createObjectURL(file) ;
		}
		return url ;
    }	
	$('.ms-radio input[name="radio"]').change(function(){
		var $index = $(this).parent().index();
		attr($index);
	});
	var rexgep = /^(http|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?$/;

	var bool = true;
	$articleUrl.children('div').children('textarea').change(function(){
		var self = $(this);
		var val = self.val();
		if(!rexgep.test(val)){
			bool = false;
			<@ms.notify msg="请输入正确的链接" type="warning"/>
		}else{
			bool = true;
		}
	});
	function attr($index){
		$('.ms-radio input[name="radio"]').each(function(index, el){
			if(index == $index) $(el).attr('checked', true);
			else $(el).removeAttr('checked');
		});
		switch($index){
			case 0:
				articleAttr = 'w';
				$title.children('label').text('文章标题')
				.next().children('input').attr('title', '文章标题').attr('placeholder', '请输入文章标题');
				$from.css('display','').children('label').text('文章来源')
				.next().children('input').attr('title', '文章来源').attr('placeholder', '请输入文章来源');
				$author.css('display', '').children('label').text('文章作者')
				.next().children('input').attr('title', '文章作者').attr('placeholder', '请输入文章作者');
				$desc.css('display', 'none');
				$pic.css('display', '').children('label').text('封面图')
				.next().find('div.alert-warning').text('提示:展示图片，支持jpg格式');
				$keyWord.css('display', 'none');
				$articleUrl.css('display', 'none');
				$content.css('display', '').children('label').text('文章内容');
			break;
			case 1:
				articleAttr = 'f';
				$title.children('label').text('内容标题')
				.next().children('input').attr('title', '内容标题').attr('placeholder', '请输入内容标题');
				$from.css('display','').children('label').text('内容来源')
				.next().children('input').attr('title', '内容来源').attr('placeholder', '请输入内容来源');
				$author.css('display', '').children('label').text('作者')
				.next().children('input').attr('title', '作者').attr('placeholder', '请输入作者');
				$pic.css('display', '').children('label').text('图片')
				.next().find('div.alert-warning').text('提示:展示图片，支持jpg格式');
				$keyWord.css('display', 'none');
				$desc.css('display', 'none');
				$articleUrl.css('display', 'none');
			break;
			case 2:
				articleAttr = 'h';
				$title.children('label').text('链接标题')
				.next().children('input').attr('title', '链接标题').attr('placeholder', '请输入链接标题');
				$pic.css('display', '').children('label').text('图片')
				.next().find('div.alert-warning').text('提示:展示图片，支持jpg格式');
				$from.css('display', 'none');
				$author.css('display', 'none');
				$keyWord.css('display', 'none');
				$articleUrl.css('display', '');
				$content.css('display', 'none');
				$desc.css('display', 'none');
			break;
			case 3:
				articleAttr = 'p';
				$title.children('label').text('姓名')
				.next().children('input').attr('title', '姓名').attr('placeholder', '姓名');
				$pic.css('display', '').children('label').text('照片')
				.next().find('div.alert-warning').text('提示: 证件照，支持jpg格式');
				$from.css('display', 'none');
				$author.css('display', 'none');
				$keyWord.css('display', 'none');
				$desc.css('display', '').children('label').text('职位')
				.next().children('textarea').attr('placeholder', '请输入职位').attr('data-bv-stringlength-message', '').next().next().text('');
				$content.css('display', '').children('label').text('个人简历');
				$articleUrl.css('display', 'none');
			break;
		}
	}

	var articleTitle="<#if categoryTitle?has_content>${categoryTitle}&nbsp;</#if><#if article.basicId !=0><small>编辑文章</small><#else><small>添加文章</small></#if>";
	$(".ms-content-body-title>span").html(articleTitle);	

	$("input[name='articleUrl']").parent().hide();
	
	//链接地址
	var actionUrl="";
	
	<#if article.basicId !=0>
	actionUrl = "${managerPath}/cms/article/${article.basicId?c?default(0)}/update.do";
	var type="${article.articleType?default('')}";
	var articleType = new Array;
	//文章属性
	$("#articleForm input[name='radio']").each(function(){
		if(type!=""){
			articleType = type.split(",");
		  	for(i=0;i<articleType.length;i++){
				if($(this).val()==articleType[i]){
					$(this).attr("checked",'true');
				}
			}
		}
	});
	articleBasicId=${article.basicId?c?default(0)};
	<#else>
	actionUrl = "${managerPath}/cms/article/save.do";
	</#if>	
	
	//获取当前栏目的自定义模型
	var url="${managerPath}/mdiy/contentModel/contentModelField/"+${categoryId?default(0)}+"/queryField.do";
	var articleId="basicId="+${article.basicId?c?default(0)};
	$(this).request({url:url,data:articleId,method:"post",func:function(data) {
		$("#addFieldForm").html(data);
	}});
	var clickDate = 0;
	var doubleClickDate = 0;
	//更新或保存		
	$("#saveUpdate").click(function(){
		if(clickDate == 0){
			clickDate = +new Date();
		}else {
			doubleClickDate = +new Date();
			if(doubleClickDate - clickDate <= 3000){
				clickDate = doubleClickDate;
				return;
			}
		}
		//禁用按钮
		$("#saveUpdate").attr("disabled",true);
		// 链接格式不正对返回
		if(!bool) {
			<@ms.notify msg="请输入正确的链接" type="warning"/>
			$("#saveUpdate").attr("disabled",false);
			return;
		}
		//获取按钮值
		var bottonText = $("#saveUpdate").text().trim();
		//设置按钮加载状态值
		$("#saveUpdate").attr("data-loading-text",bottonText+"中");
		
		var articleDateTimeValue = $("#articleDateTime").val()+":00";//让时间能精确到秒与后台对应
		$("#basicDateTime").val(articleDateTimeValue); //给basicDateTime字段赋值

		var basicCategoryId="";
		<#if !isEditCategory><!-- 如果不是单篇 -->
			basicCategoryId=$("input[name='basicCategoryId']").val(); //多篇时的文章栏目
		<#else>
			basicCategoryId=${categoryId}; //单篇时的文章栏目
		</#if>
		//文章所属栏目是数字且不能为0
		if(basicCategoryId !=0 && !isNaN(basicCategoryId) ){
			//将表单序列化
			var dataMsg = $("#articleForm").serialize();
			dataMsg += "&basicCategoryId="+ basicCategoryId + "&radio=" + articleAttr;

			var seeMsg = "";
			<#if article.basicId !=0>
	   			seeMsg="更新中....";
	   		<#else>
	   			seeMsg="保存中....";
	   		</#if>
	   		var vobj = $("#articleForm").data('bootstrapValidator').validate();
			if(vobj.isValid()){
				if(isNaN($("input[name=basicSort]").val())){
					<@ms.notify msg="自定义排序必须是数字" type="warning"/>
					$("input[name=basicSort]").val(0);
					//启用按钮
					$("#saveUpdate").button('reset');
					return;
				}
				if(fileObj == ''){
					$(this).request({url:actionUrl,data:dataMsg,loadingText:seeMsg,method:"post",type:"json",func:function(obj) {
						//执行加载状态
						$("#saveUpdate").button('loading');
						if(obj.result){
							<#if article.basicId !=0>
								<@ms.notify msg="更新文章成功" type="success"/>
							<#else>
								<@ms.notify msg="保存文章成功" type="success"/>
							</#if>
							var columnType = ${columnType?default(0)};
							if(columnType == 1){
								//更新并生成之后路径进行跳转
								location.href="${managerPath}/cms/article/${categoryId?default(0)}/main.do";
							}else{
								var dataId = obj.resultData;
								if(dataId!=""){
									location.href = base+"${baseManager}/cms/article/"+dataId+"/edit.do";
								}
							};
							$("#saveUpdate").button('reset');
						}else{
							$('.ms-notifications').offset({top:43}).notify({
								type:'warning',
								message: { text:obj.resultMsg }
							}).show();
						}
					}});
				}else{
					var formData = new FormData();
					formData.append('file', fileObj);
					formData.append('appId', ${appId});
					formData.append('contentPath', 'thumbnails');
					$.ajax({
						url: '${managerPath}/file/upload',
						type: 'post',
						data: formData,
						cache: false,
						processData: false,
						contentType: false,
						success: function(data){
							if(data.result){
								dataMsg += "&basicThumbnails=" + data.resultData[0].fileUrl;
								$(this).request({url:actionUrl,data:dataMsg,loadingText:seeMsg,method:"post",type:"json",func:function(obj) {
									//执行加载状态
									$("#saveUpdate").button('loading');
									if(obj.result){
										<#if article.basicId !=0>
											<@ms.notify msg="更新文章成功" type="success"/>
										<#else>
											<@ms.notify msg="保存文章成功" type="success"/>
										</#if>
										var columnType = ${columnType?default(0)};
										if(columnType == 1){
											//更新并生成之后路径进行跳转
											location.href="${managerPath}/cms/article/${categoryId?default(0)}/main.do";
										}else{
											var dataId = obj.resultData;
											if(dataId!=""){
												location.href = base+"${baseManager}/cms/article/"+dataId+"/edit.do";
											}
										};
										$("#saveUpdate").button('reset');
									}else{
										$('.ms-notifications').offset({top:43}).notify({
											type:'warning',
											message: { text:obj.resultMsg }
										}).show();
									}
								}});
							}
						},
						error: function(e){
							console.log(e);
						}
					})
				}
			}
		}else{
			<@ms.notify msg="请选择文章所属栏目" type="warning"/>
			//启用按钮
			$("#saveUpdate").attr("disabled",false);
		}
		 //启用按钮
		 $("#saveUpdate").button('reset');
	     $("#saveUpdate").attr("disabled",false);
	});
	
});
//选择栏目后查询自定义模型
function clickZtreeId(event,treeId,treeNode){
	if(treeNode.columnType == 2){
		<@ms.notify msg="不能选择单篇栏目" />
		return false;
	}
	if(treeNode.isParent==true){
		<@ms.notify msg="不能选择父级栏目" />
		return false;
	}
	var url="${managerPath}/mdiy/contentModel/contentModelField/"+treeNode.categoryId+"/queryField.do";
	var basicId="basicId=${article.basicId?c?default(0)}";
	$(this).request({url:url,data:basicId,method:"post",func:function(data) {
		$("#addFieldForm").html("");
		$("#addFieldForm").html(data);
	}});
} 

</script>