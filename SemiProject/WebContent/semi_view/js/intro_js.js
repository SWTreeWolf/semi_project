$(document).ready(function () {
    var height = $('.intro').outerHeight();

    $(window).scroll(function () {
        var win_height = $(this).scrollTop();
        if (win_height >= height) {
            $('.produce_01 .txt_wrap').fadeIn(500);
        } 
        if (win_height >= height * 2 - 200) {
            $('.produce_02 .txt_wrap').fadeIn(500);
        } 
        if (win_height >= height * 3 - 300) {
            $('.produce_03 .txt_wrap').fadeIn(500);
        } 
    });

    $('#btn_top_scroll').on('click', function () {
        $('html, body').animate({
            scrollTop: 0
        }, 400);
        return false;
    });

    var myslider = $('.diameter_bxslider').bxSlider({
        mode: 'fade',
        captions: true,
        pager: false,
        slideWidth: 500
    });

    $('.obj01').on('click', function () {
        return false;
    });

    $('.obj02').on('click', function () {
        $('html, body').animate({
            scrollTop: $('.intro').outerHeight()
        }, 600);
        return false;
    });

    $('.prev_btn').click(function () {
        myslider.goToPrevSlide();
        ddiv();
        return false;
    })

    $('.next_btn').click(function () {
        myslider.goToNextSlide();
        ddiv();
        return false;
    })

    $('#map').initMap;

    function ddiv() {
        var curr = myslider.getCurrentSlide();
        switch (curr) {
            case 0:
                $('.txtwrap_01').css('display', 'block');
                $('.txtwrap_02').css('display', 'none');
                $('.txtwrap_03').css('display', 'none');
                break;
            case 1:
                $('.txtwrap_01').css('display', 'none');
                $('.txtwrap_02').css('display', 'block');
                $('.txtwrap_03').css('display', 'none');
                break;
            case 2:
                $('.txtwrap_01').css('display', 'none');
                $('.txtwrap_02').css('display', 'none');
                $('.txtwrap_03').css('display', 'block');
                break;
        }
    }
});



function initMap() {
    var uluru = {
        lat: -22.363,
        lng: 80.044
    };
    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 4,
        center: uluru
    });
    var marker = new google.maps.Marker({
        position: uluru,
        map: map
    });
}