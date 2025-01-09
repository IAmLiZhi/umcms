$(function(){
    var site_code = '5400000027'
    $('#jiucuo').click(function(){
        var url = getCurrUrl();
        //跳转至纠错系统填写页面 
        // window.open("http://121.40.115.217:9080/check_web/errorInfo/jcInfoNew?site_code=" + site_code + "&url=" + encodeURI(url));
        window.open("https://zfwzgl.www.gov.cn/exposure/jiucuo.html?site_code=" + site_code + "&url=" + encod3eURIComponent(url));
    })
    //获取该站点需要纠错页面的url地址
    function getCurrUrl() {
        var url = "";
        if (parent !== window) {
            try {
                url = window.top.location.href;
            } catch (e) {
                url = window.top.document.referrer;
            }
        }
        if (url.length == 0)
            url = document.location.href;

        return url;
    }
})