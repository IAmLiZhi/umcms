<@ms.html5>
	<@ms.nav title="角色设置" back=true>
		<@ms.saveButton id="save"/>
	</@ms.nav>
	<@ms.panel>
		<@ms.form name="columnForm" isvalidation=true  >                                                   
			<@ms.text name="roleName" label="角色名称:" title="角色名称" value="${roleEntity.roleName?default('')}" width="300" validation={"required":"true","maxlength":"30","data-bv-notempty-message":"请填写角色名称"}/>
			<@ms.formRow label="权限管理:">
				<div id="myTree">
					<#--  <@ms.tree id="modelListTree" type="checkbox" url="${managerPath}/model/query.do" idKey="modelId" pIdKey="modelModelId" text="modelTitle"/>  -->
					<div>
						<ul id="treeDemo1" class="ztree"></ul>
					</div>
					<button class="set" style="">设置内容管理权限</button>
				</div>
			</@ms.formRow>
		</@ms.form>
	</@ms.panel>
	<div class="mask" style="position: fixed; left: 0; top: 0; z-index: 1000; height: 100%; width: 100%; background-color: rgba(0,0,0,0.5); display: none;">
		<div class="alert" style="width: 413px; height: 498px; background-color: #ffffff; padding: 17px 21px; margin: 33px auto 0;">
			<p style="overflow: hidden; font-size: 14px; color: #333333;">设置内容管理权限<img class="close" style="width: 10px; height: 10px; float: right; margin-top: 5px; cursor: pointer;  opacity: 1;" src="../../../../static/skin/manager/4.6.4/images/close.png"/></p>
			<!-- 树形模块菜单开始 -->
			<#--  <#if listColumn?has_content>
				<@ms.tree type="checkbox" treeId="inputTree" json="${listColumn?default('')}" addNodesName="全部" jsonId="categoryId" jsonPid="categoryCategoryId" jsonName="categoryTitle" showIcon="true" expandAll="true" />
			<#else> 
				<@ms.nodata content="暂无栏目"/>
			</#if>  -->
			<div style="height: 390px; overflow-y: scroll;">
				<ul id="treeDemo" class="ztree"></ul>
			</div>
			<!-- 树形模块菜单结束 -->
			<p style="text-align: right; margin-top: 20px;"><button class="cancel" style="background-color: #ffffff; border: 1px solid #666666; color: #666666; font-size: 14px; width: 71px; height: 28px; margin-right: 11px;">取消</button><button class="choose" style="width: 71px; height: 28px; background-color: #00AEFF; border: 0; font-size: 14px; color: #ffffff;">选择</button></p>
		</div>
	</div>
</@ms.html5>

<script>
	$(function(){
		var $mask = $('.mask');
		var zNodes = [];
		var checkedId = [];
		var checkedId1 = [];
		var setting = {
			check: {
				enable: true
			},
			data: {
				key:{
					name: "categoryTitle" //节点显示的值
				},
				simpleData: {
					enable: true,
					idKey: "categoryId",//节点的id
					pIdKey: "categoryCategoryId",//节点的父节点id
					rootPId: null,
				}
			},
			callback: {
				onCheck: onCheck
			}
		};
		(function(){
			var setting1 = {
				check: {
					enable: true
				},
				data: {
					key: {
						name: 'modelTitle'
					},
					simpleData: {
						enable: true,
						idKey: 'modelId',
						pIdKey: 'modelModelId',
						rootPId: null,
					}
				},
				callback: {
					onCheck: onCheck1
				}
			}
			function onCheck1(e,treeId,treeNode){
				var treeObj = $.fn.zTree.getZTreeObj("treeDemo1"),
				nodes = treeObj.getCheckedNodes(true);
				checkedId1 = [];
				for(var i=0;i<nodes.length;i++){
					checkedId1.push(nodes[i].modelId);
				}
			}
			$.post('${managerPath}/model/query.do',
				function(data){
					var treeObj1 = $.fn.zTree.init($("#treeDemo1"), setting1, data);
					treeObj1.expandAll(true);
					ms.post("${managerPath}/basic/role/${roleEntity.roleId?default('')}/queryByRole.do",null,function(models){
						var tempModels = []; // 用来存放已选择的权限的id
						var treeObj2 = $.fn.zTree.getZTreeObj("treeDemo1");
						if (models != null) {
							for (var i=0;i<models.length;i++) {
								tempModels.push(models[i].modelId);
							}
							checkedId1 = tempModels;
							var nodes = treeObj2.getNodes();
							treeObj2.checkAllNodes(false);
							for (var i = 0; i < nodes.length;i++) {
								var children = nodes[i].children;
								if (children != undefined && children.length>0) {
									for(var j = 0; j < children.length; j++){
										var childrenSon = children[j].children;
										if(childrenSon != undefined && childrenSon.length > 0){
											for(var k = 0; k < childrenSon.length; k++){
												if(jQuery.inArray(childrenSon[k].modelId, tempModels) > -1) {
													treeObj2.checkNode(childrenSon[k], true, true);
												}
											}
										}else{
											if(jQuery.inArray(children[j].modelId, tempModels) > -1) {
												treeObj2.checkNode(children[j], true, true);
											}
										}
									}
								} else {
									if(jQuery.inArray(nodes[i].modelId, tempModels) > -1) {
										treeObj2.checkNode(nodes[i], true, true);
									}							
								}
							}
						}
					});  
				}
			);
		})();
		// 设置内容管理权限
		$('.set').click(function(){
			$.post('${managerPath}/column/list.do', 
				function(data){
					zNodes = data.rows;
					$mask.css('display', '');
					$.fn.zTree.init($("#treeDemo"), setting, zNodes);
					ms.post("${managerPath}/basic/role/${roleId?default('')}/queryColumnByRole.do",null,function(models){
						var tempModels = []; // 用来存放已选择的权限的id
						var treeObj1 = $.fn.zTree.getZTreeObj("treeDemo");
						tempModels = models;
						checkedId = tempModels;
						if (models != null) {
							var nodes = treeObj1.getNodes();
							treeObj1.checkAllNodes(false);
							for (i = 0; i < nodes.length;i++) {
								var children = nodes[i].children;
								if (children != undefined && children.length>0) {
									for(var j = 0; j < children.length; j++){
										if(jQuery.inArray(children[j].categoryId, tempModels) > -1) {
											treeObj1.checkNode(children[j], true, true);
										}
									}
								} else {
									if(jQuery.inArray(nodes[i].categoryId, tempModels) > -1) {
										treeObj1.checkNode(nodes[i], true, true);
									}							
								}
							}
						}
					});  
				}
			);
		});
		
		// 取消，关闭
		$('.close, .cancel,.choose').click(function(){
			$mask.css('display', 'none');
		});

		//选择
		$('.choose').click(function(){
			console.log(checkedId);
		});
		function onCheck(e,treeId,treeNode){
            var treeObj = $.fn.zTree.getZTreeObj("treeDemo"),
            nodes = treeObj.getCheckedNodes(true),
            v = "";
			checkedId = [];
            for(var i=0;i<nodes.length;i++){
				checkedId.push(nodes[i].categoryId);
            }
		}

		
		//保存操作
		$("#save").click(function(){
			var roleName = $("input[name=roleName]").val();
			var roleId = "${roleEntity.roleId?default('')}";
			var oldRoleName = "${roleEntity.roleName?default('')}";
			var ids = checkedId1;
			var columnIds = checkedId;
			if(roleName == "" || roleName == null){
				<@ms.notify msg= '角色名不能为空' type= "warning" />
			}else{
				$.ajax({
					type:"post",
					url:"${managerPath}/basic/role/saveOrUpdateRole.do",
					dataType: "json",
					data:{ids:ids,columnIds:columnIds,roleName:roleName,roleId:roleId,oldRoleName:oldRoleName},
					success:function(data){
						if(data.result == false) {
							$('.ms-notifications').offset({top:43}).notify({
								type:'warning',
								message: { text:data.resultMsg }
							}).show();	
						}else {
							<@ms.notify msg= "操作成功" type= "success" />
							window.history.back();
						}
					}
				});
			}
		});
		
		//数据初始化
		
		<#--  
		//树形结点
		function getZtreeId(event,treeId,treeNode){
			if (treeNode.columnType==1) {
				//父级栏目为true，子级栏目为false
				var isParent = false;
				if(treeNode.isParent == true){
					isParent = true;
				}
				$("#listFrame").attr("src","${managerPath}/cms/article/"+treeNode.categoryId+"/main.do?isParent="+isParent+"&categoryTitle="+encodeURIComponent(treeNode.categoryTitle));
			} else if(treeNode.columnType==2){
				//判断该单篇栏目是否存在文章
				$.ajax({ 
					type: "POST", 
					url: base+"${baseManager}/cms/article/"+treeNode.categoryId+"/queryColumnArticle.do",
					dataType:"json",
					success: function(msg){
						if (msg.result) {
							$("#listFrame").attr("src","${managerPath}/cms/article/add.do?categoryId="+treeNode.categoryId+"&categoryTitle="+encodeURIComponent(treeNode.categoryTitle));
						} else {
							//如果该单篇栏目下存在文章则跳转到文章编辑页
							$("#listFrame").attr("src","${managerPath}/cms/article/"+treeNode.categoryId+"/edit.do?categoryId="+treeNode.categoryId+"&categoryTitle="+encodeURIComponent(treeNode.categoryTitle));
						}
					},
				});
			} else if(treeNode.columnType=="" || treeNode.columnType == undefined){
				$("#listFrame").attr("src","${managerPath}/cms/article/0/main.do"); 
			}
			
		}  -->
	});
</script>