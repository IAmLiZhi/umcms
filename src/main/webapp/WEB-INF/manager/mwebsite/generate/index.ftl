<@ms.html5>
	<@ms.nav title="静态化">
	<#--  <button id="synchronous" style="position: relative; z-index: 1000; border:0; color: #ffffff; background-color: #337ab7; float: right; margin: 8px 13px 0 0; height: 20px;">同步资源</button>  -->
	<#--  <@ms.button class="btn btn-primary" id="synchronous" value="同步资源" />  -->
	<select name="websites" id="websites" style="height: 34px; width: 200px; float: right; margin: 5px 0 0 10px;">
		<#if websites??>
			<#list websites as website>
				<#if websiteId?eval == website.appId> 
					<option value=${website.appId} selected>${website.appName}</option>
				<#else>
					<option value=${website.appId}>${website.appName}</option>
				</#if>
			</#list>
		<#else>
			<option value=0>未找到子站</option>
		</#if>
	</select>
	<p style="float: right; font-size: 13px; color: #555555; margin: 0; font-weight: bold;"><span style="color: #FF6666;">*</span>当前子站</p>

	</@ms.nav>
	<@ms.panel>
		<p class="alert alert-info" role="alert">
			<span class="glyphicon glyphicon-pushpin text-lef "></span>
			<a class="alert-link text-lef" style="margin-left: 12px;">
        		更新主页，如果系统存在引导页面可以手动修改主页位置文件名,default.html引导页面index.html主页。
			</a><br/>
			<span class="glyphicon glyphicon-pushpin text-lef "></span>
			<a class="alert-link text-lef" style="margin-left: 12px;">
        		更新栏目列表，推荐使用指定栏目更新。系统提示“更新中...”请不要刷新页面或点击其他菜单。
			</a><br/>
			<span class="glyphicon glyphicon-pushpin text-lef "></span>
			<a class="alert-link text-lef" style="margin-left: 12px;">
            	根据时间与栏目类型生成文章
			</a>
			<span style="display: inline-block; float: right; margin-top: -45px; margin-right: 20px;">
				<@ms.button class="btn btn-primary" id="synchronous" value="同步资源" />
				<span style="display: block; margin-top: 10px;">网站风格更新后需要手动同步静态资源</span>
			</span>
		</p>		
		<@ms.form name="generateIndex">
			<@ms.formRow label="选择主页模板" width="300">
					<select class="form-control" id="select_id" disabled="disabled"></select>
					<input type="hidden" name="url"/>				
			</@ms.formRow>
			<@ms.text  id="position" label="选择主页位置	" width="300" value="index.html" placeholder="输入主页位置" name="position"  help="注:主页位置htm文件名一般为index.html或default.html"/>
			<div class="form-group ms-form-group"><div class="col-sm-2"></div><div class="col-sm-9 ms-from-group-input ms-form-input">
				<@ms.button class="btn btn-primary" id="updateIndex" value="生成主页" /> 	
				<@ms.panelNavBtnSave title="" id="viewIndex" value="预览主页" />
			</div>
			</div>
		</@ms.form>
		<!--选择更新的栏目-->
		<@ms.form name="generateColumn">
			<@ms.formRow label="选择要生成栏目" width="300">
					<#if list?has_content>
						<@ms.treeInput treeId="inputTree" json="${list?default('')}" jsonId="categoryId" jsonPid="categoryCategoryId" jsonName="categoryTitle" buttonText="选择更新的栏目" addNodesName="所有栏目" inputName="columnId" showIcon="true" expandAll="true" />
					<#else>
						<@ms.treeInput treeId="errorTree" buttonText="暂无数据" />
					</#if>
			</@ms.formRow>
			<div class="form-group ms-form-group"><div class="col-sm-2"></div><div class="col-sm-9 ms-from-group-input ms-form-input">
				<@ms.button class="btn btn-primary" id="updateColumn" value="生成栏目" />
			</div></div>
		</@ms.form>
		<@ms.form name="generateArticle">
			<@ms.formRow label="文章栏目" width="300">
        			<#if list?has_content>
						<@ms.treeInput  treeId="inputTreee" json="${list?default('')}" jsonId="categoryId" jsonPid="categoryCategoryId" jsonName="categoryTitle" addNodesName="所有栏目" buttonText="选择更新的栏目" inputName="articleId"  showIcon="true" expandAll="true" />
    				<#else> 
						<@ms.treeInput  treeId="errorTree"  buttonText="暂无数据" />
    				</#if>	
    		</@ms.formRow>
            <#--  <@ms.date name="dateTime" label="指定时间" single=true readonly="readonly" width="300" value="${now?string('yyyy-MM-dd')}" validation={"required":"true", "data-bv-notempty-message":"必填项目"} placeholder="点击该框选择时间段"  />  -->
			<@ms.text name="dateTime" id="articleDateTime" readonly="readonly" width="300" label="指定时间" class="laydate-icon timeUstyle stateUTime" title="指定时间"  validation={"required":"true", "data-bv-notempty-message":"必填项目"}  placeholder="请输入日期"  value="${now?string('yyyy-MM-dd')}" />
			<div class="form-group ms-form-group"><div class="col-sm-2"></div><div class="col-sm-9 ms-from-group-input ms-form-input">
				<@ms.button class="btn btn-primary" id="updateArticle" value="生成文章" />
			</div></div>
		</@ms.form>	
	</@ms.panel>
</@ms.html5>	

<!--点击进行按钮提交-->
<script type="" src="../../../../static/skin/manager/4.6.4/js/laydate.js"></script>
<script>
	$(function(){
		$('#articleDateTime').click(function(){
			laydate({istime: true,format:'YYYY-MM-DD hh:mm'});
			var mylaydate = $('#laydate_box');
			mylaydate.remove();
			mylaydate.css('left', '15px').css('top', '34px');
			$('#articleDateTime').parent().append(mylaydate);
		});
		var URL="${managerPath}/mwebsite/template/queryTemplateFileForColumn.do?websiteId=${websiteId}";
		var DATA = "";
		$(this).request({url:URL,data:DATA,type:"json",method:"get",func:function(msg) {
			if(msg.length==0){
				$("#select_id").append("<option value='' >暂无文件</option>")
			}
			for(var i = 0;i<msg.length;i++){
				$("#select_id").append("<option value="+msg[i]+">"+msg[i]+"</option>");
				//如果存在index.html
				if(msg[i]=="index.html" || msg[i]=="index.htm"){
					$("#select_id").find("option[value='"+msg[i]+"']").attr("selected",true);
				}
			}
		}});
		$('#synchronous').click(function(){
			$(this).text('更新中...');
			$.ajax({
				url: '${managerPath}/mwebsite/generate/syncFolder.do?websiteId=${websiteId}',
				type: 'post',
				contentType: "application/json",
				success: function(date){
					if(date){
						$('#synchronous').text('同步资源');
					}
				},
				error: function(e){
					console.log(e)
				}
			})
		})
	})
	
	//点击一键更新主页时，进行主页更新
	$("#updateIndex").click(function(){
		//选择的主页模板名称和主页位置
		var url = $("#select_id").val();
		var position =$("input[name='position']").val();
		//封装ajax请求参数
		var URL="${managerPath}/mwebsite/generate/generateIndex.do?websiteId=${websiteId}";
		var DATA = "url="+url+"&position="+position;
		$(this).html("更新中..").attr("disabled", "disabled");
		$(this).request({url:URL,data:DATA,type:"json",method:"post",func:function(msg) {
			$("#updateIndex").html("更新主页").removeAttr("disabled");
			if(msg.result){
				<@ms.notify msg="更新成功" type="success"/>
			}else{
				$('.ms-notifications').offset({top:43}).notify({
			    	type:'warning',
			    	message: { text:msg.resultMsg }
			 	}).show();
			}
			$("#updateIndex").html("更新主页")
		}});
	});
	//点击预览时，进行预览
	$("#viewIndex").click(function(){
		var position =$("input[name='position']").val();
		window.open("${managerPath}/mwebsite/generate/"+position+"/viewIndex.do?websiteId=${websiteId}");
	});
	//
	$('#websites').change(function(){
		var self = $(this);
		var appId = self.val();
		location.href = '${managerPath}/mwebsite/generate/index.do?websiteId=' + appId;
	});
</script>

<!--点击生成栏目按钮-->
<script>
	$("#updateColumn").click(function() {
		var columnId = 0;
		if($("input[name='columnId']").val() !="" && $("input[name='columnId']").val().length>0){
			columnId = $("input[name='columnId']").val();
		}
		var URL="${managerPath}/mwebsite/generate/"+columnId+"/genernateColumn.do?websiteId=${websiteId}";
		$(this).html("更新中..").attr("disabled", "disabled");
		$(this).request({url:URL,data:columnId,type:"json",method:"post",func:function(msg) {
			$("#updateColumn").html("更新栏目").removeAttr("disabled");
			//回调处理方式
			if(msg.result){
				<@ms.notify msg="更新成功" type="success"/>
			}else{
				$('.ms-notifications').offset({top:43}).notify({
			    	type:'warning',
			    	message: { text:msg.resultMsg }
			 	}).show();
			}
		}});
	});
</script>

<!--点击生成文章按钮-->
<script>
	$("#updateArticle").click(function() {
		var articleId = 0;
		if($("input[name='articleId']").val() !="" ){
			articleId = $("input[name='articleId']").val();
		}
		var URL="${managerPath}/mwebsite/generate/" + articleId + "/generateArticle.do?websiteId=${websiteId}";
		var DATA = "dateTime=" + $("input[name='dateTime']").val();
		$(this).html("更新中..").attr("disabled", "disabled");
		
		$(this).request({url:URL,data:DATA,type:"json",method:"post",func:function(msg) {
			$("#updateArticle").html("更新文档").removeAttr("disabled");
			//回调处理方式
			if(msg.result){
				<@ms.notify msg="更新成功" type="success"/>
			}else{
				$('.ms-notifications').offset({top:43}).notify({
			    	type:'warning',
			    	message: { text:msg.resultMsg }
			 	}).show();
			}
		}});
	});
</script>
