$(function () {
    //搜索
    $(".search-button").click(function(){
        var search_value = $(".search-input").val();
    $("#search-form").attr("action","/search/"+search_value);
    $("#search-form").submit();
    });



    //轮播图

    var iconleft = 0;
    $(".icon-circle-left").click(function () {
        if (iconleft == 0) {
            $(".tool-bar-hide").css({"display": "block"});
            iconleft = 1;
        } else {
            $(".tool-bar-hide").css({"display": "none"});
            iconleft = 0;
        }

    });
    var index = 0;

    function next_pic() {
        var newLeft;
        if ($(".slide-ul").css("left") == "-5950px") {
            newLeft = 0;

            if (index > 6) {
                index = 0;
            }
        } else {
            newLeft = parseInt($(".slide-ul").css("left")) - 1190;
        }
        index++;
        if (index >= 6) {
            index = 0;
        }
        $(".slide-ul").css({"left": newLeft + "px"});
        showCurrentDot();


    }

    var timer = null;

    function autoPlay() {

        timer = setInterval(function () {
            next_pic();
        }, 2000);
    }

    autoPlay();
    var container = document.querySelector(".slide");
    container.onmouseenter = function () {
        clearInterval(timer);
    }
    container.onmouseleave = function () {
        autoPlay();
    }

    //var dots = document.getElementsByTagName("li");
    var dots = $('.dots-li').find('li');
    var li1 = $(".dots-1");
    var li2 = $(".dots-2");
    var li3 = $(".dots-3");
    var li4 = $(".dots-4");
    var li5 = $(".dots-5");
    var li6 = $(".dots-6");

    var list = new Array(li1, li2, li3, li4, li5, li6);


    function showCurrentDot() {
        for (var i = 0, len = list.length; i < len; i++) {
            //list[i].className = "";
            list[i].removeClass("on");
        }

        list[index].addClass("on");
    }

    $(".dots-1").click(function () {
        $(".slide-ul").css({"left": "0px"});
        index = 0;
        showCurrentDot();

    });
    $(".dots-2").click(function () {
        $(".slide-ul").css({"left": "-1190px"});
        index = 1;
        showCurrentDot();

    });
    $(".dots-3").click(function () {
        $(".slide-ul").css({"left": "-2380px"});
        index = 2;
        showCurrentDot();

    });
    $(".dots-4").click(function () {
        $(".slide-ul").css({"left": "-3570px"});
        index = 3;
        showCurrentDot();

    });
    $(".dots-5").click(function () {
        $(".slide-ul").css({"left": "-4760px"});
        index = 4;
        showCurrentDot();

    });
    $(".dots-6").click(function () {
        $(".slide-ul").css({"left": "-5950px"});
        index = 5;
        showCurrentDot();

    });


// <script>

    function getLocation() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(showPosition, showError);
        } else {
            alert("浏览器不支持地理定位。");
        }
    }
    function showPosition(position) {
        $("#latlon").html("纬度:" + position.coords.latitude + '，经度:' + position.coords.longitude);
        var latlon = position.coords.latitude + ',' + position.coords.longitude;
        //baidu
        var url = "http://api.map.baidu.com/geocoder/v2/?ak=C93b5178d7a8ebdb830b9b557abce78b&callback=renderReverse&location=" + latlon + "&output=json&pois=0";
        $.ajax({
            type: "GET",
            dataType: "jsonp",
            url: url,
            beforeSend: function () {
                $("#baidu_geo").html('正在定位...');
            },
            success: function (json) {
                if (json.status == 0) {
                    $("#baidu_geo").html(json.result.addressComponent.province +
                        json.result.addressComponent.city);
                    console.log(json);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $("#baidu_geo").html(latlon + "地址位置获取失败");
            }

        });
    }
    function showError(error) {
        switch (error.code) {
            case error.PERMISSION_DENIED:
                alert("定位失败,用户拒绝请求地理定位");
                break;
            case error.POSITION_UNAVAILABLE:
                alert("定位失败,位置信息是不可用");
                break;
            case error.TIMEOUT:
                alert("定位失败,请求获取用户位置超时");
                break;
            case error.UNKNOWN_ERROR:
                alert("定位失败,定位系统失效");
                break;
        }
    }
    getLocation();

// </script>


})