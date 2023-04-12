/**
 * 
 */

/* 'use strict';
*/
// document.write("<script src='jquery-3.6.1.js'></script>")

document.write("<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.6.3/jquery.min.js'></script>")


var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var stompClient = null;
var username = null;

// roomId 파라미터 가져오기
const url = new URL(location.href).searchParams;
const roomId = url.get('roomId');

function connect(event) {
	username = $("#name").val().trim();
    // username 중복 확인
    isDuplicateName();

    // usernamePage 에 hidden 속성 추가해서 가리고
    // chatPage 를 등장시킴
    $("#username-page").hide();
    //usernamePage.classList.add('hidden');
    $("#chat-page").show();
    //chatPage.classList.remove('hidden');

    // 연결하고자하는 Socket 의 endPoint
    var socket = new SockJS('/ws-stomp');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);


    event.preventDefault();


}

function onConnected() {

    // sub 할 url => /sub/chat/room/roomId 로 구독한다
    stompClient.subscribe('/sub/chat/room/' + roomId, onMessageReceived);

    // 서버에 username 을 가진 유저가 들어왔다는 것을 알림
    // /pub/chat/enterUser 로 메시지를 보냄
    stompClient.send("/pub/chat/enterUser",
        {},
        JSON.stringify({
            "roomId": roomId,
            sender: username,
            type: 'ENTER'
        })
    )

    
}

$("#showUserListButton").on("click", getUserList);


// 유저 닉네임 중복 확인
function isDuplicateName() {

    $.ajax({
        type: "GET",
        url: "/chat/duplicateName",
        data: {
            "username": username,
            "roomId": roomId
        },
        success: function (data) {
            console.log("함수 동작 확인 : " + data);

            username = data;
        }
    })

}

// 유저 리스트 받기
// ajax 로 유저 리스를 받으며 클라이언트가 입장/퇴장 했다는 문구가 나왔을 때마다 실행된다.
function getUserList() {
    var $list = $("#list");
    
    $.ajax({
        type: "GET",
        url: "/chat/userlist",
        data: {
            "roomId": roomId
        },
        success: function (data) {
            var users = "";
            for (let i = 0; i < data.length; i++) {
                users += "<li class='dropdown-item'>" + data[i] + "</li>"
            }
            $list.html(users);
            console.log(data);
        }
    })
}


function onError(error) {
    console.log("error" + error);
}

// 메시지 전송때는 JSON 형식을 메시지를 전달한다.
function sendMessage(event) {
	var messageContent = $("#message").val().trim();
    
	console.log("messageContent : " + messageContent);
    if (messageContent && stompClient) {
        var chatMessage = {
            "roomId": roomId,
            sender: username,
            message: messageContent,
            type: 'TALK'
        };

        stompClient.send("/pub/chat/sendMessage", {}, JSON.stringify(chatMessage));
        $("#message").val('');
    }
    event.preventDefault();
}

// 메시지를 받을 때도 마찬가지로 JSON 타입으로 받으며,
// 넘어온 JSON 형식의 메시지를 parse 해서 사용한다.
function onMessageReceived(payload) {
    var chat = JSON.parse(payload.body);
    console.log(chat);
	    if (chat.type === 'ENTER') {  // chatType 이 enter 라면 아래 내용
        chat.content = chat.sender + "  :   " + chat.message;
        getUserList();
        $(".messageArea").append("<li>" + chat.message +"</li>");

    } else if (chat.type === 'LEAVE') { // chatType 가 leave 라면 아래 내용
        chat.content = chat.sender + "  :   " + chat.message;
        
        getUserList();
        $(".messageArea").append("<li>" + chat.message +"</li>");

    } else { // chatType 이 talk 라면 아래 내용용
		chat.content = chat.sender + "  :   " + chat.message;
		$(".messageArea").append("<li>"+chat.sender  + "   :  " + chat.message +"</li>");

    	}
	}
$("#usernameForm").on("submit", connect);
$("#messageForm").on("submit", sendMessage);
