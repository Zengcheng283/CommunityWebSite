function post() {
    var targetId = $("#question_id").val();
    var content = $("#comment_text").val();

    comment2target(targetId, 1, content);


}

function comment2target(targetId, type, content) {
    if (!content) {
        alert("回复未输入内容");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "id": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code === 200) {
                $("#comment_section").hide();
                location.reload();
            } else {
                if (response.code === 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        login();
                    }
                }
                alert(response.message);
            }
        },
        dataType: "json"
    });
}

function comment(e) {
    var id = e.getAttribute("data-id");
    var content = $("#reply-" + id).val();

    console.log(id);

    comment2target(id, 2, content);
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
    $("#comment_text").val("");
}

/**
 * 展开二级评论
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);


    var collapse = e.getAttribute("data-collapse");

    if (collapse) {
        var subCommentContainer = $("#comment-" + id);
        subCommentContainer.children(".content").remove();


        // 折叠二级评论
        comments.toggleClass("in");
        e.classList.toggle("active");
        e.removeAttribute("data-collapse");
    } else {
        $.getJSON("/comment/" + id, function (data) {
                var subCommentContainer = $("#comment-" + id);
                $.each(data.data, function (index, comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD HH:mm')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>",
                        {
                            "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments content"
                        }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });

            }
        )
        ;
        comments.toggleClass("in");
        e.classList.toggle("active");
        e.setAttribute("data-collapse", "in");
    }
}