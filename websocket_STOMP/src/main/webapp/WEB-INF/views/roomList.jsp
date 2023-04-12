<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.3/jquery.min.js"></script>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
	crossorigin="anonymous">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4"
	crossorigin="anonymous"></script>
<style type="text/css">
a {
	text-decoration: none;
}

#table {
	margin-top: 40px;
	width: 600px;
}

h1 {
	margin-top: 40px;
}
</style>
<script type="text/javascript">
	function createRoom(){
		console.log("채팅방 생성");
		var name = $("#roomName").val();
		console.log(name);
		
		if(name== ""){
			alert("방이름은 필수 입니다.");
			return false;
		}
		return true;
	}
</script>
</head>
<body>

	<div class="container">
		<h1>채팅방 리스트</h1>


		<table id="table" class="table table-hover">
			<tr>
				<th>채팅방명</th>
				<th>참여인원</th>
			</tr>

			<c:forEach var="room" items="${list }">
				<tr>
					<td><a href="/chat/room?roomId=${room.roomId }"
						onclick="return chkRoomUserCnt(this.getAttribute('roomid'));">${room.roomId }</a>
					</td>
				<td><span class="badge bg-primary rounded-pill">${room.userCount }</span></td>
				</tr>
				
			</c:forEach>

		</table>
		<button type="button" class="btn btn-info" data-bs-toggle="modal"
			data-bs-target="#exampleModal">채팅방 생성</button>

		<!-- Modal -->
		<div class="modal fade" id="exampleModal" tabindex="-1"
			aria-labelledby="exampleModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h1 class="modal-title fs-5" id="exampleModalLabel">채팅방 생성</h1>
						<button type="button" class="btn-close" data-bs-dismiss="modal"
							aria-label="Close"></button>
					</div>
					
					<form action="/chat/createroom" method="post" onsubmit="return createRoom()">
						<div class="modal-body">
							<div class="mb-3">
								<label for="roomName" class="col-form-label">방이름</label>
								<input type="text" name="roomname" id="roomName" />
							</div>
						</div>
					
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary"
							data-bs-dismiss="modal">Close</button>
						<input type="submit" class="btn btn-primary" value="방 생성하기" />
						
					</div>
					</form>
					
				</div>
			</div>
		</div>

	</div>
	<h1>${list }</h1>
	
	
</body>
</html>