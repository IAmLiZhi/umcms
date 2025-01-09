;(function($){
    $.fn.lastLi = function(option){
        var dft = {
            dividend: 3, // 被除数
            remainder: 2, // 余数
        };
        var endOptions = $.extend(dft, option);
        $(this).each(function(index, el){
            if(index % endOptions.dividend == endOptions.remainder) $(el).css('marginRight', 0);
        });
    }
    $.hiddenLimit = function(option){
        var dft = {
            parent: '.right',
            length: 15,
            num: 5,
        };
        var endOptions = $.extend(dft, option);
        var $div = $(endOptions.parent);
        var $allLi = $div.children('ul').children('li');
        $allLi.each(function(index, el){
            if(index % endOptions.num == (endOptions.num - 1) && index != $allLi.length - 1){
                $(el).addClass('bottom').next().css('margin-top', '21px');
            }
        });
    }
    $.fn.print = function(){
        $(this).bind('click', function(){
            window.print();
        });
    }
    $.getCurrentDate = function(){
        var date = new Date();
        var week = '星期';
        switch(date.getDay()){
            case 1:
                week += '一'
            break;
            case 2:
                week += '二'
            break;
            case 3:
                week += '三'
            break;
            case 4:
                week += '四'
            break;
            case 5:
                week += '五'
            break;
            case 6:
                week += '六'
            break;
            case 0:
                week += '日'
            break;
        }
        var year = date.getFullYear()
        var month = date.getMonth() + 1;
        var day = date.getDate();
        date = year + '年' + month + '月' + day + '日 ' + week;
        $('p.date').text(date);
    }
    $.goPage = function(option){
        var dft = {
            goEL: '.go',
            inputEl: '.goPage',
            allTotal: '.pagenum'
        }
        var endOptions = $.extend(dft, option);
        $(endOptions.goEL).click(function(){
            var goPageNum = Number($(endOptions.inputEl).val());
            var allTotalStr = $(endOptions.allTotal).text();
            allTotalStr = allTotalStr.substring(allTotalStr.indexOf('/') + 1, allTotalStr.length);
            var allTotal = Number(allTotalStr);
            if(isNaN(goPageNum) || goPageNum > allTotal){
                return;
            }
            if(goPageNum == 1){
                location.href = './index.html';
            }else{
                location.href = './list' + goPageNum + '.html';
            }
        })
    }
    $.height = function(){
        var $left = $('.left');
        var left = $left[0];
        var $middle = $('.middle');
        var $right = $('.right');
        var right = $right[0];
        var max = 0;
        if($middle.length != 0){
            var middle = $middle[0];
            if(left.clientHeight > middle.clientHeight){
                if(left.clientHeight > right.clientHeight)
                    max = left.clientHeight;
                else
                    max = right.clientHeight;
            }else{
                max = middle.clientHeight;
            }
            $middle.css('height', max + 'px');
        }else{
            max = left.clientHeight > right.clientHeight ? left.clientHeight : right.clientHeight;
        }
        $left.css('height', max + 'px');
        $right.css('height', max + 'px');
        
    }
    $.borderStyle = function(option){
        var dft = {
            parent: '.content'
        }
        var endOptions = $.extend(dft, option);
        $(endOptions.parent).find('td').css('border-style', 'solid');
    }
    $.hit = function(id){
        $.getJSON('/app/site/hit.do',{
            'id': id
        },function(data){
            console.log('tag', data);
        });
    }
    $.searchClick = function(articlecontent){
        window.location.href = "http://zsks.edu.xizang.gov.cn/57/98/index.html";
        window.name = articlecontent;
    }
    Date.prototype.Format=function(fmt){
        var o = {
            'M+': this.getMonth() + 1,//月份
            'd+': this.getDate(),//日
            'H+': this.getHours(),//小时
            'm+': this.getMinutes(),//分
            's+': this.getSeconds(),//秒
            'q+': Math.floor((this.getMonth()+3)/3),//季度
            'S+': this.getMilliseconds()//毫毛
        };
        if(/(y+)/.test(fmt)) 
            fmt=fmt.replace(RegExp.$1,(this.getFullYear()+"").substr(4-RegExp.$1.length));
        for (var k in o)
        if (new RegExp("(" + k + ')').test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (('00' + o[k]).substr(("" + o[k]).length)));
        return fmt;
    };
    $.new = function(arr){
        var lastMonth = [];
        for(var i = 0;i<30;i++){
            lastMonth.unshift(
                new Date(
                    new Date().setDate(
                        new Date().getDate()-i
                    )
                ).Format("yyyy-MM-dd").toString()
            );
        }
        for(var j = 0; j < arr.length; j++){
            $(arr[j]).each(function(index, el){
                $(el).children('li').each(function(index, el){
                    var everyDate = $(el).children('.date').text();
                    console.log('tag', everyDate)
                    if(lastMonth.indexOf(everyDate) < 0){
                        $(el).children('img').css('display', 'none');
                    }
                })
            })
        }
        console.log('tag', lastMonth);
    }
})(jQuery);
(function(page, referer,flowSwitch) {
    flowSwitch=flowSwitch||"true";
    if(flowSwitch=="true"){
        $.getJSON("/app/site/flow.do", {
            page : page,
            referer : referer
        }, function(data) {
        });
    }
})(location.href, document.referrer, 'true');