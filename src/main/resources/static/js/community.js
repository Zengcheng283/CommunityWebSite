function post() {
    var question_id = $("#question_id").val();
    var comment_text = $("#comment_text").val();

    if (!comment_text) {
        alert("回复未输入内容");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "id": question_id,
            "content": comment_text,
            "type": 1
        }),
        success: function (response) {
            if (response.code === 200) {
                $("#comment_section").hide();
                location.reload();
                // var url = location.href;
                // const splitWord = url.split("/")
                // var id = splitWord[splitWord.length-1]
                // var address = splitWord[2]
                // $.ajax({
                //     url: "http://"+ address + "/local/" + id,
                //     contentType: "application/json",
                //     success: function (response) {
                //         console.log(response);
                //     }
                // })
                // console.log("局部刷新")
            } else {
                if (response.code === 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=9b52377c31220b9e94e9&redirect_uri=http://localhost:8887/callback&scope=user&state=18871");

                    }
                }
                alert(response.message);
            }
        },
        dataType: "json"
    });
    console.log(question_id);
    console.log(comment_text);
}

function login() {
    // 获取当前所在页面
    var href = window.location.href;
    // console.log(href);
    // 将当前所在页面存储在sessionStorage中
    window.sessionStorage.setItem("callBackPage", href);
    // 加载登录
    location.href = "https://github.com/login/oauth/authorize?client_id=9b52377c31220b9e94e9&redirect_uri=http://localhost:8887/callback&scope=user&state=18871";

}

function showRepeatDialogue() {
    $("#comment_section").show();
}

function hideRepeatDialogue() {
    $("#comment_section").hide();
}