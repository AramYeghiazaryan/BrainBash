$().ready(function () {
    let pathArray = window.location.pathname.split('/');
    let id = parseInt(pathArray[pathArray.length - 1]);
    // let startTime = $('#startTime').val();
    console.log(dateTime);
    $('#editOptions').click(function () {

        let users = $("input[name='userSelect']:checked").map(function () {
            return parseInt($(this).attr("value"));
        }).toArray();

        let userTest = JSON.stringify({
            testId: id,
            usersId: users,
            startTime: dateTime
        });

         console.log(userTest);
        $.ajax({
            type: "POST",
            data: userTest,
            url: "/test/notify",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: (function () {
                location.href = "https://brainbash.herokuapp.com/test/organize";
            })

        });
        $.ajax({
            type: "POST",
            data: userTest,
            url: "/test/selectUser",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: (function (result) {
                location.href = 'https://brainbash.herokuapp.com/';
            })

        });

    });
});

let dateTime = $('#startTime').val();
$('#startTime').on('change', function updateDateTime() {
    dateTime = $(this).val();
});