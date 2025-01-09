<@ms.html5>
	<@ms.nav title="站点流量">
		<select name="websites" id="websites" style="height: 34px; width: 200px; float: right; margin: 5px 0 0 10px;">
			<#if websites??>
				<#list websites as website>
					<#if websiteId?eval == website.appId> 
						<option value=${website.appId} selected>${website.appName}</option>
					<#else>
						<option value=${website.appId}>${website.appName}</option>
					</#if>
				</#list>
			<#else>
				<option value=0>未找到子站</option>
			</#if>
		</select>
		<p style="float: right; font-size: 13px; color: #555555; margin: 0; font-weight: bold;"><span style="color: #FF6666;">*</span>当前子站</p>
	</@ms.nav>
	<@ms.panel style="width: 1130px; margin: 0 auto; padding: 55px 0 30px;">
		<div class="my-left">
			<div class="small">
				<p>内容统计</p>
				<div class="my-parent">
					<div class="my-left-left">
						<img src="../../../../static/skin/manager/4.6.4/images/article-list.png" alt="" title="">
						<div>
							<p class="my-desc">文章总数</p>
							<p class="total-article my-num"></p>
						</div>
					</div>
					<div class="my-left-right">
						<img src="../../../../static/skin/manager/4.6.4/images/add-article.png" alt="" title="">
						<div>
							<p class="my-desc">今日新增</p>
							<p class="today-article my-num"></p>
						</div>
					</div>
				</div>
			</div>
			<div class="big">
				<div class="echarts"></div>
			</div>
			<div class="big">
				<div class="echarts2"></div>
			</div>
		</div>
		<div class="my-right">
			<div class="small">
				<p>流量统计</p>
				<div class="my-parent">
					<div class="desc">
						<p>总数</p>
						<p>今日</p>
					</div>
					<div class="my-right-left">
						<p class="my-desc">浏览量（PV）</p>
						<p class="total-pv my-num"></p>
						<p class="today-pv my-num"></p>
					</div>
					<div class="my-right-middle">
						<p class="my-desc">IP数（IP）</p>
						<p class="total-ip my-num"></p>
						<p class="today-ip my-num"></p>
					</div>
					<div class="my-right-right">
						<p class="my-desc">访客数</p>
						<p class="total-visitor my-num"></p>
						<p class="today-visitor my-num"></p>
					</div>
				</div>
			</div>
			<div class="big">
				<p>文章访问TOP10</p>
				<ul class="article">
					<li><p class="one title">受访文章</p><p class="two title">浏览量（PV）</p></li>
				</ul>
			</div>
			<div class="big">
				<p>来源网站TOP10</p>
				<ul class="from">
					<li><p class="one title">来源网站</p><p class="two title">浏览量（PV）</p></li>
				</ul>
			</div>
		</div>
	</@ms.panel>
</@ms.html5>

<script>
$(function(){
	function getBeforeDate(n){
		var n = n;
		var d = new Date();
		var year = d.getFullYear();
		var mon=d.getMonth()+1;
		var day=d.getDate();
		if(day <= n){
				if(mon>1) {
					mon=mon-1;
				}
				else {
				year = year-1;
				mon = 12;
				}
				}
			d.setDate(d.getDate()-n);
			year = d.getFullYear();
			mon=d.getMonth()+1;
			day=d.getDate();
		s = year+"-"+(mon<10?('0'+mon):mon)+"-"+(day<10?('0'+day):day);
		return new Date(s);
	}
		var myEcharts = echarts.init($('.echarts')[0]);
		var myEcharts2 = echarts.init($('.echarts2')[0]);
		$.post('${managerPath}/mwebsite/stats/pv/list.do?websiteId=${websiteId}', {
			flag: 3,
			begin: getBeforeDate(7),
			end: getBeforeDate(1)
		}, function(data){
			var option = {
				title: {
					text: '流量趋势图'
				},
				tooltip: {
					trigger: 'axis'
				},
				legend: {
					data:['IP数(IP)','浏览量(PV)','访客量(PV)']
				},
				grid: {
					left: '3%',
					right: '4%',
					bottom: '3%',
					containLabel: true
				},
				toolbox: {
					feature: {
						saveAsImage: {}
					}
				},
				xAxis: {
					type: 'category',
					boundaryGap: false,
					data: []
				},
				yAxis: {
					type: 'value'
				},
				series: [
					{
						name:'IP数(IP)',
						type:'line',
						stack: 'ips',
						data: []
					},
					{
						name:'浏览量(PV)',
						type:'line',
						stack: 'lll',
						data: []
					},
					{
						name:'访客量(PV)',
						type:'line',
						stack: 'fkl',
						data: []
					}
				]
			};
			console.log(data);
			for(var i = 0; i < data.length; i++){
				option.xAxis.data.push(data[i].statisticDate);
				option.series[0].data.push(data[i].ip);
				option.series[1].data.push(data[i].pv);
				option.series[2].data.push(data[i].visitors);
			}
			myEcharts.setOption(option);
		}, 'JSON');
		$.post('${managerPath}/mwebsite/stats/source/list.do?websiteId=${websiteId}', {type: 'link'}, function(data){
			for(var i = 0; i < data.length; i++){
				var $li = $('<li><p class="one">' + data[i].statisticColumnValue + '</p><p class="two">' + data[i].pv + '</p></li>');
				$('.from').append($li);
			}
		}, 'json');
		$.post('${managerPath}/mwebsite/stats/source/list.do?websiteId=${websiteId}', {type: 'source'}, function(data){
			var option2 = {
				title : {
					text: '来源分布图',
					x:'center'
				},
				tooltip : {
					trigger: 'item',
					formatter: "{a} <br/>{b} : {c} ({d}%)"
				},
				legend: {
					orient: 'vertical',
					left: 'left',
					data: []
				},
				series : [
					{
						name: '访问来源',
						type: 'pie',
						radius : '55%',
						center: ['50%', '60%'],
						data:[],
						itemStyle: {
							emphasis: {
								shadowBlur: 10,
								shadowOffsetX: 0,
								shadowColor: 'rgba(0, 0, 0, 0.5)'
							}
						}
					}
				],
				color: ['#37A2DA', '#32C5E9', '#67E0E3', '#9FE6B8', '#FFDB5C', '#FF9F7F']
			};
			for(var i = 0; i < data.length; i++){
				option2.legend.data.push(data[i].statisticColumnValue);
				option2.series[0].data.push({value: data[i].pv, name: data[i].statisticColumnValue});
			}
			myEcharts2.setOption(option2);
		}, 'json');
		$.post('${managerPath}/mwebsite/stats/article/list.do?websiteId=${websiteId}', {}, function(data){
			for(var i = 0; i < data.length; i++){
				var $li = $('<li><p class="one">' + data[i].basicTitle + '</p><p class="two">' + data[i].basicHit + '</p></li>')
				$('.article').append($li);
			}
		}, 'json');
		$.post('${managerPath}/mwebsite/stats/pv/count.do?websiteId=${websiteId}', {}, function(data){
			$('.total-pv').text(number_format(data.pvTotal)).next().text(number_format(data.pvToday));
			$('.total-ip').text(number_format(data.ipTotal)).next().text(number_format(data.ipToday));
			$('.total-visitor').text(number_format(data.visitorsTotal)).next().text(number_format(data.visitorsToday));
		}, 'json');
		$.post('${managerPath}/mwebsite/stats/article/count.do?websiteId=${websiteId}', {}, function(data){
			$('.total-article').text(number_format(data.total));
			$('.today-article').text(number_format(data.today));
		}, 'json');
	$('#websites').change(function(){
		var self = $(this);
		var appId = self.val();
		location.href = '${managerPath}/mwebsite/stats/index.do?websiteId=' + appId;
	});
	function number_format(num) {
		return num && num
		.toString()
		.replace(/(^|\s)\d+/g, (m) => m.replace(/(?=(?!\b)(\d{3})+$)/g, ','))
	} 
});
</script>