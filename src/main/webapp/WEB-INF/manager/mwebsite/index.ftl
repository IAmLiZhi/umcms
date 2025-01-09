<@ms.html5>
	<@ms.panel>
		<@ms.nav title="站点管理"></@ms.nav>
		<@ms.searchForm  name="searchForm" action="">
			<@ms.text label="标题"  name="websiteName" value="" title="请输入标题"  placeholder="请输入标题" value=""/>
			<@ms.searchFormButton>
				<@ms.queryButton id="submitSearch" onclick="query()"/>			
			</@ms.searchFormButton>
		</@ms.searchForm>
		<!--使用toolbar添加按钮-->
		<div id="toolbar">
			<@ms.panelNav>
				<@ms.buttonGroup>
					<@ms.addButton url="${base}${baseManager}/website/add.do"/>
					<@ms.panelNavBtnDel id="delButton" value="" />
				</@ms.buttonGroup>
				<@ms.menuButton links=[{"click":"setManager()","name":"设置管理员"}] name="操作"/>
			</@ms.panelNav>
		</div>
		<table id="websiteListTable"
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
	<@ms.modal  modalName="deleteModal" title="删除站点" >
		<@ms.modalBody>删除站点</@ms.modalBody>
		<@ms.modalButton>
			<!--模态框按钮组-->
			<@ms.button  value="确认删除？"  id="deletewebsite"  />
		</@ms.modalButton>
	</@ms.modal>	
	<!--添加或编辑站点管理员-->
	<@ms.modal modalName="addAndEdit" title="管理员设置">
		<@ms.modalBody>
			<@ms.form isvalidation=true name="managerForm" action="${managerPath}/website/manager/update.do" redirect="${base}${baseManager}/website/index.do">
				<@ms.hidden name="managerWebsiteId"/>
				<@ms.hidden name="modelIds"/>
				<@ms.text name="managerName" readonly="readonly" width="250" label="帐号" title="管理员帐号" maxlength="12" placeholder="请输入管理员帐号" validation={"required":"true", "data-bv-notempty-message":"请输入管理员帐号!","data-bv-regexp":"true","data-bv-regexp-regexp":"^[a-zA-Z0-9_]+$","data-bv-regexp-message":"帐号只能由英文字母，数字，下划线组成!","data-bv-stringLength-message":"帐号长度最长为12个字符！"}/>
	    		<@ms.text name="managerNickName" width="250" label="昵称" title="管理员昵称"  maxlength="15" placeholder="请输入管理员昵称" validation={"required":"true", "data-bv-notempty-message":"请输入管理员昵称!"} />
	    		<@ms.password name="managerPassword" width="250" label="密码" title="管理员密码"   maxlength="16" validation={"data-bv-regexp":"true","data-bv-regexp-regexp":"^[a-zA-Z0-9_]+$","data-bv-regexp-message":"密码只能由英文字母，数字，下划线组成!"}  />
	    		<@ms.formRow label="模块" width="300">
	    			<@ms.tree id="modelListTree" type="checkbox" url="${managerPath}/mwebsite/model/query.do" idKey="modelId" pIdKey="modelModelId" text="modelTitle"/>
	    		</@ms.formRow>
			</@ms.form>
	    </@ms.modalBody>
		<@ms.modalButton>
	 		<@ms.saveButton postForm="managerForm" postBefor="setModelIds" postAfter="closeModal"/>  
	 	</@ms.modalButton>
	</@ms.modal>
</@ms.html5>
<script>	
	$(function() {
		//对应bootstrap-table框架的控制
        $("#websiteListTable").bootstrapTable({
    		url:"${managerPath}/website/list.do",
    		contentType : "application/x-www-form-urlencoded",
    		queryParamsType : "undefined",
    		toolbar: "#toolbar",
		    columns: [{ checkbox: true},{
		        field: 'websiteId',
		        title: '编号',
		        align: 'center'
		    }, {
		        field: 'websiteName',
		         formatter: function(value,row,index){
		        	return "<a class='btn btn-xs red tooltips' data-rid='' target='_self' data-toggle='tooltip'  data-original-title='编辑站点' href='${base}${baseManager}/website/"+row.websiteId+"/edit.do' data-id='"+row.websiteId+"'>"+value+"</a>"
		        },
		        title: '标题'
		    }, {
		        field: 'websiteUrl',
		        formatter: function(value,row,index){
		        	var urlList = [];
		        	urlList = value.split("\r");
		        	var url;
		        	for(var i = 0 ; i < urlList.length ; i++){
		        		url = urlList[i];
		        		return "<a href='"+url+"' target='_self'  data-toggle='tooltip'  data-original-title='点击查看'>"+url+"<a><br/>"
		        	}
		        },
		        title: '域名'
		    }, {
		        field: 'websiteState',
		        formatter: function(value,row,index){
		        	if(value == 0){
		        		return "<font color='green'>运行中</font>"
		        	}else{
		        		return "<font color='red'>已停止</font>"
		        	}
		        },
		        title: '状态',
		        align: 'center'
		    }
		    /*
		    , {
		        field: 'websiteMobileState',
		        formatter: function(value,row,index){
		        	if(value == 0){
		        		return "<font color='green'>已启用</font>"
		        	}else{
		        		return "<font color='red'>未启用</font>"
		        	}
		        },
		        title: '手机端',
		        align: 'center'
		    }, {
		        field: 'websitePayDate',
		        formatter: function(value,row,index){
		        	var timeList = [];
		        	timeList = value.split(" ");
		        	var time = timeList[0];
		        	return time
		        },
		        title: '续费日期',
		        align: 'center'
		    }*/
		    ]
        }); 		
	})
	
	function query(){
		var search = $("form[name='searchForm']").serializeJSON();
        var params = $('#websiteListTable').bootstrapTable('getOptions');
        params.queryParams = function(params) {  
        	 $.extend(params,search);
	            return params;  
       	}  
   	 	$("#websiteListTable").bootstrapTable('refresh', {query:$("form[name='searchForm']").serializeJSON()});	
	}
	function setManager() {

		var rows =  $("#websiteListTable").bootstrapTable("getSelections");
		if (rows.length > 1) {
			<@ms.notify msg="只能选择一条站点！"/>
		} else if (rows.length == 0) {
			<@ms.notify msg="请选择需要设置站点！"/>
		} else {	
			$("input[name='managerPassword']").val("");
			var website = rows[0];
			var websiteId = website.websiteId;
			ms.post("${managerPath}/website/manager/"+websiteId+"/edit.do",null,function(manager){
				$("input[name='managerWebsiteId']").val(websiteId);
				if (manager.managerId>0) {
					$("input[name='managerName']").val(manager.managerName);
					$("input[name='managerNickName']").val(manager.managerNickName);
					$("input[name='managerId']").val(manager.managerId);
					$("input[name='managerName']").attr("readonly","readonly");
					ms.post("${managerPath}/basic/role/"+manager.managerRoleID+"/queryByRole.do",null,function(models){
						var tempModels = [];
						for (i=0;i<models.length;i++) {
							tempModels.push(models[i].modelId);
						}
						if (models != null) {
							var nodes = modelListTree.getNodes();
							modelListTree.checkAllNodes(false);
							for (i=0;i<nodes.length;i++) {
								var children = nodes[i].children;
								if (children != undefined && children.length>0) {
									for(k=0;k<children.length;k++) {
										if(jQuery.inArray(children[k].modelId, tempModels) > -1) {
											modelListTree.checkNode(children[k], true, true);
										}									
									}
								} else {
									if(jQuery.inArray(nodes[i].modelId, tempModels) > -1) {
										modelListTree.checkNode(nodes[i], true, true);
									}							
								}
							}
						}
					});					
				} else {
					$("input[name='managerNickName']").val();
					modelListTree.checkAllNodes(false)
					$("input[name='managerName']").removeAttr("readonly");
				}
			});		
			$(".addAndEdit").css('margin','-55px 0 0').modal();//打开该模态框
		}
	}
	//判断打开删除模态框的条件
	$("#delButton").click(function(){
		//没有选中checkbox
		var rows =  $("#websiteListTable").bootstrapTable("getSelections");
		if(rows.length <= 0){
			 <@ms.notify msg="请选择删除的站点" type="warning"/>
		//点击全选，但是列表为空
		}else if(rows.length == 0){
			<@ms.notify msg="请选择删除的站点" type="warning"/>
		}else{
			$(".deleteModal").modal();
		}
	})
	//批量删除
	$("#deletewebsite").click(function(){
		var rows =  $("#websiteListTable").bootstrapTable("getSelections");
		var websiteIds = [];
		for(var i = 0;i<rows.length;i++){
			websiteIds[i] = rows[i].websiteId;
		}
		$.ajax({		
		    type:"GET",
			url:"${base}${baseManager}/website/batchDelete.do",
		    data:"websiteIds="+websiteIds,
		    success:function(msg) { 
				if (msg.result == false) {
					 <@ms.notify msg="删除失败" type="fail"/>
				}else{
					location.href = base+"${baseManager}/website/index.do";
					<@ms.notify msg="删除成功!" type="success"/>
				}
			}
		});	
	})
	function closeModal(msg) {
		$(".addAndEdit").modal("hide");
	}
	
	//设置模块工
	function setModelIds() {
		var nodes = modelListTree.getCheckedNodes(true);
		var ids = [];
		for (i=0;i<nodes.length;i++) {
			ids.push(nodes[i].modelId);
		}
		$("input[name='modelIds']").val(ids);
		return true;
	}
	//删除站点的ajax
	function remove(ids) {
		ms.post("${base}${baseManager}/website/batchDelete.do","websiteIds="+ids,function(msg){
			if(msg.result){
				location.href="${base}${baseManager}/website/index.do";
			}
		});
	}
</script>