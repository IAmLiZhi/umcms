<@ms.html5>
	<@ms.nav title="改进作风狠抓落实监督信箱"></@ms.nav>
	<@ms.searchForm name="searchForm" action="">
		<#if stateList?has_content>
			<@ms.select label="状态" default="全部" value="0" name="state" id="state" list=stateList listValue="stateName" listKey="stateValue"/>
		<#else>
			<@ms.select label="状态" default="全部" value="0" name="state" id="state" list=[]/>
		</#if>
		<@ms.text label="举报内容"  name="publishContent" value="" title="请输入投诉内容"  placeholder="请输入投诉内容" />
		<@ms.searchFormButton>
			<@ms.queryButton id="submitSearch" />
		</@ms.searchFormButton>
	</@ms.searchForm>
	<div id="toolbar">
		<@ms.panelNavBtnGroup>
			<@shiro.hasPermission name="article:del"><@ms.panelNavBtnDel title=""/></@shiro.hasPermission> 
			<div id="toolbarts" class="select" style="display: inline-block; margin-left: 10px;">
			  <select class="form-control">
			    <option value="">导出本页</option>
			    <option value="selected">导出选中</option>
			  </select>
			</div>
		</@ms.panelNavBtnGroup>
	</div>
	<@ms.panel>
		<table id="columnListts" 
			data-show-refresh="true"
			data-show-columns="true"
			data-show-export="true"
			data-method="post" 
			data-side-pagination="server">
		</table>
	</@ms.panel>
	<div class="mask" style="position: fixed; display: none; z-index:1001; left: 0; top: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.5);">
		<div class="alert" style="background-color: #Ffffff; width: 569px; height: 427px; margin: 70px auto 0;">
			<p style="overflow: hidden;">回复<img class="myclose" style="width: 10px; height: 10px; float: right; margin-top: 5px; cursor: pointer; opacity: 1;" src="../../../../static/skin/manager/4.6.4/images/close.png"/></p>
			<p style="margin-top: 10px;">请输入回复内容：</p>
			<textarea name="" id="content" style="width: 537px; height: 236px; padding: 10px;"></textarea>
			<p style="overflow: hidden;"><span class="prompt" style="display: none; color: #ff5959;">请输入内容</span><span style="float: right">最大字数限制1024字。</span></p>
						<p>是否公开：<input type="radio" id="public" name="public" value="0"><label for="public">公开</label><input type="radio" id="nopublic" name="public" value="1"><label for="nopublic">不公开</label></p>
			<p style="text-align: right; margin-top: 14px;"><button class="cancel" style="background-color: #ffffff; margin-right: 10px; border: 1px solid #666666; font-size: 14px; color: #666666; width: 71px; height: 28px;">取消</button><button class="sure" style="background-color: #00AEFF; border: 0; font-size: 14px; color: #ffffff; width: 71px; height: 28px;">确定</button></p>
		</div>
	</div>
</@ms.html5>
<@ms.modal modalName="delCategory" title="删除内容">
	  <@ms.modalBody>
	  		确定要删除所选的内容吗?
     </@ms.modalBody>
	 <@ms.modalButton>
 		<@ms.button class="btn btn-danger rightDelete delete mydelete" value="确定"/>
 	</@ms.modalButton>
</@ms.modal>
<script>
$(function(){
	var id = 0;
	var state = 0;
	$('#public').prop('checked', true);
	$('[name="public"]').change(function(){
		state = $(this).val();
	});
	$('.myclose').click(function(){
		$('.mask').css('display', 'none');
	});
	$('.cancel').click(function(){
		$('.mask').css('display', 'none');
	})
	$('textarea').blur(function(){
		var length = $(this).val().trim().length;
		if(length > 1024){
			$('.prompt').css('display','').text('最多可以输入1024字，现在有' + length + '字');
		}else if(length == 0){
			$('.prompt').css('display', '').text('请输入回复的内容');
		}else{
			$('.prompt').css('display', 'none');
		}
	});
	$('.sure').click(function(){
		var value = $('textarea').val().trim();
		if(value.lenght == 0){
			$('.prompt').css('display', '').text('请输入回复内容')
			return;
		}else if(value.lenght > 1024){
			$('.prompt').css('display', '').text('回复内容不得超过1024字')
			return;
		}
		$.ajax({
			type: 'post',
			url: '${managerPath}/hd/reply.do',
			contentType: "application/json;charset=UTF-8",
			dataType: 'json',
			data: JSON.stringify({
				'publishId': id,
				'replyContent': value,
				isOpen: state
			}),
			success: function(data){
				if(data){
					$('.mask').css('display', 'none');
					var ss = "${managerPath}/hd/tsjb/index.do";
					location.href= ss;
				}
			},
			error: function(e){
				console.log(e);
			}
		})
	})
	
	var search = $("form[name='searchForm']").serializeJSON();
	var state = search.state;
	$('#toolbarts').find('select').change(function () {
		$("#columnListts").bootstrapTable('destroy').bootstrapTable({
			url:"${managerPath}/hd/5/list.do"+"?stateStr="+state,
			contentType : "application/x-www-form-urlencoded",
			queryParamsType : "undefined",
			toolbar: "#toolbar",
			showExport: true,
			exportDataType: $(this).val(),
			Icons:'glyphicon-export icon-share',
            exportTypes: ['json', 'xml', 'csv', 'txt', 'sql', 'excel', 'pdf'],
            exportOptions: {
                fileName: '投诉举报' + new Date().toLocaleDateString(),//下载文件名称
            },
			pagination :true,		//在底部显示分页组件
			pageList: [10, 20, 50],		//设置页面可以显示的数据条数
			pageSize: 10,			// 页面数据条数
			pageNumber: 1, 
			columns: [{ checkbox: true, visible: $(this).val() === 'selected'},
				{
					field: 'publishContent',
					title: '举报内容',
					align: 'left',
					valign: 'middle',
			        formatter: function (value, row, index){
			        	var url="${managerPath}/hd/"+row.id+"/report/show.do?1";
			        	return "<a href="+url+" target='_self' >"+value+"</a>";
			    	}
				},{
					field: 'name',
					title: '举报者',
					align: 'center',
					width: 100,
					valign: 'middle'
					
				},{
					field: 'createTime',
					title: '举报时间',
					align: 'center',
					width: 120,
					valign: 'middle'
				},{
		        	field: 'replyTime',
		        	title: '回复时间',
		        	align: 'center',
		        	width: 120,
		        	valign: 'middle'
			    },{
					field: 'state',
					title: '状态',
					align: 'center',
					width: 100,
					valign: 'middle',
					formatter: function(value,row,index){
						var state = "";
						if(row.state == 1){
							return "已回复";
						}else{
							return "未回复";
						}
					}
				},{
					title: '操作',
					align: 'center',
					width: 100,
					valign: 'middle',
					events: 'operateEvents',	//按钮的点击事件，必须加''
					formatter: function(value,row,index){
						var state = "";
						if(row.state == 1){
							//state="<button id='btn_replys' disabled></button>";
							state = "<button id='btn_detail' >修改</button>"
						}else{
							state="<button id='btn_reply'>回复</button>";
						}
						return state;
					}
				}
			]
		})
	}).trigger('change')
	$("#submitSearch").click(function(){
		var search = $("form[name='searchForm']").serializeJSON();
		var params = $('#columnList').bootstrapTable('getOptions');
		params.queryParams = function(params) {  
			$.extend(params,search);
				return params;  
			}  
		$("#columnList").bootstrapTable('refresh', {query:$("form[name='searchForm']").serializeJSON()});
	});
	//多选删除确认
	$("#delButton").click(function(){
		//获取勾选的所在行的所有数据
		var rows = $("#columnList").bootstrapTable("getSelections");
		if(rows!=""){
			//弹出modal窗体
			$(".delCategory").modal();
		}else{
			<@ms.notify msg="请选择内容!" type="warning"/>
		}
	});
	//删除调用
	$(".mydelete").click(function(){
		var rows = $("#columnList").bootstrapTable("getSelections");
		if(rows!=""){
			$.ajax({
				url:"${managerPath}/hd/del.do",
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
			<@ms.notify msg="请选择要删除的内容!" type="warning"/>
		}
	});		
	//按钮的点击事件
	window.operateEvents = {
		'click #btn_reply': function (e, value, row, index) {
			//回复点击事件
			if(row !=null){
				//alert(row.id);
				id = row.id;
				$('.mask').css('display','');
			}
			
		},
		'click #btn_detail': function (e, value, row, index) {
			//详情点击事件
			if(row !=null){
				show(row.id)
			}
		}
	};
	
	//跳转到详情页面
	function show(id){
		var ss = "${managerPath}/hd/"+id+"/report/show.do";
		location.href= ss;
	}
})
</script>