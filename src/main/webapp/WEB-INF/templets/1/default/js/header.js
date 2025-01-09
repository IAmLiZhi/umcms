$(function(){
    $('#search').click(function(){
        search();
    });
    
    function search(){
        articlecontent = $('#searchInput').val();
        // window.location.href = "http://www.xzedu.gov.cn/9/48/index.html?content=" + $(this).prev().val();
        // console.log('tag', articlecontent)
        window.open("http://edu.xizang.gov.cn/9/48/index.html", "_blank");
        // window.name = articlecontent;
        window.localStorage.setItem('content', articlecontent)
    }
    $('#searchInput').bind('keydown',function(event){
        if(event.keyCode == "13") {
            search();
        }
    }); 
});