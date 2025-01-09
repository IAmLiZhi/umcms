<@shiro.hasPermission name="activity:update">
var url = "${managerPath}/${model?default("")}/column/"+row.categoryId+"/edit.do?modelId=${Session.model_id_session?default(0)}&modelTitle=${Session.model_title_session?default('')}";
return "<a href=" +url+ " target='_self'>" + value + "</a>";
</@shiro.hasPermission>
<@shiro.lacksPermission name="activity:update">
return value;
</@shiro.lacksPermission> 