<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Rich List</title><%-- ${pageContext.request.contextPath} --%>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/lib/jquery-2.1.4.min.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/lib/jqueryui/jquery-ui.css" />
<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/resources/lib/jqgrid5.3.1/css/ui.jqgrid.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/lib/jqgrid5.3.1/js/i18n/grid.locale-kr.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/lib/jqgrid5.3.1/js/jquery.jqGrid.min.js"></script>

<script>var CONTEXT_PATH="<%=request.getContextPath() %>"</script>
<style type="text/css">
.ui-jqgrid .ui-jqgrid-labels th.ui-th-column {
    background-color: #f9f9f9;
    background-image: none;
    font-size:13px;
    font-weight:bold;
}
#paginate{
 	background-color: #f9f9f9;
	background-image: none
}

</style>
<script type="text/javascript">
var reloadTimer;
$(function(){
	var jqGridId = "jqGrid";
	$("#"+jqGridId).jqGrid({
	    url : CONTEXT_PATH+"/listData",
	    datatype: "json",
	    mtype: 'GET',
	    postData  : { 
	    	pagingNum: function(){
		        var currentPage = $('#jqGrid').getGridParam('page');
		    	return currentPage;
		    }
		},
	    rowNum: 100,
	    pager: "#paginate",
	    emptyrecords: "Nothing to display",
	    height:'auto',
	    jsonReader:{
	    		page:"page"//현재페이지
	    		,total:"total"//총 페이지수
	    		,root: "rows"
	    		,repeatitems: false
	    },
	    
	    colModel: [
	        { label: '순위', name: 'index', width: 30},
	        { label: '지갑주소', name: 'address', formatter:addlink, width: 300},
	        { label: '금액', name: 'balance', width: 375},
	        { label: '보유비율', name: 'ofCoins'},
	        { label: '첫입금', name: 'firstIn', width: 180},
	        { label: '마지막입금', name: 'lastIn', width: 180},
	        { label: '입금횟수', name: 'nbIn', width: 100},
	        { label: '첫출금', name: 'firstOut', width: 180},
	        { label: '마지막출금', name: 'lastOut', width: 180},
	        { label: '출금횟수', name: 'nmOut'},
	    ],
	    //width:'auto',
	    //scrollOffset : 0, // 0으로 선언할 경우 Grid 스크롤바 표시 않함
	    pgtext : "<span style=' font-size: 13.5px;font-weight:bold;'> 전체 {1} 페이지 중 {0} 페이지 </span>", 
	    scrollPopUp:true,
	    loadonce: false,
	    viewrecords:true,
	    hoverrows : false,
	    ajaxGridOptions : { cache: false },
	    gridview: true,
        cmTemplate: { title: false, sortable:false 	},// label 클릭시 자동 정렬 false 
	    autowidth:true,
	    //loadtext: "Refreshing Grid",
	    //shrinkToFit: false,
	    //forceFit: true,
	    //caption: "Rich List",
	    //rownumbers:true,
	    //repeatitems: false,
	    //toppager: true,viewrecords:true, pgbuttons:true, pginput: true,
	    //rowList:[100,10,20,30,50],
	    //reload : true,
		beforeSelectRow : function(){return false;},//beforeSelectRow OFF
	    loadComplete : function(data){
	    	 // 그리드 데이터 총 갯수
            var allRowsInGrid = $("#"+jqGridId).jqGrid('getGridParam','records');
	    	 if( allRowsInGrid <= 0){
// 	    		 if( confirm("https://bitinfocharts.com-사이트접속이 불가능 합니다. 재 접속 하시겠습니까?") ){
// 	    			 location.reload();
// 	    		 } 
	    	 }
	    	 
	    	 setGridResize();
	    }	    
		,onInitGrid: function () {
			//header 마우스 cursor pointer disabled
			$(".ui-jqgrid .ui-jqgrid-sortable").css("cursor","default");
			
        	$(this.grid.hDiv)
            	.find(".ui-jqgrid-labels th.ui-th-column")
        		.off("mouseenter mouseleave");
        }
		,loadError : function(jqXHR, textStatus, errorThrown){
			if( jqXHR.status == 500 ){
	        	if(reloadTimer){
	        		clearTimeout(reloadTimer);
	        	}else{
	        		alert("사이트 접속 오류로 인하여 5초 후 다시 접속합니다.");
	        		reloadTimer = setTimeout(function() {	$('#jqGrid').trigger('reloadGrid');}, 7000);
	        	}
	        }
	        console.log('HTTP status code: ' + jqXHR.status + '\n' +
	                'textStatus: ' + textStatus + '\n' +
	                'errorThrown: ' + errorThrown);
	        console.log('HTTP message body (jqXHR.responseText): ' + '\n' + jqXHR.responseText);
		}
		,onPaging : function(){
			var totalPage = $("#"+jqGridId).jqGrid('getGridParam','records');
			var inputPageNum = $("input.ui-pg-input").val();
			
			if( !isNumber(inputPageNum) ){
				alert("숫자만 입력 가능합니다.");
				return;
			}
			
			if( inputPageNum > totalPage){
				alert("전체 페이지 수 보다 높은 수를 입력하셨습니다.");
				$("input.ui-pg-input").val("");
				return; 
			}
		}
		
	});
});

$(window).resize(function() {
	setGridResize();
});

function setGridResize(){
	$("#jqGrid").setGridWidth($(window).width() - 10);
	$("#jqGrid").jqGrid('setGridHeight',$(window).innerHeight() - 80);
}

function addlink(cellvalue, options, rowObject){
	var linkUrl = "<a href='https://www.blockchain.com/btc/address/"+cellvalue+"'>"+cellvalue+"</a>";
	return linkUrl;
}

function isNumber(s) {
	  s += ''; // 문자열로 변환
	  s = s.replace(/^\s*|\s*$/g, ''); // 좌우 공백 제거
	  if (s == '' || isNaN(s)) return false;
	  return true;
	}
</script>
</head>
<body>
	<table id="jqGrid"></table>
	<div id="paginate"></div>
</body>
</html>