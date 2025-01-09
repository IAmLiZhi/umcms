<@ms.html5>
	<@ms.nav title="文章列表"></@ms.nav>
	<@ms.searchForm name="searchForm" action="">
		<#if articleTypeList?has_content>
			<@ms.select label="文章属性" default="全部" value="a" name="articleType" id="forumSelect"  list=articleTypeList  listValue="dictLabel"   listKey="dictValue"    value="${articleType?default('')}"/>
		<#else>
			<@ms.select label="文章属性" default="全部" value="a" name="articleType" id="forumSelect" list=[]  />
		</#if>
		<@ms.text label="文章标题"  name="basicTitle" value="" title="请输入文章标题"  placeholder="请输入文章标题" />
		<@ms.searchFormButton>
			<@ms.queryButton id="submitSearch" />								
		</@ms.searchFormButton>
	</@ms.searchForm>
	<div id="toolbar">
		<@ms.panelNavBtnGroup>
			<@ms.panelNavBtnAdd title=""/> 
			<@ms.panelNavBtnDel title=""/> 
			<#--  <#if appId != 1>  -->
				<button id="pushToMainButton" style="border: 0; margin: -1px 0 0 10px; padding: 0 10px;">推送到主站</button>
			<#--  </#if>  -->
			<button id="pushToLocalButton" style="border: 0; margin: -1px 0 0 10px; padding: 0 10px;">推送到本站</button>
		</@ms.panelNavBtnGroup>
	</div>
	<@ms.panel>
		<table id="articleListTable"
			data-show-refresh="true"
	        data-show-columns="true"
	        data-show-export="true"
			data-method="post" 
			data-detail-formatter="detailFormatter" 
			data-pagination="true"
			data-page-size="10"
			data-side-pagination="server">
		</table>
	</@ms.panel>
</@ms.html5>	
<!--删除文章-->
<@ms.modal modalName="delete" title="删除文章">
	  <@ms.modalBody>
	  		确定要删除所选的文章吗?
     </@ms.modalBody>
	 <@ms.modalButton>
 		<@ms.button class="btn btn-danger rightDelete" value="确定"/>
 	</@ms.modalButton>
</@ms.modal>
<!--推送文章-->
<@ms.modal modalName="push" title="推送文章">
	 <@ms.modalBody>
	  		确定要推送所选的文章吗?
     </@ms.modalBody>
	 <@ms.modalButton>
 		<@ms.button id="push" value="确定"/>
 	</@ms.modalButton>
</@ms.modal>
<script>
	$(function(){
		var search = $("form[name='searchForm']").serializeJSON();
		var articleType = search.articleType; 
		$("#articleListTable").bootstrapTable({
		url:"${managerPath}/cms/article/${categoryId}/mylist.do?articleTypeStr="+articleType,
		contentType : "application/x-www-form-urlencoded",
		queryParamsType : "undefined",
		toolbar: "#toolbar",
	    columns: [{ checkbox: true}, {
	        field: 'column.categoryTitle',
	        title: '栏目名',
	        width: 90,
	        align: 'left'
	    },{
	        field: 'basicTitle',
	        title: '文章标题',
	        formatter: function (value, row, index){
	        	var url='${managerPath}/cms/article/'+row.articleID+"/preview.do";
	        	return "<a href="+url+" target='_blank' >"+value+"</a>";
	    	}
	    }, {
	        field: 'articleAuthor',
	        title: '作者',
	        width: 60
	    }, {
	        field: 'basicSort',
	        title: '排序',
	        align: 'center',
	        width: 30
	    }, {
	        title: '状态',
	        align: 'center',
	        width: 60,
	        formatter: function (value, row, index){
	        	var stateDesc = "未知";
	        	switch(row.articleState)
				{
				case 0:
				  stateDesc = "编辑中";
				  break;
				case 1:
				  stateDesc = "审核中";
				  break;
				case 2:
				  stateDesc = "已通过";
				  break;
				case 3:
				  stateDesc = "未通过";
				  break;
				case 4:
				  stateDesc = "待发布";
				  break;
				case 5:
				  stateDesc = "已发布";
				  break;
				default:
				  break;
				}
				return stateDesc;
	    	}
	    }, {
	        title: '编辑',
	        align: 'center',
	        width: 40,
	        formatter: function (value, row, index){
	        	var url='${managerPath}/cms/article/'+row.articleID+"/edit.do?" + row.articleType;
				if(row.articleState == 1 || row.articleState == 5){
	    			return "<a href='javascript:;' title='已发布和审核中的文章不能被编辑!'><i class='glyphicon glyphicon-pencil' style='color:#428BCA'></i></a>";
				}else{
	    			return "<a href="+url+" target='_self' ><i class='glyphicon glyphicon-pencil' style='color:#428BCA'></i></a>";
				}
	    	}
	    }, {
	        field: 'basicDateTime',
	        title: '发布时间',
	        align: 'center',
	        width: 90
	    }]
	});
	//查询文章标题
	$("#submitSearch").click(function(){
		var search = $("form[name='searchForm']").serializeJSON();
		var params = $('#articleListTable').bootstrapTable('getOptions');
		params.queryParams = function(params) {  
		 	$.extend(params,search);
	        	return params;  
			}  
		$("#articleListTable").bootstrapTable('refresh', {query:$("form[name='searchForm']").serializeJSON()});
	});
	//添加文章
	$("#addButton").click(function(){
		if(${isParent}==true){
			<@ms.notify msg="不能选择父级栏目" />
			return false;
		}
		location.href = base+"${baseManager}/cms/article/add.do?categoryId=${categoryId?default(0)}&modelId=${Session.model_id_session?default(0)}&categoryTitle=${categoryTitle?default('')}"; 
	});	
	//点击重置按钮
	$(".reset").click(function(){
		$("input[name=basicTitle]").val("");
	})
	//多选删除确认
	$("#delButton").click(function(){
		//获取勾选的所在行的所有数据
		var rows = $("#articleListTable").bootstrapTable("getSelections");
		if(rows!=""){
			var bool = true;
			for(var i = 0; i < rows.length; i++){
				var state = rows[i].articleState;
				if(state == 1){
					bool = false;
				}
			}
			if(bool){
				//弹出modal窗体
				$(".delete").modal();
			}else{
				<@ms.notify msg="不能删除审核中的文章!" type="warning"/>
			}
		}else{
			<@ms.notify msg="请选择文章!" type="warning"/>
		}
	});
	//删除调用
	$(".rightDelete").click(function(){
		var rows = $("#articleListTable").bootstrapTable("getSelections");
		if(rows!=""){
			$.ajax({
				url:base+"${baseManager}/cms/article/delete.do",
				type:'post',
				dataType:'json',
				data:JSON.stringify(rows),
				contentType:'application/json',
				success:function(msg) {
					if (msg.result) {
						<@ms.notify msg="删除成功!" type="success"/>
						location.reload();
					} else {
						<@ms.notify msg="删除失败" type="fail"/>
					}
				}
			});
		}else{
	    	<@ms.notify msg="请选择文章!" type="warning"/>
	    }
	});
	
	//推送确认
	var pushType = 1; //主站是1，本站是2
	$("#pushToMainButton").click(function(){
		//获取勾选的所在行的所有数据
		var rows = $("#articleListTable").bootstrapTable("getSelections");
		if(rows!=""){
			var bool = true;
			for(var i = 0; i < rows.length; i++){
				var state = rows[i].articleState;
				if(state != 5){  //只有发布状态才可以推送
					bool = false;
					break;
				}
			}
			if(bool){
				pushType = 1;
				//弹出modal窗体
				$(".push").modal();
			}else{
				<@ms.notify msg="只能推送已发布的文章!" type="warning"/>
			}
		}else{
			<@ms.notify msg="请选择文章!" type="warning"/>
		}
	});
	$("#pushToLocalButton").click(function(){
		//获取勾选的所在行的所有数据
		var rows = $("#articleListTable").bootstrapTable("getSelections");
		if(rows!=""){
			var bool = true;
			for(var i = 0; i < rows.length; i++){
				var state = rows[i].articleState;
				if(state != 5){  //只有发布状态才可以推送
					bool = false;
					break;
				}
			}
			if(bool){
				pushType = 2;
				//弹出modal窗体
				$(".push").modal();
			}else{
				<@ms.notify msg="只能推送已发布的文章!" type="warning"/>
			}
		}else{
			<@ms.notify msg="请选择文章!" type="warning"/>
		}
	});
	//推送调用
	$("#push").click(function(){
		var rows = $("#articleListTable").bootstrapTable("getSelections");
		if(rows!=""){
			$.ajax({
				url:base+"${baseManager}/cms/article/push.do?type="+pushType,
				type:'post',
				dataType:'json',
				data:JSON.stringify(rows),
				contentType:'application/json',
				success:function(msg) {
					if (msg.result) {
						<@ms.notify msg="推送成功!" type="success"/>
						location.reload();
					} else {
						<@ms.notify msg="推送失败" type="fail"/>
					}
				}
			});
		}else{
	    	<@ms.notify msg="请选择文章!" type="warning"/>
	    }
	});
});
</script>













