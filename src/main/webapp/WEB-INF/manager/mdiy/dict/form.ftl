<@ms.html5>
	 <@ms.nav title="字典表编辑" back=true>
	 	<#if dictEntity.dictId ??>
	 		<@ms.updateButton onclick="save()" value="更新"/>
	 	<#else>
	 		<@ms.saveButton onclick="save()" value="保存"/>
	 	</#if>
    </@ms.nav>
    <@ms.panel>
    	<@ms.form name="dictForm" isvalidation=true>
    		<@ms.hidden name="dictId" value="${dictEntity.dictId?default('')}"/>
    			<@ms.text label="类型" name="dictType" value="${dictEntity.dictType?default('')}"  width="360px;" placeholder="请输入类型" validation={"required":"false","maxlength":"50","data-bv-stringlength-message":"类型长度不能超过五十个字符长度!", "data-bv-notempty-message":"必填项目"}  help="注：类型与标签名组合为同一个查询条件（唯一索引）"/>
    			<@ms.text label="标签名" name="dictLabel" value="${dictEntity.dictLabel?default('')}"  width="360px;" placeholder="请输入标签名" validation={"required":"false","maxlength":"60","data-bv-stringlength-message":"标签名长度不能超过六十个字符长度!", "data-bv-notempty-message":"必填项目"} help="注：参数建"/>
    			<@ms.text label="数据值" name="dictValue" value="${dictEntity.dictValue?default('')}"  width="360px;" placeholder="请输入数据值" validation={"required":"false","maxlength":"50","data-bv-stringlength-message":"数据值长度不能超过五十个字符长度!", "data-bv-notempty-message":"必填项目"}/>
    			<@ms.number label="排序" name="dictSort" value="${dictEntity.dictSort?default('')}" width="360px;" placeholder="请输入排序（升序）" validation={"required":"false","maxlength":"50","data-bv-stringlength-message":"排序（升序）长度不能超过五十个字符长度!", "data-bv-notempty-message":"必填项目"}/>
    			<@ms.textarea label="描述" name="dictDescription" value="${dictEntity.dictDescription?default('')}"  width="360px;" placeholder="请输入描述" validation={"maxlength":"50","data-bv-stringlength-message":"描述长度不能超过五十个字符长度!"}/>
    			<@ms.textarea label="备注信息" name="dictRemarks" value="${dictEntity.dictRemarks?default('')}"  width="360px;" placeholder="请输入备注信息" validation={"maxlength":"50","data-bv-stringlength-message":"备注信息长度不能超过五十个字符长度!"}/>
    	</@ms.form>
    </@ms.panel>
</@ms.html5>
<script>
	var url = "${managerPath}/mdiy/dict/save.do";
	if($("input[name = 'dictId']").val() > 0){
		url = "${managerPath}/mdiy/dict/update.do";
		$(".btn-success").text("更新");
	}
	//编辑按钮onclick
	function save() {
		$("#dictForm").data("bootstrapValidator").validate();
			var isValid = $("#dictForm").data("bootstrapValidator").isValid();
			if(!isValid) {
				<@ms.notify msg= "数据提交失败，请检查数据格式！" type= "warning" />
				return;
		}
		var btnWord =$(".btn-success").text();
		$(".btn-success").text(btnWord+"中...");
		$(".btn-success").prop("disabled",true);
		$.ajax({
			type:"post",
			dataType:"json",
			data:$("form[name = 'dictForm']").serialize(),
			url:url,
			success: function(status) {
				if(status.result) { 
					<@ms.notify msg="保存或更新成功" type= "success" />
					location.href = "${managerPath}/mdiy/dict/index.do";
				}
				else{
					$('.ms-notifications').offset({top:43}).notify({
			    		    type:'danger',
						    message: { text:status.resultMsg }
						 }).show();	
					$(".btn-success").removeAttr("disabled");
					$(".btn-success").text(btnWord);
				}
			}
		})
	}	
</script>
