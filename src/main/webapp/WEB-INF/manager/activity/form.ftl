<@ms.html5>
	<@ms.nav title="添加工作流" back=true>
		<@ms.saveButton id="saveUpdate" value="保存"/>
		<@ms.updateButton id="myUpdate" value="更新"/>
	</@ms.nav>
	<@ms.panel>
		<@ms.form name="columnForm" isvalidation=true  action="" method="post" >
			<#if column.categoryId!=0>.
				<@ms.hidden name="categoryId" value="${column.categoryId?c?default(0)}" />
			</#if>
			<@ms.text name="categoryTitle" width="600" label="工作流名称" title="工作流名称" placeholder="工作流名称" value="${column.categoryTitle?default('')}" id="name" validation={"data-bv-stringlength":"true","required":"true", "data-bv-notempty-message":"必填项目", "data-bv-regexp":"true","data-bv-regexp-regexp":'^[^[!@#$%^&*()_+-/~?！@#￥%…&*（）——+—？》《：“‘’]+$',"data-bv-stringlength-max":"50","data-bv-regexp-message":"工作流名称不能包含特殊字符","data-bv-stringLength-message":"长度不能超过50个字符"} />
	  		
	  		<@ms.formRow label="所属栏目" width="300">
				<@ms.treeInput treeId="inputTree"  json="${listColumn?default('')}"  jsonId="categoryId" jsonPid="categoryCategoryId" jsonName="categoryTitle" inputName="categoryCategoryId" inputValue="${column.categoryCategoryId?c?default(0)}" addNodesName="顶级栏目管理"  buttonText="${columnSuper.categoryTitle?default('顶级栏目管理')}" clickZtreeId="clickZtreeId(event,treeId,treeNode);"  expandAll="true" showIcon="true"/>
			</@ms.formRow>
	  		<@ms.textarea name="columnDescrip" width="600" label="描述" wrap="Soft" rows="4" placeholder=""  value="${column.columnDescrip?default('')}" id="description" validation={"data-bv-stringlength":"true", "data-bv-stringlength-max":"200","data-bv-stringLength-message":"长度不能超过200个字符"} />
			<@ms.text name="categorySort"  width="200"  label="顺序" title="顺序" size="5"  placeholder="请输入文章顺序" id="sort" value="${column.categorySort?c?default(0)}"  validation={"data-bv-between":"true","data-bv-between-message":"自定义顺序必须大于0","data-bv-between-min":"0", "data-bv-between-max":"99999999","data-bv-notempty-message":"自定义顺序不能为空","data-bv-between-message":"请输入0-99999999之间的数","required":"true", "data-bv-notempty-message":"必填项目"}/>
			<@ms.formRow label="工作流步骤" width="400">
				<select style="width: 180px;height: 25px;" id="select_role">
					<option value="1">一次审核</option>
					<option value="2">二次审核</option>
					<option value="3">三次审核</option>
				</select>
				<label style="margin-left: 10px;color: #ccc;"><span style="color: red;">*</span>设置审核次数</label>
			</@ms.formRow>
			<div style="margin-left: 198px;background-color: #f7f9f9f8;padding: 15px; width: 800px;">
				<table class="activity_table"  style="background-color: #ff000000; margin-bottom: 0px;">
					<thead>
						<tr style="height: 35px;">
							<td>步骤</td>
							<td>审核管理员</td>
						</tr>
					</thead>
					<tbody>
						<tr id="instance_1"  style="height: 35px;" class="tbody_1"> 
							<td>第一步</td> 
							<td> 
								<div class="select" id="roleselect_1" style="width: 394px; height: 28px; border: 1px solid #DCDFE6; background-color: #ffffff; overflow: hidden; padding: 3px 14px; cursor: pointer;"><p style="display: inline-block; width: 300px; margin: 0; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;"></p><span style="float: right; margin-top: 3px 0;">﹀</span></div>
							</td>
						</tr>
						<tr id="instance_2"  style="height: 35px; display:none;" class="tbody_1"> 
							<td>第二步</td> 
							<td> 
								<div class="select" id="roleselect_2" style="width: 394px; height: 28px; border: 1px solid #DCDFE6; background-color: #ffffff; overflow: hidden; padding: 3px 14px; cursor: pointer;"><p style="display: inline-block; width: 300px; margin: 0; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;"></p><span style="float: right; margin-top: 3px 0;">﹀</span></div>
							</td>
						</tr>
						<tr id="instance_3"  style="height: 35px; display:none;" class="tbody_1"> 
							<td>第三步</td> 
							<td> 
								<div class="select" id="roleselect_3" style="width: 394px; height: 28px; border: 1px solid #DCDFE6; background-color: #ffffff; overflow: hidden; padding: 3px 14px; cursor: pointer;"><p style="display: inline-block; width: 300px; margin: 0; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;"></p><span style="float: right; margin-top: 3px 0;">﹀</span></div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</@ms.form>
	</@ms.panel>
	<div class="mask" style="background-color: rgba(0,0,0,0.5); width: 100%; height: 100%; position: fixed; left: 0; top: 0; display: none; z-index: 1001">
		<div style="width: 413px; height: 498px; background-color: #fff; margin: 25px auto; padding: 17px 21px;">
			<p style="overflow: hidden; margin: 0 0 28px 0;"><span>选择审核管理员</span><img class="close" style="width: 10px; height: 10px; float: right; margin-top: 5px; cursor: pointer; opacity: 1;" src="../../../../static/skin/manager/4.6.4/images/close.png"/></p>
			<div id="inputBox" style="width: 370px; height: 373px; background-color: #F5F7FA; overflow-y: scroll;">
			</div>
			<p style="text-align: right; margin: 20px 0 0 0;"><button class="cancel" style="width: 71px; height: 28px; border: 1px solid #666; background-color: #ffffff; margin-right: 11px;">取消</button><button class="sure" style="width: 71px; height: 28px; border: 0; color: #ffffff; background-color: #00AEFF;">选择</button></p>
		</div>
	</div>
	
</@ms.html5>	
<script>
$(function(){
	var param = window.location.search; // 获取参数用来区分是添加还是更新
	var select1 = [], select2 = [],  select3 = []; // 分别存放三个选中的管理员id
	var selectName1 = [], selectName2 = [], selectName3 = []; // 分别存放三个选中的管理员名字
	var id = ''; // 点击选择审核人员时存放点击的id
	var $name = $('#name'); // 工作流名称控件
	var $column = $('#treeLabelinputTree'); // 所属栏目控件
	var $columnId = $('input[name="categoryCategoryId"]');
	var $description = $('#description'); // 描述控件
	var $sort = $('#sort'); // 排序控件
	var $select = $('#select_role'); // 工作流步骤控件
	var $allDiv = $('.select'); // 所有步骤的div
	if(param.indexOf('id') > 0 ? true : false){
		//param = param.substring(param.indexOf('=') + 1, param.indexOf('&'));
		param = param.substring(param.indexOf('=') + 1);
		$('#saveUpdate').css('display', 'none');
		$('#myUpdate').attr('id', '');
		$.ajax({
			url: '${managerPath}/activity/show.do',
			type: 'post',
			contentType: "application/json",
			dataType: 'json',
			data: JSON.stringify({
				'id': param
			}),
			success: function(data){
				console.log(data);
				$name.val(data.name);
				$column.text(data.columnName);
				$columnId.val(data.columnId);
				$('#treeDomeinputTree').find('a>span:not(:first-of-type)').each(function(index, el){
					if($(el).text() == data.columnName) $(el).parent().addClass('curSelectedNode');
					else $(el).parent().removeClass('curSelectedNode');
				})
				$description.val(data.description);
				$sort.val(data.sort);
				$select.children('option').each(function(index, el){
					if($(el).attr('value') == data.steps){
						$(el).attr('selected', 'selected');
						optionsShow();
					}
				});
				for(var i = 0; i < data.member.length; i++){
					$allDiv.each(function(index, el){
						if(index == i){
							switch(i){
								case 0:
								select1 = JSON.parse(data.member[i].member);
								$(el).text(JSON.parse(data.member[i].memberName).join(','));
								selectName1 = JSON.parse(data.member[i].memberName);
								break;
								case 1:
								select2 = JSON.parse(data.member[i].member);
								$(el).text(JSON.parse(data.member[i].memberName).join(','));
								selectName2 = JSON.parse(data.member[i].memberName);
								break;
								case 2:
								select3 = JSON.parse(data.member[i].member);
								$(el).text(JSON.parse(data.member[i].memberName).join(','));
								selectName3 = JSON.parse(data.member[i].memberName);
								break;
							}
						}
					})
				}
			},
			error: function(e){
				console.log(e)
			}
		});
	}
	// 选择审核步骤
	$select.change(optionsShow);
	$column.click(function(){
		var self = $(this);
		if(!self.parent().hasClass('open')){
			var inputValue = $columnId.val();
			var buttonValue = self.text();
			if(inputValue != '0'){
				var nodeParam = zTreeObjinputTree.getNodesByParam("categoryId", inputValue, null);
				var allNodeParam = zTreeObjinputTree.getNodesByParam("categoryTitle", buttonValue, null);
				var allA = $('a[title="' + buttonValue + '"]');
				var checkIndex = 0;
				for(var i = 0; i < allNodeParam.length; i++){
					if(nodeParam[0].categoryId == allNodeParam[i].categoryId){
						checkIndex = i;
					}
				}
				allA.each(function(index, el){
					if(index != checkIndex){
						$(el).removeClass('curSelectedNode');
					}
				});
			}
		}
	})
	function optionsShow(){
		var optionNum = $select.val();
		$('.tbody_1').each(function(index, el){
			if(index < optionNum)
				$(el).css('display', '');
			else
				$(el).css('display', 'none');
		});
	}
	// 选择每步的审核人员
	$('.select').each(function(index, el){
		$(el).click(function(){
			id = $(this).attr('id');
			$('.mask').css('display', '');
			getManagerList(id);
		});
	});
	// 取消
	$('.cancel, .close').click(function(){
		$('.mask').css('display', 'none');
	});
	// 保存
	$('.sure').click(function(){
		var arr = [];
		var str = '';
		var selectName = [];
		$('input[name="role"]').each(function(index, el){
			if($(el).is(':checked')){
				arr.push(Number($(el).attr('id')));
				selectName.push($(el).next().text());
				str += $(el).next().text() + ',';
			}
		});
		str = str.substring(0, str.length - 1);
		switch(id){
			case 'roleselect_1':
				select1 = [];
				select1 = select1.concat(arr);
				selectName1 = [];
				selectName1 = selectName1.concat(selectName);
				$('#' + id).text(str);
			break;
			case 'roleselect_2':
				select2 = [];
				select2 = select2.concat(arr);
				selectName2 = [];
				selectName2 = selectName2.concat(selectName);
				$('#' + id).text(str);
			break;
			case 'roleselect_3':
				select3 = [];
				select3 = select3.concat(arr);
				selectName3 = [];
				selectName3 = selectName3.concat(selectName);
				$('#' + id).text(str);
			break;
		}
		$('.mask').css('display','none');
	})
	//获取管理员列表
	function getManagerList(id){
		$.ajax({
			type: "post",
			url: "${managerPath}/basic/manager/queryAll.do",
			dataType: "json",
			contentType: "application/json",
			success:function(data) {
				var $tr =  $('.activity_table tbody tr');
				var trShowNum = 0;
				$tr.each(function(index, el){
					if($(el).css('display') != 'none'){
						trShowNum++;
					}
				});
				var $div = $('#inputBox');
				$div.html('');
				for(var j = 0; j < data.length; j++){
					var value = data[j].managerNickName;
					var roleId = data[j].managerId;
					var $p = '';
					switch(id){
						case 'roleselect_1':
							if(select1.indexOf(roleId) >= 0 ? true : false)
								$p = $('<p style="margin: 14px 0 0 71px; line-height: 1;"><input id="' + roleId + '" type="checkbox" checked name="role" style="vertical-align: -2px; margin: 0;"/><label for="' + roleId + '" style="margin: 0 0 0 7px;">' + value + '</label></p>');
							else
								$p = $('<p style="margin: 14px 0 0 71px; line-height: 1;"><input id="' + roleId + '" type="checkbox" name="role" style="vertical-align: -2px; margin: 0;"/><label for="' + roleId + '" style="margin: 0 0 0 7px;">' + value + '</label></p>');
						break;
						case 'roleselect_2':
							if(select2.indexOf(roleId) >= 0 ? true : false)
								$p = $('<p style="margin: 14px 0 0 71px; line-height: 1;"><input id="' + roleId + '" type="checkbox" checked name="role" style="vertical-align: -2px; margin: 0;"/><label for="' + roleId + '" style="margin: 0 0 0 7px;">' + value + '</label></p>');
							else
								$p = $('<p style="margin: 14px 0 0 71px; line-height: 1;"><input id="' + roleId + '" type="checkbox" name="role" style="vertical-align: -2px; margin: 0;"/><label for="' + roleId + '" style="margin: 0 0 0 7px;">' + value + '</label></p>');
						break;
						case 'roleselect_3':
							if(select3.indexOf(roleId) >= 0 ? true : false)
								$p = $('<p style="margin: 14px 0 0 71px; line-height: 1;"><input id="' + roleId + '" type="checkbox" checked name="role" style="vertical-align: -2px; margin: 0;"/><label for="' + roleId + '" style="margin: 0 0 0 7px;">' + value + '</label></p>');
							else
								$p = $('<p style="margin: 14px 0 0 71px; line-height: 1;"><input id="' + roleId + '" type="checkbox" name="role" style="vertical-align: -2px; margin: 0;"/><label for="' + roleId + '" style="margin: 0 0 0 7px;">' + value + '</label></p>');
						break;
					}
					$div.append($p);
				}
			}
		});
	}
	//栏目保存提交事件
	$("#saveUpdate").click(function(){
		$("#columnForm").data("bootstrapValidator").validate();
		var isValid = $("#columnForm").data("bootstrapValidator").isValid();
		if(!isValid) {
			<@ms.notify msg= "数据提交失败，请检查数据格式！" type= "warning" />
			return;
		}
		if(isNaN($("input[name=categorySort]").val())){
			<@ms.notify msg= "自定义排序必须是数字" type= "warning" />
			$("input[name=categorySort]").val(0);
			return;
		}
		var arr = [];
		var name = $name.val(); // 获取工作流名称
		var description = $description.val(); // 获取描述
		var sort = $sort.val(); // 获取排序
		var options = $select.val(); // 获取步骤
		var arrName = [];
		if(options == 1){
			arr.push(select1);
			arrName.push(selectName1);
		}else if(options == 2){
			if(select2.length == 0){
				<@ms.notify msg= "审核人员不能为空" type= "warning" />
				return;
			}
			arr.push(select1, select2);
			arrName.push(selectName1, selectName2);
		}else{
			if(select2.length == 0 || select3.length == 0){
				<@ms.notify msg= "审核人员不能为空" type= "warning" />
				return;
			}
			arr.push(select1, select2, select3);
			arrName.push(selectName1, selectName2, selectName3);
		}
		var steps = options;
		var columnId = $columnId.val();
		var columnName = $column.text();
		var dataObj = {"name":name, "description":description, "sort":sort, "steps":steps, "columnId":columnId,"members":arr, 'columnName': columnName, 'memberName': arrName};
		$.ajax({
		   	type: "post",
		   	url: "${managerPath}/activity/save.do",
		   	data: JSON.stringify(dataObj),
		   	dataType:"json",
		   	contentType: "application/json",
		   	beforeSend:function(){
		   		//获取按钮值
				var bottonText = $("#saveUpdate").text().trim();
				//设置按钮加载状态值
				$("#saveUpdate").attr("data-loading-text",bottonText+"中");
				//执行加载状态
				$("#saveUpdate").button('loading');
		   	},
		   	success: function(msg){
		    	if (msg) {
	     			<#if column.categoryId==0>
	     				<@ms.notify msg= "保存成功" type= "success" />
	     			<#else>
	     				<@ms.notify msg= "更新成功" type= "success" />	
	     			</#if>
	     			var ss = "${managerPath}/activity/index.do";
	     			location.href= ss;
	    		}else{
	    			<#if column.categoryId==0>
	     				<@ms.notify msg= "保存失败" type= "warning" />
	     			<#else>
	     				<@ms.notify msg= "更新失败" type= "warning" />
	     			</#if>
	    		}
	    		$("#saveUpdate").button('reset');
		   	}
		});
	});
	// 更新
	$('html body>div>div>div.ms-content-body-title>button[title="更新"]').click(function(){
		$("#columnForm").data("bootstrapValidator").validate();
		var isValid = $("#columnForm").data("bootstrapValidator").isValid();
		if(!isValid) {
			<@ms.notify msg= "数据提交失败，请检查数据格式！" type= "warning" />
			return;
		}
		if(isNaN($("input[name=categorySort]").val())){
			<@ms.notify msg= "自定义排序必须是数字" type= "warning" />
			$("input[name=categorySort]").val(0);
			return;
		}
		var arr = [];
		var name = $name.val(); // 获取工作流名称
		var description = $description.val(); // 获取描述
		var sort = $sort.val(); // 获取排序
		var options = $select.val(); // 获取步骤
		var arrName = [];
		if(options == 1){
			arr.push(select1);
			arrName.push(selectName1);
		}else if(options == 2){
			arr.push(select1, select2);
			arrName.push(selectName1, selectName2);
		}else{
			arr.push(select1, select2, select3);
			arrName.push(selectName1, selectName2, selectName3);
		}
		var steps = options;
		var columnId = $columnId.val();
		var columnName = $column.text();
		var dataObj = {'id': Number(param),"name":name, "description":description, "sort":sort, "steps":steps, "columnId":columnId,"members":arr, 'columnName': columnName, 'memberName': arrName};
		$.ajax({
		   	type: "post",
		   	url: "${managerPath}/activity/edit.do",
		   	data: JSON.stringify(dataObj),
		   	
		   	contentType: "application/json",
		   	beforeSend:function(){
		   		//获取按钮值
				var bottonText = $("#saveUpdate").text().trim();
				//设置按钮加载状态值
				$("#saveUpdate").attr("data-loading-text",bottonText+"中");
				//执行加载状态
				$("#saveUpdate").button('loading');
		   	},
		   	success: function(msg){
		    	if (msg) {
	     			var ss = "${managerPath}/activity/index.do";
	     			location.href= ss;
	    		}
	    		$("#saveUpdate").button('reset');
		   	},
			error: function(e){
			}	
		});
	});
});
//选择栏目后查询自定义模型
function clickZtreeId(event,treeId,treeNode){
	//栏目不能选择自己及其子栏目为父栏目的事件
	<#if column.categoryId gt 0 >
		var booleanClick=true;
		var nodeParam = zTreeObjinputTree.getNodesByParam("categoryId", "${column.categoryId?c?default(0)}", null);
		var nodes = zTreeObjinputTree.getNodesByParam("categoryId", treeNode.categoryId, nodeParam[0]);
		if(nodes.length>0 || treeNode.categoryId == nodeParam[0].categoryId){
			booleanClick=false;
			<@ms.notify msg= "不能选择该栏目作为父栏目" type= "warning" />
		}
		return booleanClick;
	</#if>
};
</script>
