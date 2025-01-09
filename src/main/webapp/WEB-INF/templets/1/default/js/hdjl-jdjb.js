$(function(){
    var currentPage = 1;
    getdata();
    var first = $('.first');
    var prev = $('.prev');
    var next = $('.next');
    var last = $('.last');
    var goPage = $('.goPage');
    var go = $('.go');
    first.click(function(){
        if(currentPage == 1) return;
        currentPage == 1;
        getdata();
    });
    prev.click(function(){
        if(currentPage == 1) return;
        currentPage--;
        getdata();
    });
    next.click(function(){
        if(currentPage == Number($('span.all').text())) return;
        currentPage++;
        getdata();
    });
    last.click(function(){
        if(currentPage == Number($('span.all').text())) return;
        currentPage = Number($('span.all').text());
        getdata();
    });
    go.click(function(){
        if(Number(goPage.val()) < 1 || Number(goPage.val()) > Number($('span.text').text()) || currentPage == Number(goPage.val())) return;
        currentPage = Number(goPage.val());
        getdata();
    })
    var $dl = $('dl');
    function getdata(){
        $.ajax({
            type: 'post',
            url: '/app/interaction/list.do',
            contentType: "application/json;charset=UTF-8",
            dataType: 'json',
            data: JSON.stringify({
                type: 5,
                appId: 1,
                pageNo: currentPage,
                pageSize: 10
            }),
            success: function(data){
                var $dt =  $('dl dt');
                $dl.html('').append($dt);
                data = data.data[0];
                var list = data.list;
                console.log(list)
                if(list.length == 0) return;
                for(var i = 0; i < list.length; i++){
                    var date = list[i].createTime;
                    // var $dd = $('<dd><p class="title"><img src="../../static/image/connection/exclamation-mark.png"/><a target="_blank" href="/5/292/index.html?' + list[i].id + '">' + list[i].title + '</a></p><p class="time">' + date + '</p><p class="already">已回复</p></dd>');
                    var $dd = $('<dd><p class="title"><img src="../../static/image/connection/exclamation-mark.png"/><a target="_blank" href="javascript:;" id="' + list[i].id + '">' + list[i].title + '</a></p><p class="time">' + date + '</p><p class="already">已回复</p></dd>');
                    $dl.append($dd);
                }
                $('.limit')
                    .children('p.allNum')
                    .children('span')
                    .text(data.total)
                    .parent()
                    .next()
                    .children('span.current')
                    .text(data.pageNum)
                    .next()
                    .text(data.total % 10 == 0 ? data.total / 10 : parseInt(data.total / 10) + 1);
            },
            error: function(e){
                console.log('e', e)
            }
        });
    }
    $('p.button button').click(function(){
        window.localStorage.setItem('key', 'jdjb');
        $(this).parent().attr({
            'href': '../49/index.html'
        })
    });
    $('dl').on('click', 'a', function(){
        window.localStorage.setItem('key', $(this).attr('id'));

        $(this).attr('href', '/5/292/index.html');
    });
})
