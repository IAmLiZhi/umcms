$(function(){
    // var param = window.location.search;
    var param = window.localStorage.getItem('key');
    window.localStorage.removeItem('key');
    console.log('tag', param);
    var type = 0;
    var obj = {
        currtentPosition: '',
        parent: '',
    }
    switch(param){
        case 'advice':
            obj.currtentPosition = '我要咨询';
            obj.parent = '政策咨询';
            $('#jdo').css("display","block");
            $('#jdt').css("display","none");
            type = 1;
        break;
        case 'report':
            obj.currtentPosition = '我要投诉';
            obj.parent = '投诉举报';
            $('#jdo').css("display","none");
            $('#jdt').css("display","block");
            type = 2;
        break;
        case 'feedback':
            obj.currtentPosition = '我要反馈';
            obj.parent = '意见反馈';
            $('#jdo').css("display","block");
            $('#jdt').css("display","none");
            type = 3;
        break;
        case 'email':
            obj.currtentPosition = '给厅长写信';
            obj.parent = '厅长信箱';
            $('#jdo').css("display","block");
            $('#jdt').css("display","none");
            type = 4;
        break;
        case 'jdjb':
            obj.currtentPosition = '我要反映问题';
            obj.parent = '改进作风狠抓落实监督信箱';
            $('#jdo').css("display","none");
            $('#jdt').css("display","block");
            type = 5;
        break;
    }
    $('.current-position span.parent').text(obj.parent);
    $('.current-position span.son, .right>div p span').text(obj.currtentPosition);
//     互动交流发布
// /app/save/interaction
// {
// 	"name":发布人姓名,
// 	"phone":发布人手机号，
// 	"cardNo":身份证,
// 	"title":发布标题，
// 	"publishContent":发布内容,
// 	 type:		//1.咨询,2.投诉,3.反馈,4.信箱，
// 	 state:,	//1.已回复,2.未回复
// 	 appId:1，	//子站id
// }
    var $name = $('#name');
    var $phone = $('#phone');
    var $id = $('#id');
    var $title = $('#title');
    var $content = $('#content');
    var $namePhoneBox = $('.name-phone');
    var $idBox = $('.id');
    var $titleBox = $('.title');
    var $contentBox = $('.content');
    var $fileName = $('#fileName');
    var $file = $('.file');
    var filObj = '';
    var phoneRegExp = /^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\d{8}$/;
    var idRegExp = /^\d{17}([0-9]|X)$/;
    $('#submit').click(function(ev){
        var event = ev || window.event;
        if(event.preventDefault){
            event.preventDefault();
        }else{
            event.returnValue = false;
        }
        var name = $name.val().trim();
        if(!judgeName(name)) return false;
        var phone = $phone.val().trim();
        if(!judgePhone(phone)) return false;
        var id = $id.val().trim();
        if(!judgeId(id)) return false;
        var title = $title.val().trim();
        if(!judgeTitle(title)) return false;
        var content = $content.val().trim();
        if(!judgeContent(content)) return false;
        var fileName = $fileName.val().trim();
        if(fileName == ''){
            $.ajax({
                type: 'post',
                url: '/app/interaction/save.do',
                contentType: "application/json;charset=UTF-8",
                dataType: 'json',
                data: JSON.stringify({
                    name: name,
                    phone: phone,
                    cardNo: id,
                    title: title,
                    publishContent: content,
                    appId: 1,
                    type: type,
                    state: 2,
                    fileUrl: ''
                }),
                success: function(data){
                    if(data){
                        $('.mask').removeClass('none').children('.true').removeClass('none');
                        setTimeout(function(){
                            $('.mask').addClass('none').children('.true').addClass('none');
                            window.location.href = '../../5/index.html' + param;
                        }, 3000);
                    }else{
                        $('.mask').removeClass('none').children('.false').removeClass('none');
                        $('.mask').click(function(){
                            $('.mask').addClass('none').children('.false').addClass('none');
                        })
                    }
                },
                error: function(e){
                    console.log('e', e)
                }
            });
        }else{
            var formData = new FormData();
            formData.append('file', filObj);
            formData.append('appId', 1);
            formData.append('contentPath', 'hd');
            $.ajax({
                url: '/file/genupload',
                dataType: 'json',
                type: 'post',
                data: formData,
                cache: false,
                processData: false,
                contentType: false,
                success: function(data){
                    if(data.result){
                        $.ajax({
                            type: 'post',
                            url: '/app/interaction/save.do',
                            contentType: "application/json;charset=UTF-8",
                            dataType: 'json',
                            data: JSON.stringify({
                                name: name,
                                phone: phone,
                                cardNo: id,
                                title: title,
                                publishContent: content,
                                appId: 1,
                                type: type,
                                state: 2,
                                fileUrl: data.resultData[0].fileUrl
                            }),
                            success: function(data){
                                if(data){
                                    $('.mask').removeClass('none').children('.true').removeClass('none');
                                    setTimeout(function(){
                                        $('.mask').addClass('none').children('.true').addClass('none');
                                        window.location.href = '../../5/index.html' + param;
                                    }, 3000);
                                }else{
                                    $('.mask').removeClass('none').children('.false').removeClass('none');
                                    $('.mask').click(function(){
                                        $('.mask').addClass('none').children('.false').addClass('none');
                                    })
                                }
                            },
                            error: function(e){
                                console.log('e', e)
                            }
                        });
                    }
                },
                error: function(e){
                    console.log(e);
                }
            })
        }
       
    });
    $name.blur(function(){
        judgeName($(this).val().trim());
    });
    $phone.blur(function(){
        judgePhone($(this).val().trim());
    });
    $id.blur(function(){
        judgeId($(this).val().trim());
    });
    $title.blur(function(){
        judgeTitle($(this).val().trim());
    });
    $content.blur(function(){
        judgeContent($(this).val().trim());
    });
    $file.change(function(){
        var self = $(this);
        var fileName = self[0].files[0].name;
        var suffix = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length);
        var suffixRegExp = /(doc|docx|rtf|xls|xlsx|txt|jpg|png|mp3|mp4)$/;
        console.log('文件后缀', suffix);
        if(!suffixRegExp.test(suffix.toLowerCase())){
            $('span.file-prompt').removeClass('none').text('仅支持doc/rtf/xls/txt/jpg/png/mp3/mp4类型文件');
            return ;
        }
        var fileSize = self[0].files[0].size;
        if(fileSize > (10 * 1024 * 1024)){
            $('span.file-prompt').removeClass('none').text('文件大小不能大于10M');
            return ;
        }
        $('span.file-prompt').addClass('none');
        $fileName.val(fileName);
        filObj = self[0].files[0];
    })
    function judgeName(val){
        var value = val;
        if(value.length == 0){
            $namePhoneBox.children('.prompt').removeClass('visibel').children('.name').removeClass('visibel').text('请输入姓名');
            return false;
        }else{
            $namePhoneBox.children('.prompt').addClass('visibel').children('.name').addClass('visibel');
            return true;
        }
    }
    function judgePhone(val){
        var value = val;
        if(value.length == 0){
            $namePhoneBox.children('.prompt').removeClass('visibel').children('.phone').removeClass('visibel').text('请输入手机号');
            return false;
        }else if(!phoneRegExp.test(value)){
            $namePhoneBox.children('.prompt').removeClass('visibel').children('.phone').removeClass('visibel').text('请输入正确的手机号');
            return false;
        }else{
            $namePhoneBox.children('.prompt').addClass('visibel').children('.phone').addClass('visibel');
            return true;
        }
    }
    function judgeId(val){
        var value = val;
        if(value.length == 0){
            $idBox.children('.prompt').removeClass('visibel').children('span').removeClass('visibel').text('请输入身份证号');
            return false;
        }else if(!idRegExp.test(value)){
            $idBox.children('.prompt').removeClass('visibel').children('span').removeClass('visibel').text('请输入正确的身份证号');
            return false;
        }else{
            $idBox.children('.prompt').addClass('visibel').children('span').addClass('visibel');
            return true;
        }
    }
    function judgeTitle(val){
        var value = val;
        if(value.length == 0){
            $titleBox.children('.prompt').removeClass('visibel').children('span').removeClass('visibel').text('请输入标题');
            return false;
        }else if(value.length > 1024){
            $titleBox.children('.prompt').removeClass('visibel').children('span').removeClass('visibel').text('标题不得超过1024字');
            return false;
        }else{
            $titleBox.children('.prompt').addClass('visibel').children('span').addClass('visibel');
            return true;
        }
    }
    function judgeContent(val){
        var value = val;
        if(value.length == 0){
            $contentBox.children('.prompt').removeClass('visibel').children('span').removeClass('visibel').text('请输入内容');
            return false;
        }else if(value.length > 1024){
            $contentBox.children('.prompt').removeClass('visibel').children('span').removeClass('visibel').text('内容不得超过1024字');
            return false;
        }else{
            $contentBox.children('.prompt').addClass('visibel').children('span').addClass('visibel').text('请输入内容');
            return true;
        }
    }
    
})
