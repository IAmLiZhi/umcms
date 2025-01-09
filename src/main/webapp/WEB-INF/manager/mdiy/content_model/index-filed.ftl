<@ms.html5>
	<@ms.nav title="自定义模型字段管理" back=true></@ms.nav>
	<@ms.panel>
		<div id="toolbar">
			<@ms.panelNav>
				<@ms.buttonGroup>
					<@shiro.hasPermission name="mdiy:content:save"><@ms.addButton id="addContentModelFieldBtn"/></@shiro.hasPermission>
					<@shiro.hasPermission name="mdiy:content:del"><@ms.delButton id="delContentModelFieldBtn"/></@shiro.hasPermission>
				</@ms.buttonGroup>
			</@ms.panelNav>
		</div>
		<table id="contentModelFieldList" 
			data-show-refresh="true"
			data-show-columns="true"
			data-show-export="true"
			data-method="post" 
			data-pagination="true"
			data-page-size="10"
			data-side-pagination="server">
		</table>
	</@ms.panel>
	<@ms.modal modalName="contentModelField" title="字段设置">
	    <@ms.modalBody height="400">
	    	<@ms.form isvalidation=true  name="fieldForm" action="${managerPath}/mdiy/contentModel/contentModelField/save.do" redirect="${managerPath}/mdiy/contentModel/contentModelField/${contentModelId}/index.do">
	    		<@ms.hidden name="fieldCmid" value="${contentModelId}"  />
	    		<@ms.hidden name="fieldId" value="0"  />
	    		<@ms.text label="提示文字" maxlength="10" value="" placeholder="请输入提示文字" name="fieldTipsName" validation={"required":"true", "data-bv-notempty-message":"不能为空","data-bv-stringlength":"true","data-bv-stringlength-max":"100","data-bv-stringlength-min":"1","data-bv-stringlength-message":"长度介于1-100个字符"} />
	    		<@ms.text label="字段名" maxlength="10" value=""  placeholder="请输入字段名称" name="fieldFieldName"  
	    		validation={"required":"true", "data-bv-regexp":"true","data-bv-regexp-regexp":"^[a-zA-Z_]+$","data-bv-regexp-message":"只能是字母组成","data-bv-notempty-message":"名称不能为空且只能由字母组成！","data-bv-stringlength":"true","data-bv-stringlength-max":"10","data-bv-stringlength-min":"1","data-bv-stringlength-message":"长度介于1-15个字符"}/>			
	    		<@ms.select label="类型" list=fieldTypes name="fieldType" value="1"/>
	    		<#--@ms.text label="长度" name="fieldLength" value="0" maxlength="5" validation={"data-bv-regexp":"true","data-bv-regexp-regexp":"^[0-9]+$","data-bv-regexp-message":"只能是整型"}/-->
	    		<@ms.radio label="必填字段" name="fieldIsNull" list=[{"key":"0","value":"必填"},{"key":"1","value":"可选"}] listKey="key" listValue="value" value="0"/>
	    		<@ms.radio label="搜索字段" name="fieldIsSearch" list=[{"key":"0","value":"否"},{"key":"1","value":"是"}] listKey="key" listValue="value" value="0"/>
	    		<@ms.textarea label="默认值" name="fieldDefault"  placeholder="下拉框,多选框等存在多个默认值的必须使用英文逗号隔开" help="日期、html类型无效"/>
	    	</@ms.form>
        </@ms.modalBody>
        <@ms.modalButton>   		
	    	<@ms.saveButton  id="saveOrUpdate" postForm="fieldForm"/>  
	    </@ms.modalButton>
	</@ms.modal>
	<!--删除字段--> 		
	<@ms.modal  modalName="delContentModelFieldModal" title="字段删除" >
		<@ms.modalBody>删除选中字段
			<@ms.modalButton>
				<!--模态框按钮组-->
				<@ms.button  value="确认删除？"  id="deleteContentModelFieldBtn"  />
			</@ms.modalButton>
		</@ms.modalBody>
	</@ms.modal>
</@ms.html5>	

	
<script type="text/javascript">	
$(function(){
		$("#contentModelFieldList").bootstrapTable({
			url:"${managerPath}/mdiy/contentModel/contentModelField/${contentModelId}/list.do",
			contentType : "application/x-www-form-urlencoded",
			queryParamsType : "undefined",
			toolbar: "#toolbar",
	    	columns: [{ checkbox: true},
				    		{
				        	field: 'fieldTipsName',
				        	title: '描述',
				        	align: 'center',
				        	formatter:function(value,row,index) {
				        		return value;
				        	}
				    	}, {
	        				field: 'fieldFieldName',
	       					title: '字段名',
	       					align: 'center',
	        				formatter:function(value,row,index) {
	        				    return "<a style='cursor:pointer;text-decoration:none;' onclick='upContentModelFieldBtn("+row.fieldId+")'>" + value + "</a>";
				   				return value;
							}
	    				}, {
	       				 	field: 'fieldTypeString',
	       					title: '类型',
	       					align: 'center',
	        				formatter:function(value,row,index) {
				   				return value;
							}
	    }, {
	        field: 'fieldIsNull',
	        title: '是否可以为空',
	        align: 'center',
	        formatter:function(value,row,index) {
	        		if(value==0){
	        			value ="否"
	        		}else{
	        			value ="是"
	        		}
				  	return value;
			}
	    }, {
	        field: 'fieldIsSearch',
	        title: '是否支持搜索',
	        align: 'center',
	        formatter:function(value,row,index) {
	        		if(value==0){
	        			value ="否"
	        		}else{
	        			value ="是"
	        		}
				  return value;
			}
	    }, {
	        field: 'fieldDefault',
	        title: '默认值',
	        align: 'center',
	        formatter:function(value,row,index) {
				  return value;
			}
	    }]
	    
	});
	})
//删除按钮
	$("#delContentModelFieldBtn").click(function(){
		//获取checkbox选中的数据
		var rows = $("#contentModelFieldList").bootstrapTable("getSelections");
		//没有选中checkbox
		if(rows.length <= 0){
			<@ms.notify msg="请选择需要删除的记录" type="warning"/>
		}else{
			$(".delContentModelFieldModal").modal();
		}
	})
	
	$("#deleteContentModelFieldBtn").click(function(){
		var rows = $("#contentModelFieldList").bootstrapTable("getSelections");
		$(this).text("正在删除...");
		$(this).attr("disabled","true");
		$.ajax({
			type: "post",
			url: "${managerPath}/mdiy/contentModel/contentModelField/delete.do",
			data: JSON.stringify(rows),
			dataType: "json",
			contentType: "application/json",
			success:function(msg) {
				if(msg.result == true) {
					<@ms.notify msg= "删除成功" type= "success" />
				}else {
					<@ms.notify msg= "删除失败" type= "danger" />
				}
				location.reload();
			}
		})
	});

function editContentModelField(fieldId){
		var URL = base+"${baseManager}/mdiy/contentModel/contentModelField/"+fieldId+"/edit.do";
		$(this).request({url:URL,type:"json",method:"post",func:function(model) {
			if (model) {
				$("#fieldForm").attr("action","${managerPath}/mdiy/contentModel/contentModelField/update.do");
				$("#contentModelField input[name='fieldId']").val(model.fieldId);
				$("#contentModelField input[name='fieldTipsName']").val(model.fieldTipsName);
				$("#contentModelField input[name='fieldFieldName']").val(model.fieldFieldName);
				$("#contentModelField select[name='fieldType']").val(model.fieldType);
				
				$("#contentModelField input[name='fieldFieldName']").attr("readonly","readonly");
				$("#contentModelField input[name='fieldLength']").val(model.fieldLength);
				$("#contentModelField input[name='fieldIsNull']").each(function(o) {
					if ($(this).val()==model.fieldIsNull) {
						$(this).attr("checked",true);
					}
				});
				$("#contentModelField input[name='fieldIsSearch']").each(function(o) {
					if ($(this).val()==model.fieldIsSearch) {
						$(this).attr("checked",true);
					}
				});				
				
				$("#contentModelField textarea[name='fieldDefault']").val(model.fieldDefault);
				$("#contentModelField").modal();
			}
		}});
}
	//添加字段
	$("#addContentModelFieldBtn").click(function(){
		$("input[name='cmTableName']").attr("disabled",false);
		postUrl = "${managerPath}/mdiy/contentModel/contentModelField/save.do";
		$("#addEditBtn").text("保存");
		$("#addEditModelTitle").text("添加字段");
		$("#fieldForm")[0].reset();
		$(".contentModelField").modal();
	});
	function upContentModelFieldBtn(fieldId){
	    editContentModelField(fieldId);
		$(".contentModelField").modal();
	}

function resetForm() {
		$("#contentModelField input[name='fieldId']").val(0);
		$("#contentModelField input[name='fieldFieldName']").removeAttr("readonly");
}

function editContentModel(cmId){
	location.href= base+"${baseManager}/mdiy/contentModel/"+cmId+"/edit.do";
}
</script>
