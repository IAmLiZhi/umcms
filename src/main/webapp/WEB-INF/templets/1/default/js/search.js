$(function () {
    $('#headerSearch').css('display', 'none')
    height();
    function height(){
        var left = $('div.order-timer');
        var right = $('div.search-content');
        var height = left[0].clientHeight > right[0].clientHeight ? left[0].clientHeight : right[0].clientHeight;
        left.css('height', height + 'px');
        right.css('height', height + 'px');
    }
    var dateTime = 1;
    var order = false;
    var myarticlecontent = '';
    var currentPage = 1;
    var $orderDd = $('.order dd');
    $orderDd.click(function () {
        var $index = $(this).index();
        $orderDd.each(function (index, el) {
            if (index != $index) $(el).removeClass('active');
        });
        if (!$(this).hasClass('active')) {
            $(this).addClass('active');
        }
        currentPage = 1;
        myarticlecontent = $('.search-box input').val();
        if ($(this).hasClass('up')) {
            order = true;
            getData();
        } else {
            order = false;
            getData();
        }
    });
    var $timeDd = $('.timer dd');
    $timeDd.click(function () {
        var $index = $(this).index();
        $timeDd.each(function (index, el) {
            if (index != $index) $(el).removeClass('active');
        });
        if (!$(this).hasClass('active')) {
            $(this).addClass('active');
        }
        $('.current-position span').text($(this).text());
        currentPage = 1;
        myarticlecontent = $('.search-box input').val();
        switch ($index) {
            case 0:
                dateTime = 1;
                getData();
                break;
            case 1:
                dateTime = 4;
                getData();
                break;
            case 2:
                dateTime = 3;
                getData();
                break;
            case 3:
                dateTime = 2;
                getData();
                break;
        }
    });
    
    var dateStr = $('.timer dd.active').text();
    switch (dateStr) {
        case '一周内':
            dateTime = 2;
            break;
        case '一个月内':
            dateTime = 3;
            break;
        case '一年内':
            dateTime = 4;
            break;
    }
    var dateStr = $('.timer dd.active').text();
    
    switch(dateStr){
        case '一周内':
            dateTime = 2;
        break;
        case '一个月内':
            dateTime = 3;
        break;
        case '一年内':
            dateTime = 4;
        break;
    }
    
    if($('.order dd.active').text() == '按时间升序') order = true;
    $('#searchBtn').click(function(){
        getData($('.search-box input').val());
    });
    $('#searchBtn').prev().bind('keydown',function(event){
        if(event.keyCode == "13") {
            getData($('.search-box input').val());
        }
    }); 
    // 首页
    $('#home').click(function () {
        if (currentPage == 1) return;
        currentPage = 1;
        getData();
    });
    // 上一页
    $('.prev').click(function () {
        if (currentPage == 1) return;
        currentPage--;
        getData();
    });
    // 下一页
    $('#next').click(function () {
        if (currentPage == Number($('.pagenum .all').text())) return;
        currentPage++;
        getData();
    });
    // 最后一页
    $('.last').click(function () {
        if (currentPage == Number($('.pagenum .all').text())) return;
        currentPage = Number($('.pagenum .all').text());
        getData();
    });
    // 跳转
    $('.img-box').click(function () {
        var text = Number($('.go').val());
        if (isNaN(text) || text <= 0 || text > Number($('.pagenum .all').text()) || text == currentPage) return;
        currentPage = text;
        getData();
    });
    if ($('.order dd.active').text() == '按时间升序') order = true;
    var param = window.localStorage.getItem('content');
    if(param != ''){
        myarticlecontent = param;
        getData(myarticlecontent)
        $('.search-box input').val(myarticlecontent);
        window.localStorage.removeItem('content');
    }
    function getData(content) {
        if(content){
            myarticlecontent = content;
        }else{
            myarticlecontent = $('.search-box input').val();
        }
        var myData = {
            'pageNo': currentPage,
            'pageSize': 6,
            'articleContent': myarticlecontent, //搜索内容
            'basicDateTime': dateTime, //1,全部,2.前一周,3.前一月,4.前一年
            'order': order, //是否排序,true 升序,false 降序
            'appId': 1
        };
        $.ajax({
            url: '/app/1/search.do',
            type: 'post',
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify(myData),
            dataType: 'json',
            success: function (data) {
                data = data.data[0];
                var dataArr = data.list;
                console.log(data)
                if (data.total == 0){
                    $('.news').html('').removeClass('none');
                    $('.search-result').removeClass('none').children('span').text(data.total);
                    return;
                };
                $('.search-result').removeClass('none').children('span').text(data.total);
                $('.limit')
                    .removeClass('none')
                    .children('.allNum')
                    .children('span')
                    .text(data.total)
                    .parent()
                    .next()
                    .children('span.current')
                    .text(currentPage)
                    .next()
                    .text(data.total % 6 == 0 ? data.total / 6 : parseInt(data.total / 6) + 1);
                $('.news').html('').removeClass('none');
                var length = dataArr.length < 6 ? dataArr.length : 6;
                for (var i = 0; i < length; i++) {
                    var content = $(dataArr[i].articleContent).text();
                    var $li = $('<li class="every-news"><a class="every-news-title" target="_blank" href="' + dataArr[i].articleUrl + '">' + dataArr[i].basicTitle + '</a><p class="every-news-content">' + content + '</p><p class="every-news-time">发布时间：' + dataArr[i].basicUpdateTime + '</p></li>')
                    $('.news').append($li);
                }
                $('div.search-content').css('height', 'auto');
                $('div.order-timer').css('height', 'auto');
                height();
            },
            error: function (e) {
                console.log('tag', e);
            }
        });
    }
});