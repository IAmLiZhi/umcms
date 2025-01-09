<@ms.html5>
	<@ms.nav title="审核列表"></@ms.nav>
	<@ms.panel>
		<div id="toolbar">
			<div>
				<button id="pass" style="height: 34px; width: 100px; padding: 0">审核通过</button>
				<button id="miss" style="height: 34px; width: 100px; padding: 0">审核不通过</button>		
			</div>
		</div>
		<table id="columnList" 
			data-show-columns="true"
			data-show-refresh="true"
			data-method="post" 
			data-side-pagination="server">
		</table>
		<div class="mask" style="position: fixed; left: 0; top: 0; z-index: 1000; height: 100%; width: 100%; background-color: rgba(0,0,0,0.5); display: none;">
			<div class="alert" style="width: 413px; height: 498px; background-color: #ffffff; padding: 17px 21px; margin: 33px auto 0;">
				<p style="overflow: hidden; font-size: 14px; color: #333333;">请输入不通过的理由<img class="close" style="opacity: 1; width: 10px; height: 10px; float: right; margin-top: 5px; cursor: pointer;" src="../../../../static/skin/manager/4.6.4/images/close.png"/></p>
				<textarea class="note" style="width: 369px; height: 366px; margin-top: 15px;"></textarea>
				<p class="prompt" style="text-align: right; color: red; display: none;">必填项</p>
				<p style="text-align: right; margin-top: 10px;"><button class="cancel" style="background-color: #ffffff; border: 1px solid #666666; color: #666666; font-size: 14px; width: 71px; height: 28px; margin-right: 11px;">取消</button><button class="choose" style="width: 71px; height: 28px; background-color: #00AEFF; border: 0; font-size: 14px; color: #ffffff;">确定</button></p>
			</div>
		</div>
	</@ms.panel>
	<@ms.modal modalName="delete" title="审核文章">
		<@ms.modalBody>
				确定通过审核?
		</@ms.modalBody>
		<@ms.modalButton>
			<@ms.button class="btn btn-danger rightDelete" value="确定"/>
		</@ms.modalButton>
	</@ms.modal>	
</@ms.html5>
<script>
	$(function(){
		$("#columnList").bootstrapTable({
			url:"${managerPath}/cms/audit/list.do",
			contentType : "application/x-www-form-urlencoded",
			queryParamsType : "undefined",
			toolbar: "#toolbar",
			pagination :true,		//在底部显示分页组件
			pageList: [10, 20],		//设置页面可以显示的数据条数
            pageSize: 10,			// 页面数据条数
            pageNumber: 1, 
	    	columns: [{ checkbox: true}, 
				{
			        field: 'id',
			        title: '任务Id',
			        width: 50,
			        align: 'left'
			    },{
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
			        width: 100
			    }, {
			        field: 'basicDateTime',
			        title: '发布时间',
			        align: 'center',
			        width: 90
			    }]
	    })
		var taskIds = [];
		var note = '';
	    $('#pass').click(function(){
			$('input[name]').each(function(index, el){
				if(index != 0){
					if($(el).is(':checked')){
						taskIds.push(Number($(el).val()));
					}
				}
			});
			if(taskIds.length == 0){
				<@ms.notify msg="请选择文章!" type="warning"/>
				$('.mask, .prompt').css('display', 'none');
				return;
			}else{
				$(".delete").modal();
			}
		});
		$(".rightDelete").click(function(){
			check(taskIds,true,note);
		})
		$('#miss').click(function(){
			$('input[name]').each(function(index, el){
				if(index != 0){
					if($(el).is(':checked')){
						taskIds.push(Number($(el).val()));
					}
				}
			});
			if(taskIds.length == 0){
				<@ms.notify msg="请选择文章!" type="warning"/>
				$('.mask, .prompt').css('display', 'none');
				return;
			}
			$('.mask').css('display', '');
		});
		$('.note').change(function(){
			var val = $(this).val();
			if(val.trim() != ''){
				$('.prompt').css('display', 'none');
			}else{
				$('.prompt').css('display', '');
			}
		})
		$('.choose').click(function(){
			note = $('.note').val().trim();
			if(note == ''){
				$('.prompt').css('display', '');
				return;
			}
			else $('.prompt').css('display', 'none');
			$('.mask').css('display', 'none');
			check(taskIds,false,note);
		});
		$('.close, .cancel').click(function(){
			$('.mask').css('display', 'none');
		});
		function check(arr, state, note){
			
			$.ajax({
				url: '${managerPath}/cms/audit/finish.do',
				dataType: 'json',
				data: JSON.stringify({
					"taskIds": arr,
					"state": state,
					'note': note
				}),
				contentType: "application/json",
				type: 'post',
				success: function(data){
					if(data){
						window.location.reload();
					}
				},
				error: function(e){
				
				}
			})
		}
});
</script>