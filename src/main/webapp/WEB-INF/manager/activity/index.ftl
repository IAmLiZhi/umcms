<@ms.html5>
	<@ms.nav title="工作流"></@ms.nav>
	<!--@ms.searchForm name="searchForm" isvalidation=true>
		<@ms.searchFormButton>
			 <@ms.queryButton onclick="search()"/> 
		</@ms.searchFormButton>			
	</@ms.searchForm-->
	<@ms.panel>
		<div id="toolbar">
			<@ms.panelNav>
				<@ms.buttonGroup>
					<#include "${managerViewPath}/activity/shiro-button.ftl"/>
				</@ms.buttonGroup>
			</@ms.panelNav>
		</div>
		<table id="activityList" 
			data-show-refresh="true"
			data-show-columns="true"1
			data-show-export="true"
			data-method="post" 
			data-side-pagination="server">
		</table>
	</@ms.panel>
	
	<@ms.modal  modalName="delColumn" title="删除工作流" >	
		<@ms.modalBody>确定要删除所选的流程么
			<@ms.modalButton>
				<!--模态框按钮组-->
				<@ms.button  value="确认" class="btn btn-danger"  id="deleteColumnBtn"  />
			</@ms.modalButton>
		</@ms.modalBody>
	</@ms.modal>
</@ms.html5>
<script>
	$(function(){
		$("#activityList").bootstrapTable({
			url:"${managerPath}/activity/list.do",
			contentType : "application/x-www-form-urlencoded",
			queryParamsType : "undefined",
			toolbar: "#toolbar",
	    	columns: [{ checkbox: true},
	    		{
		        	field: 'name',
		        	title: '工作流名称',
		        	align: 'left',
		        	formatter: function (value, row, index){
						var url="${managerPath}/activity/add.do?id=" + row.id;
						return "<a href=" +url+ " target='_self'>" + value + "</a>";
						//var url="${managerPath}/activity/add.do";
			    		//return "<a href='" + url + "?id=" + row.id + "&modelId=37&modelTitle=工作流' target='_self'>" + value + "</a>";
			    	}
		    	},{
		        	field: 'description',
		        	title: '描述',
		        	align: 'center'
		        	
		    	},{
		        	field: 'sort',
		        	title: '排序',
		        	align: 'center'
		        	
		    	},{
		        	field: 'steps',
		        	title: '审核步骤',
		        	align: 'left'
		        	
		    	},{
		        	field: 'columnName',
		        	title: '栏目名称',
		        	align: 'left'
		        	
		    	}]
	    })
	})
 
	
	//增加按钮
	$("#addColumnBtn").click(function(){
		var ss = "${managerPath}/activity/add.do";
		location.href = ss; 
	})
	//删除按钮
	$("#delColumnBtn").click(function(){
		//获取checkbox选中的数据
		var rows = $("#activityList").bootstrapTable("getSelections");
		//没有选中checkbox
		if(rows.length <= 0){
			<@ms.notify msg="请选择需要删除的记录" type="warning"/>
		}else{
			$(".delColumn").modal();
		}
	})
	
	$("#deleteColumnBtn").click(function(){
		var rows = $("#activityList").bootstrapTable("getSelections");
		$(this).text("正在删除...");
		$(this).attr("disabled","true");
		var ids = [];
		for(var i=0;i<rows.length;i++){
			ids[i] = rows[i].id;
		}
		ids.reverse();
		$.ajax({
			type: "post",
			url: "${managerPath}/activity/delete.do",
			dataType: "json",
			data:JSON.stringify({'ids':ids}),
			contentType: "application/json",
			success:function(msg) {
				if(msg.result == true) {
					<@ms.notify msg= "删除成功" type= "success" />
				}else {
					<@ms.notify msg= "删除失败" type= "warning" />
				}
				location.reload();
			}
		})
	});
	//查询功能
	function search(){
		var search = $("form[name='searchForm']").serializeJSON();
        var params = $('#activityList').bootstrapTable('getOptions');
        params.queryParams = function(params) {  
        	$.extend(params,search);
	        return params;  
       	}  
   	 	$("#activityList").bootstrapTable('refresh', {query:$("form[name='searchForm']").serializeJSON()});
	}
</script>