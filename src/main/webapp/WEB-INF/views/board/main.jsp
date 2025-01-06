<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Spring MVC04</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
  <script type="text/javascript">
  
  var csrfHeaderName = "${_csrf.headerName}";
  var csrfTokenValue = "${_csrf.token}";
  
  
  // JQuery => javascript lib, $(document).ready(function(){}) : jQuery문법의 시작
  	$(document).ready(function(){
  		loadList();
  	});
  
  function loadList(){
	  //서버와 통신 : 게시판 리스트 가져오기 -> 비동기식 전송 방식
	  $.ajax({
		  //url: "boardList.do",
		  url: "board/all",
		  type: "get",
		  dataType:"json", //서버로 부터 응답할 타입
		  success : makeView, //콜백 함수
		  error:function(){alert("error");}
	  });
  
	 //         0      1      2 ---->
	// data=[{   },{    },{    },,, ]

  	function makeView(data){
		//alert('## = '+data[0].title); //잉
		
  		 var listHtml="<table class='table table-bordered'>";
 		 listHtml+="<tr>";
 		 listHtml+="<td>번호</td>";  		 
 		 listHtml+="<td>제목</td>";  	
 		 listHtml+="<td>작성자</td>";  	
 		 listHtml+="<td>작성일</td>";  	
 		 listHtml+="<td>조회수</td>";  	
 		 listHtml+="</tr>";
    	 // $.each (반복할 데이터(data), ->데이터의 함수(인덱스(data[0]), 데이터(data[0].xx)))
 		 $.each(data, function(index, obj){ // 앞에는 인덱스를 받고, 뒤에는 데이터를 받는다
 	  		 listHtml+="<tr>";
 	  		 listHtml+="<td>"+obj.idx+"</td>";
 	  		 listHtml+="<td id='t"+obj.idx+"'><a href='javascript:goContent("+obj.idx+")'>"+obj.title+"</a></td>";
       	 	 listHtml+="<td>"+obj.writer+"</td>";
       	 	 listHtml+="<td>"+obj.indate.split(' ')[0]+"</td>";
       	 	 listHtml+="<td id='cnt"+obj.idx+"'>"+obj.count+"</td>";
 	  		 listHtml+="</tr>";
 	  		 
 	  		 //상세보기
 	  		 listHtml+="<tr id='c"+obj.idx+"' style='display:none'>";
        	 listHtml+="<td>내용</td>";
        	 listHtml+="<td colspan='4'>";
        	 listHtml+="<textarea id='ta"+obj.idx+"' readonly rows='7' class='form-control'></textarea>";
        	 //자기 자신 글만 수정, 삭제 처리
        	 // {mvo.memID}에 "" 안붙이면 312 =="312"이라 에러처리됨
        	 //console.log('${mvo.memID} = '+${mvo.memID}+" obj.memID = "+obj.memID);
        	 if("${mvo.memID}"==obj.memID){
        		 console.log('1111');
        		 listHtml+="<br/>";
            	 listHtml+="<span id='ub"+obj.idx+"'><button class='btn btn-success btn-sm' onclick='goUpdateForm("+obj.idx+")'>수정화면</button></span>&nbsp;";
            	 listHtml+="<button class='btn btn-warning btn-sm' onclick='goDelete("+obj.idx+")'>삭제</button>";	 
        	 }else{
        		 console.log('222222');
        		 listHtml+="<br/>";
        	 }
        	         	 
        	 
        	 listHtml+="</td>";
        	 listHtml+="</tr>";
 		 });
    	 
    	 // 로그인을 해야 보이는 부분
    	 if(${!empty mvo}){
	 		 listHtml+="<tr>";
	    	 listHtml+="<td colspan='5'>";
	    	 listHtml+="<button class='btn btn-primary btn-sm' onclick='goForm()'>글쓰기</button>";
	    	 listHtml+="</td>";
	    	 listHtml+="</tr>";
	 		 listHtml+="</table>";
    	 }
 		 $("#view").html(listHtml);
 		 goList();
  	}
	  
  }//loadList
  
  function goForm(){
	  $("#view").css("display","none"); //감추기
	  $("#wform").css("display","block"); //보이기
  }
  
  function goList(){
	  $("#view").css("display","block"); //보이기
	  $("#wform").css("display","none"); //감추기
	  
  }
  
  function goInsert(){
	  
	  //var title=$("#title").val();
	  //var content=$("#content").val();
	  //var writer=$("#writer").val(); 
	  
	  var fData=$("#frm").serialize(); // serialize : form데이터 안의 모든 파라미터를 직렬화 시켜서 한줄로 한번에 가지고 올 수 있음
	  //alert(fData); //title=111&content=1111&writer=222
	  
	  $.ajax({
		  //url: "boardInsert.do",
		  url: "board/new",
		  type:"post",
		  data : fData, // 폼에 저장된 데이터들이 넘어감, title, content,. writer
		  beforeSend: function(xhr){
 			 xhr.setRequestHeader(csrfHeaderName, csrfTokenValue)
 		  },
		  success : loadList,
		  error: function(){alert("error");}
	  });
	  
	  //폼 초기화
	  //$("#title").val("");
	  //$("#content").val("");
	  //$("#writer").val("");
 	 $("#fclear").trigger("click"); // trigger을 하면 위의 각각의 번수마다 초기화하는걸 한번에 끝내줄 수 있다.
  } 
  
  function goContent(idx){ // idx=11, 10, 9
	  
 	  if($("#c"+idx).css("display")=="none"){    		 
 		 $.ajax({
			 url : "board/"+idx,
			 type : "get",    			 
			 dataType : "json",
			 success : function(data){ // data={  "content": ~~~  }
				 $("#ta"+idx).val(data.content);
			 },
			 error : function() { alert("error"); }    			 
		 });    	
	     $("#c"+idx).css("display","table-row"); // 보이게
 	     $("#ta"+idx).attr("readonly",true);
 	    //조회수 증가
 		 $.ajax({
			 //url : "boardCount.do",
			 url : "board/count/"+idx,
			 type : "put",    	
			 //data : {"idx":idx}, rest방식에서는 {키:값}문자열을 json형태로 보내주기+contentType에 전달할 타입 적어줘야함
			 dataType : "json",
			 beforeSend: function(xhr){
    			 xhr.setRequestHeader(csrfHeaderName, csrfTokenValue)
    		 },
			 success : function(data){
				 $("#cnt"+idx).text(data.count);
			 },    			 
			 error : function(){ alert("조회수 카운팅 error"); }
		 });

 	  }else{
 		 $("#c"+idx).css("display","none"); // 감추게
 	 }
  }//goContent
  
  function goDelete(idx){
	  $.ajax({
		  //url:"boardDelete.do",
		  url:"board/"+idx,
		  type: "get",
		  data: {"idx":idx},
		  beforeSend: function(xhr){
 			 xhr.setRequestHeader(csrfHeaderName, csrfTokenValue)
 		  },
		  success: loadList,
		  error : function(){ alert("goDelete error"); }
	  });
  }
  
  function goUpdateForm(idx){
	  $("#ta"+idx).attr("readonly",false);
      
	  
	  //제목 변경
  	  var title = $("#t"+idx).text(); // 기존 제목!
	  var newInput="<input type='text' id='nt"+idx+"' class='form-control' value='"+title+"'/>";
	  $("#t"+idx).html(newInput);
      
	  //새로운 버튼 부여
	  var newButton="<button class='btn btn-primary btn-sm' onclick='goUpdate("+idx+")'>수정</button>";
      $("#ub"+idx).html(newButton); // 3
  }
  
  function goUpdate(idx){
	  var title=$("#nt"+idx).val();
      var content=$("#ta"+idx).val();
      
      $.ajax({
    	  //url: "boardUpdate.do",
    	  url: "board/update",
    	  type: "put",
    	  //data: {"idx":idx, "title":title,"content":content},
    	  contentType:'application/json;charset=utf-8',
    	  data : JSON.stringify({"idx":idx,"title":title,"content":content}),
    	  beforeSend: function(xhr){
 			 xhr.setRequestHeader(csrfHeaderName, csrfTokenValue)
 		  },
    	  success: loadList,
		  error : function(){ alert("update error"); }
      });

  }
  </script>

</head>
<body>
<div class="container">
<jsp:include page="../common/header.jsp"/>      
  <h2>회원 게시판</h2>
  <div class="panel panel-default">
    <div class="panel-heading">BOARD</div>
    <div class="panel-body" id="view">Panel Content</div>
    <div class="panel-body" id="wform" style="display: none">
	    <form id="frm">
	      <input type="hidden" name="memID" id="memID" value="${mvo.memID}"/>

	      <table class="table">
	         <tr>
	           <td>제목</td>
	           <td><input type="text"  id="title" name="title" class="form-control"/></td>
	         </tr>
	         <tr>
	           <td>내용</td>
	           <td><textarea rows="7" class="form-control" id="content" name="content"></textarea> </td>
	         </tr>
	         <tr>
	           <td>작성자</td>
	           <td><input type="text" id="writer" name="writer" class="form-control" value="${mvo.memID}" readonly="readonly"/></td>
	         </tr>
	         <tr>
	           <td colspan="2" align="center">
                   <button type="button" class="btn btn-success btn-sm" onclick="goInsert()">등록</button>
	               <button type="reset" class="btn btn-warning btn-sm" id="fclear">취소</button>
	           	   <button type="button" class="btn btn-info btn-sm" onclick="goList()">리스트</button>
	           </td>
	         </tr>
	      </table>
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>   
	     </form>
    </div>
    
    <div class="panel-footer">인프런_스프1</div>
  </div>
</div>

</body>
</html>