<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>jsp</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.3/jquery.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
<style type="text/css">
	#menu{
		width: 350px;
	}
	input{
		width: 250px;
		margin-top: 15px;
	}
	#list{
		display: block;
		margin-left: 200px;
	}
	
	.input-group{width: 80%}
	.chat-container{position: rel }
		
</style>

</head>
<body>
	<div class="container">
		<div id="username-page">
			<h3>당신의 이름을 입력하세요</h3>
			<form id="usernameForm" name="usernameForm">
				<c:if test="${user == null }">
					<div class="form-group">
					<input type="text" name="name" id="name" placeholder="사용자명" class="form-control" />
					</div>
				</c:if>
				<c:if test="${user != null }">
					<div class="form-group">
					<input type="text" name="name" id="name" placeholder="사용자명" class="form-control" value="${user.nickName }" />
					</div>
				</c:if>
				<div class="form-group">
					<input type="hidden" name="roomId" value="${room.roomId }" />
					<button type="submit" class="accent username-submit">채팅시작</button>
				</div>
			</form>
		</div>
		
		<div class="chat-page" class="hidden">
			<div class="btn-group dropend">
				<button class="btn btn-secondary dropdown-toggle" type="button" id="showUserListButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				참가한 유저
				</button>
				<div id="list" class="dropdown-menu" aria-labelledby="showUserListButton"></div>
			</div>
		</div>
		
		<div class="chat-container">
			<div class="chat-header">
				<h2>${room.roomName }</h2>
			</div>
		</div>
		
		<ul class="messageArea">
		
		</ul>
		
		<form id="messageForm" name="messageForm">
			<div class="form-group">
				<div class="input-group clearfix">
					<input type="text" id="message" placeholder="메세지 입력" class="form-control" />
					<button type="submit" class="primary"> 전송</button>
				</div>
			</div>
		</form>
		
	</div>
	
	<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.6.1/sockjs.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
	<script src="/js/socket.js"></script>
</body>
</html>