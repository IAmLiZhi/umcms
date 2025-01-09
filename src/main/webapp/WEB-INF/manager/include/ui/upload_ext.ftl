<#--上传控件-->
<#--上传控件-->
<#--path:上传路径，文件夹是在ms属性配置文件里面配置，会自动增加当前appId-->
<#--uploadFloderPath:如果path路径无法满足可以使用这个参数重新定义上传路径-->
<#--inputName:name值-->
<#--filetype:上传规则-->
<#--size:上传数量-->
<#--msg:提示信息-->
<#--callBack:回调提示信息-->
<#--isRename:是否重命名true:将使用时间命名，否则使用上传是文件名，可能会出现同名称覆盖的问题,中文也会存在问题-->
<#--maxSize:默认文件大小 单位m-->
<#macro myuploadFile path inputName uploadFloderPath="" size=1 filetype="*.zip,*.rar,*.doc,*.xls,*.doc,*.txt,*.pdf" msg=""  maxSize="10" callBack="" isRename=false>
	<div id="ms__uploadPic_${inputName}">
		<div class="divUploader"><input type="button" id="up_file_${inputName}" />

		<select name="websites" id="websites" style="height: 34px;vertical-align: top;width: 200px; float: right; margin-right: 40px;">
		<#if websites??>
			<#list websites as website>
				<#if websiteId?eval == website.appId > 
					<option value=${website.appId} selected>${website.appName}</option>
				<#else>
					<option value=${website.appId}>${website.appName}</option>
				</#if>
			</#list>
		<#else>
			<option value=0>未找到子站</option>
		</#if>
		</select>
		<p style="float: right; font-size: 13px; color: #555555; margin: 6px 11px 0 0; font-weight: bold;"><span style="color: #FF6666;">*</span>当前子站</p>

		<#if msg!="">
		<div class="alert alert-warning alert-dismissable">${msg}</div>
		</#if>
		</div>
		<input type="hidden" size="100" name="${inputName}" id="${inputName}" value="${imgs?default('')}" />
	</div>
	<script type="text/javascript">
		    $(function ()  {
		        $('#ms__uploadPic_${inputName}').swfupload({
		            upload_url: "${basePath}/fileuploader/tmpupload.do",
		            post_params:{"uploadPath":"/${path}","uploadFloderPath":"${uploadFloderPath}","isRename":"${isRename?default("true")}","maxSize":"${maxSize}","allowedFile":"${filetype}","allowedFile":""},
		            file_size_limit: ${maxSize?default("1")}*1024,
		            file_types: "${filetype?default("*.jpg;*.png;*.gif;*.bmp;*.jpeg;*.zip;*.rar")}",
		            file_upload_limit: ${size?default("5")},
		            flash_url: "${basePath}/static/plugins/jquery.swfupload/1.0.0/swfupload.swf",
		            button_image_url: static+"/plugins/jquery.swfupload/1.0.0/button_file.png",
		         	button_width: 92,
					button_height: 34,
		            button_placeholder: $('#up_file_${inputName}')[0],
		            debug: false
		        })
				.bind('fileQueued', function (event, file) {	
				   if ($("#ms__uploadPic_${inputName} .msUploadImgs").children().length>=${size?default("5")}) {
						  $(this).swfupload('cancelUpload');
						  <@ms.notify msg= "上传图片过多或上传图片过大!" type= "warning" />
					} else {
					    $(this).swfupload('startUpload');
				    }
				    
				})
				.bind('fileQueueError', function (event, file, errorCode, message) {
				    if (errorCode==-130) {
				    	<@ms.notify msg= "文件类型错误!" type= "warning" />
				    } else if (errorCode==-100) {
				    	<@ms.notify msg= "最多上传${size}个文件!" type= "warning" />
				    } else {
				    	//alert(errorCode+'上传图片过多或上传图片过大!');
				    	<@ms.notify msg= "上传文件过大!" type= "warning" />
				    }
				    <#if callBack!=""> 
				    	eval("${callBack}(errorCode)");
				    </#if>
				})
				.bind('uploadStart', function (event, file) {
						$('#ms__uploadPic_${inputName} ul li#' + file.id).find('span.front-cover').text('0%');
				})
				.bind('uploadProgress', function (event, file, bytesLoaded) {
					   var percentage = Math.round((bytesLoaded / file.size) * 100);
				})
				.bind('uploadSuccess', function (event, file, serverData) {
						$('#${inputName}').val(serverData);
						<#if callBack!=""> 
							eval("${callBack}(serverData)");
						<#else>
							<@ms.notify msg= "上传成功" type= "success" />
						</#if>
				})
				.bind('uploadComplete', function (event, file) {
				    $(this).swfupload('startUpload');
				})
		    });
			</script>
</#macro>
