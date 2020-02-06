<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<fmt:requestEncoding value="utf-8"/>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<!-- Custom styles for this page -->
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link href="${pageContext.request.contextPath }/resources/vendor/datatables/dataTables.bootstrap4.min.css" rel="stylesheet">
  
<!-- Page Heading -->
<h1 class="h3 mb-2 text-gray-800">원재료 재고관리(LOT)</h1>

<form class="d-none d-sm-inline-block form-inline mr-auto ml-md-3 my-2 my-md-0 mw-100 navbar-search" style="float:right;">
	<div class="input-group" style="margin:30px;">
		<div class="input-group-append">
			<button class="btn btn-primary" type="button" id="quality-insert-button">
				  재고 수정하기 
			</button> &nbsp;&nbsp;&nbsp;&nbsp;
			<button class="btn btn-primary" type="button" id="quality-search-button">
				  재고 상세 검색하기 &nbsp;&nbsp;&nbsp; <i class="fas fa-search fa-sm"></i>
			</button> 
		</div>
	</div>
</form>

<br />

<!-- DataTales Example -->
<div class="card shadow mb-4" style="clear:both;">
	<div class="card-header py-3">
	<h6 class="m-0 font-weight-bold text-primary">조회 결과</h6>
</div>
  
  <div class="card-body">
    <div class="table-responsive">
		<table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
		  <thead>
		    <tr>
		     <th>원재료번호</th>
		     <th>원재료이름</th>
		     <th>규격</th>
		     <th>총 수량</th>
		    </tr>
		  </thead>
		  
		  
		  <tbody>
	      	<c:forEach items="${rmList}" var="rm" varStatus="vs">
		        <tr>
		          <td>${rm.RM_NO}</td>
		          <td>${rm.RM_NAME}</td>
		          <td>${rm.STANDARD}</td>      
		          <td>${rm.RM_SUM}</td>
		        </tr>
	        </c:forEach>
		  </tbody>
		  
		</table>
    </div>
  </div>
</div>
          
          
<!-- Modal -->
<div class="modal" tabindex="-1" role="dialog" id="myModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
	        <div class="modal-header">
	            <h5 class="modal-title controll-title"></h5>
	            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	            <span aria-hidden="true">&times;</span>
	            </button>
	        </div>
	        <div class="modal-body controll-modal-body">
	            <!-- <p>Modal body text goes here.</p> -->
	        </div>
        </div>
    </div>
</div>

  
<script>
	$("#quality-insert-button").click(function(){
	 $('.controll-modal-body').load("${pageContext.request.contextPath}/stock/rm/modalRmInsert.do",function(){
	        $('#myModal').modal({backdrop: 'static', keyboard: false});
	        $('#myModal').modal({show:true});
	        $(".modal-backdrop.in").css('opacity', 0.4);
	        $(".controll-title").html("");
	        $(".controll-title").html("재고 수정하기");
		});
	});
	
	$("#quality-search-button").click(function(){
	    $('.controll-modal-body').load("${pageContext.request.contextPath}/stock/rm/modalRmSearch.do",function(){
	        $('#myModal').modal({backdrop: 'static', keyboard: false});
	        $('#myModal').modal({show:true});
	        $(".modal-backdrop.in").css('opacity', 0.4);
	        $(".controll-title").html("");
	        $(".controll-title").html("재고 상세 검색하기");
	    });
	});
</script>

<style>
#myModal{
	z-index: 1060;
}
</style>

<!-- Page level plugins -->
<script src="${pageContext.request.contextPath }/resources/vendor/datatables/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath }/resources/vendor/datatables/dataTables.bootstrap4.min.js"></script>

<!-- Page level custom scripts -->
<script src="${pageContext.request.contextPath }/resources/js/demo/datatables-demo.js"></script>


<jsp:include page="/WEB-INF/views/common/footer.jsp"/>






