<@ms.html5>
	<@ms.nav title="推送列表"></@ms.nav>
	<@ms.searchForm name="searchForm" action="">
		<#if websites?has_content>
			<@ms.select label="来源网站" default="全部" value="0" name="srcAppId" id="srcAppId"  list=websites  listValue="appName"   listKey="appId" />
		<#else>
			<@ms.select label="来源网站" default="全部" value="0" name="srcAppId" id="srcAppId" list=[]  />
		</#if>
		<@ms.text label="文章标题"  name="linkTitle" value="" title="请输入文章标题"  placeholder="请输入文章标题" />
		<@ms.searchFormButton>
			<@ms.queryButton id="submitSearch" />		
		</@ms.searchFormButton>
	</@ms.searchForm>
	<@ms.panel>
		<div id="toolbar">
			<@ms.panelNavBtnGroup>
				<@ms.panelNavBtnDel title=""/>
				<button id="pubButton" style="height: 34px; width: 100px; padding: 0">发布</button>
			</@ms.panelNavBtnGroup>
		</div>
		<table id="articleListTable" 
			data-show-columns="true"
			data-show-refresh="true"
			data-method="post" 
			data-side-pagination="server">
		</table>
	</@ms.panel>
	<div class="mask" style="position: fixed; left: 0; top: 0; z-index: 1000; height: 100%; width: 100%; background-color: rgba(0,0,0,0.5); display: none;">
		<div class="alert" style="width: 413px; height: 498px; background-color: #ffffff; padding: 17px 21px; margin: 33px auto 0;">
			<p style="overflow: hidden; font-size: 14px; color: #333333;">选择推送文章的发布栏目 <img class="close" style="width: 10px; height: 10px; float: right; margin-top: 5px; cursor: pointer; opacity: 1;" src="../../../../static/skin/manager/4.6.4/images/close.png"/></p>
			<div id="myTree1" style="height: 390px; overflow-y: scroll;">
				<div>
					<ul id="treeDemo1" class="ztree"></ul>
				</div>
			</div>
			<p style="text-align: right; margin-top: 23px;"><button class="cancel" style="background-color: #ffffff; border: 1px solid #666666; color: #666666; font-size: 14px; width: 71px; height: 28px; margin-right: 11px;">取消</button><button class="choose" style="width: 71px; height: 28px; background-color: #00AEFF; border: 0; font-size: 14px; color: #ffffff;">选择</button></p>
		</div>
	</div>
</@ms.html5>
<!--删除文章-->
<@ms.modal modalName="delete" title="删除文章">
	  <@ms.modalBody>
	  		确定要删除所选的文章吗?
     </@ms.modalBody>
	 <@ms.modalButton>
 		<@ms.button class="btn btn-danger rightDelete delete mydelete" value="确定"/>
 	</@ms.modalButton>
</@ms.modal>
<!--发布文章-->
<@ms.modal modalName="pub" title="发布文章">
	 <@ms.modalBody>
	  		确定要发布所选的文章吗?
     </@ms.modalBody>
	 <@ms.modalButton>
 		<@ms.button id="push" value="确定"/>
 	</@ms.modalButton>
</@ms.modal>
<script>
	window.onload = function(){
		var $mask = $('.mask');
		var checkedId1 = [];
		// 取消，关闭
		$('.close, .cancel').click(function(){
			$mask.css('display', 'none');
		});
		//选择
		$('.choose').click(function(){
			if(checkedId1.length == 0){
				<@ms.notify msg="请选择栏目!" type="warning"/>
				return;
			}
			$mask.css('display','none');
			$(".pub").modal();
		});
		// 生成表格
		//var websiteId = $("#websites").val();
		var search = $("form[name='searchForm']").serializeJSON();
		var srcAppId = search.srcAppId;
		$("#articleListTable").bootstrapTable({
			url:"${managerPath}/cms/push/list.do"+"?srcAppIdStr="+srcAppId,
			contentType : "application/x-www-form-urlencoded",
			queryParamsType : "undefined",
			toolbar: "#toolbar",
			pagination :true,		//在底部显示分页组件
			pageList: [10, 20],		//设置页面可以显示的数据条数
            pageSize: 10,			// 页面数据条数
            pageNumber: 1, 
	    	columns: [{ checkbox: true}, 
				{
			        field: 'pushId',
			        title: '编号',
			        width: 50,
			        align: 'left'
			    },{
			        field: 'linkTitle',
			        title: '文章标题',
			        formatter: function (value, row, index){
			        	var url=row.linkUrl;
			        	return "<a href="+url+" target='_blank' >"+value+"</a>";
			    	}
			    },{
			        field: 'srcAppName',
			        title: '来源网站',
			        width: 100
			    },{
			        field: 'srcColumnName',
			        title: '来源栏目',
			        width: 100
			    },{
			        field: 'createTime',
			        title: '推送时间',
			        width: 100
			    },{
			        field: 'state',
			        title: '状态',
			        width: 60,
			        formatter: function (value, row, index){
			        	if(row.state == 0){
			        		return "待发布";
			        	}else{
			        		return "已发布";
			        	}
			    	}
			    },{
			        field: 'destColumnName',
			        title: '发布栏目',
			        align: 'center',
			        width: 90
			    }, {
			        field: 'pubTime',
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
		//多选删除确认
		$("#delButton").click(function(){
			//获取勾选的所在行的所有数据
			var rows = $("#articleListTable").bootstrapTable("getSelections");
			if(rows!=""){
				//弹出modal窗体
				$(".delete").modal();
			}else{
				<@ms.notify msg="请选择文章!" type="warning"/>
			}
		});
		//删除调用
		$(".mydelete").click(function(){
			var rows = $("#articleListTable").bootstrapTable("getSelections");
			if(rows!=""){
				$.ajax({
					url:"${managerPath}/cms/push/delete.do",
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
		//多选发布确认
		$("#pubButton").click(function(){
			var rows = $("#articleListTable").bootstrapTable("getSelections");
			if(rows!=""){
				$('.mask').css('display','');
				$.post('${managerPath}/cms/column/listColumn.do',
					function(data){
						var setting1 = {
							check: {
								enable: true,
								radioType: "all",
								chkStyle: "radio"
							},
							data: {
								key: {
									name: 'categoryTitle'
								},
								simpleData: {
									enable: true,
									idKey: 'categoryId',
									pIdKey: 'categoryParentId',
									rootPId: null,
								}
							},
							callback: {
								onCheck: onCheck1,
								beforeCheck : function(treeId, treeNode) {
									if (treeNode.isParent) {
										<@ms.notify msg="请选择子栏目!" type="warning"/>
										return false;
									}
								}
							}
						};
						function onCheck1(e,treeId,treeNode){
							var treeObj = $.fn.zTree.getZTreeObj("treeDemo1"),
							nodes = treeObj.getCheckedNodes(true);
							checkedId1 = [];
							for(var i=0;i<nodes.length;i++){
								checkedId1.push(nodes[i].categoryId);
							}
						}
						for(var i = 0; i < data.length; i++){
							if(data[i].categoryParentId && data[i].categoryParentId.split(',').length > 1){
								data[i].categoryParentId = data[i].categoryParentId.split(',').pop();
							}
						}
						var treeObj1 = $.fn.zTree.init($("#treeDemo1"), setting1, data);
						treeObj1.expandAll(true);
					}
				);
			}else{
				<@ms.notify msg="请选择文章!" type="warning"/>
			}
			
		});
		//发布调用
		$("#push").click(function(){
			var rows = $("#articleListTable").bootstrapTable("getSelections");
			for(var index=0; index<rows.length; index++){
				rows[index].destColumnId = checkedId1[0];
			}
			
			$.ajax({
				url:"${managerPath}/cms/push/pub.do",
				type:'post',
				dataType:'json',
				data:JSON.stringify(rows),
				contentType:'application/json',
				success:function(msg) {
					if (msg.result) {
						<@ms.notify msg="发布成功!" type="success"/>
						location.reload();
					} else {
						<@ms.notify msg="发布失败" type="fail"/>
					}
				}
			});
		});
	}
</script>