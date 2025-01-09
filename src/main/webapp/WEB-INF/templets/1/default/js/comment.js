;(function($){
    $.fn.lastLi = function(option){
        var dft = {
            dividend: 3, // 被除数
            remainder: 2, // 余数
        };
        var endOptions = $.extend(dft, option);
        $(this).each(function(index, el){
            if(index % endOptions.dividend == endOptions.remainder) $(el).css('margin-right', 0);
        });
    }
    $.fn.changeDiv = function(){
        var $allLi = $(this)
        $allLi.each(function(index, el){
            $(el).bind('click', function(){
                var $li = $(this);
                var $divBox = $li.parent().parent();
                var $allDiv = $divBox.children('div');
                var $index = $li.index();
                if(!$li.hasClass('active')){
                    $allLi.each(function(index, el){
                        if($(el).hasClass('active')){
                            $(el).removeClass('active');
                            var $img = $(el).children('img');
                            $img.attr('src', $img.attr('src').replace(/-on/, '-out'));
                        }
                    });
                    $allDiv.each(function(index, el){
                        if(index == $index) $(el).removeClass('none')
                        else $(el).addClass('none');
                    });
                    $li.addClass('active');
                    $li.children('img').attr('src',$li.children('img').attr('src').replace(/-out/, '-on'));
                    $('.current-position span').html($li.text());
                }
            })
        });
    }
    $.fn.even = function(option){
        var dft = {
            evenClass: 'even'
        };
        var self = $(this);
        var endOptions = $.extend(dft, option);
        self.children('li:odd').addClass(endOptions.evenClass);
    }
    $.fn.changeUl = function(option){
        var dft = {
            html: 'ul',
            eventType: 'click'
        };
        var endOptions = $.extend(dft, option);
        var $allP = $(this);
        $allP.each(function(index, el){
            $(el).bind(endOptions.eventType, function(){
                var $p = $(this);
                var $a = $p.parent();
                var $aIndex = $a.index();
                var $div = $a.parent();
                var $divBox = $div.parent();
                var $allUl = $divBox.children(endOptions.html);
                var haveTwo = true;
                if(!$p.hasClass('active')){
                    $allP.each(function(index, el){
                        if($(el).hasClass('active')) $(el).removeClass('active');
                        if($(el).hasClass('two')) haveTwo = false;
                    });
                    $allUl.each(function(index, el){
                        if(index == $aIndex) $(el).removeClass('none');
                        else $(el).addClass('none');
                    });
                    if(haveTwo){
                        $allP.each(function(index, el){
                            if(index == $aIndex) $(el).children('img').removeClass('visibel');
                            else $(el).children('img').addClass('visibel');
                        });
                    }
                    $p.addClass('active');
                }
            })
        })
    }
    $.getCurrentDateFormat = function(option){
        var dft = {
            split: '-'
        };
        var date = new Date();
        var endOptions = $.extend(dft, option);
        var dateArr = date.toLocaleDateString().split('/');
        var nowDate = '';
        for(var i = 0; i < dateArr.length; i++){
            nowDate += dateArr[i] + endOptions.split;
        }
        return nowDate.substring(0, nowDate.length - 1);
    }   
    $.fn.changeDd = $.fn.changeLi = function(){
        var $all = $(this);
        $all.each(function(index, el){
            $(el).bind('click', function(){
                var self = $(this);
                if(!self.hasClass('active')){
                    $all.each(function(index, el){
                        if($(el).hasClass('active')) $(el).removeClass('active');
                    });
                    self.addClass('active')
                }
            });
        });
    }
    $.changeFontSize = function(el, val){
        var $content = $(el);
        if(val == '【小】'){
            $content.find('*').css('fontSize', '14px');
        }
        else if(val == '【中】'){
            $content.find('*').css('fontSize', '16px');
        }
        else if(val == '【大】'){
            $content.find('*').css('font-size', '18px');
        }
    }
    $.hiddenLimit = function(option){
        var dft = {
            parent: '.content-box',
            length: 18,
            pageSize: 6
            // pageNum: 1,
        };
        var endOptions = $.extend(dft, option);
        var $div = $(endOptions.parent);
        var $allLi = $div.children('ul').children('li');
        $allLi.each(function(index, el){
            if(index % endOptions.pageSize == (endOptions.pageSize - 1) && index != $allLi.length - 1){
                $(el).addClass('bottom').next().css('margin-top', '28px');
            }
        });
        // if($allLi.length < endOptions.length && endOptions.pageNum != 1){
        //     $div.children('div.limit').css('display', 'none');
        // }
    }
    $.hiddenMore = function(option){
        var dft = {
            parent: '',
            length: 7,
            more: 'li.more-box'
        };
        var endOptions = $.extend(dft, option);
        var $ul = $(endOptions.parent);
        if($ul.children('li').length < endOptions.length){
            $ul.children(endOptions.more).css('display', 'none');
        }
    }
    $.fn.print = function(){
        $(this).bind('click', function(){
            window.print();
        });
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
            if(isNaN(goPageNum) || goPageNum > allTotal || goPageNum == 0){
                return;
            }
            if(goPageNum == 1){
                location.href = './index.html';
            }else{
                location.href = './list' + goPageNum + '.html';
            }
        })
    }
    $.content = function(){
        var dft = {
            parent: '.content'
        };
        $(dft.parent).html($(dft.parent).text());
    }
    $.borderStyle = function(option){
        var dft = {
            parent: '.content'
        }
        var endOptions = $.extend(dft, option);
        $(endOptions.parent).find('table').css('max-width', '825px').css('scroll-x', 'auto');
        $(endOptions.parent).find('td').css('border', '1px solid #000');
    }
    $.hit = function(id){
        $.getJSON('/app/site/hit.do',{
            'id': id
        },function(data){
            console.log('tag', data)
        });
    }
    $.changeImgSrc = function(str){
        var $img = $('#logo');
        var imgSrc = $img.attr('src');
        imgSrc = imgSrc.replace(/header\/logo\.png/, str);
        $img.attr('src', imgSrc).css('width', 'auto').css('height', '130px').parent().attr('href', 'javascript:;');
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
                ).Format("yy-MM-dd").toString()
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
    var data = JSON.stringify({
            'page' : page,
            'referer' : referer
        })
        console.log(data)
    	$.ajax({
            url: '/app/site/flow.do',
            type: 'POST',
            dataType: 'json',
            data: data,
            cache: false,
            processData: false,
            headers: { 
            	'Content-Type': 'application/x-www-form-urlencoded'
            },                
            success: function(data){console.log(data)},
            error: function(e) {console.log(e)}
        });
})(location.href, document.referrer, 'true');
