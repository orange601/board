//PagingHelper.gotoPage(1);
//PagingHelper.pagingHtml( totalRecord )
//$("#paging").append(PagingHelper.pagingHtml(1001)); //1001 전체리스트 개수 
// http://huskdoll.tistory.com/792
//https://m.blog.naver.com/PostView.nhn?blogId=javaking75&logNo=220080243500&proxyReferer=https%3A%2F%2Fwww.google.co.kr%2F
var PagingHelper = {
	'data' :{
        pageSize : 5 // 페이지 사이즈 (화면 출력 페이지 수 (1|2|3|4|5) )
        ,maxListCount : 10  // (보여질)최대 리스트 수 (한페이지 출력될 항목 갯수)
	},
	'pagingHtml' : function(pTotalCnt, cPage, pTotalPageCnt) {
        var _this = this;
        //현재 페이지 번호
        var currentPage = cPage;//_.data.currentPage
        //전체 글 개수
        var totalCnt = pTotalCnt;
        //전체 페이지 개수 페이지 출력 범위 (1|2|3|4|5)
        var totalPageCnt = pTotalPageCnt;
        //현재 블럭 구하기 
        var n_block = Math.ceil(currentPage / _this.data.pageSize); 
        //페이징의 시작페이지와 끝페이지 구하기
        var s_page = (n_block - 1) * _this.data.pageSize + 1; // 현재블럭의 시작 페이지
        var e_page = n_block * _this.data.pageSize; // 현재블럭의 끝 페이지
        
        var sb='';
        var sbTemp ='';
        
        //전체 페이지 개수가 0 이면 return;
        if (totalCnt == 0) return "";
          
        // 블럭의 페이지 목록 및 현재페이지 강조
        for (var j = s_page; j <= e_page; j++) {
            if (j > totalPageCnt ) break;                         
            if(j == currentPage) {
                sbTemp += "<li class='selected'>["+j+"]</li>";
            } else {
                sbTemp += "<li onclick='PagingHelper.gotoPage("+j+");'>["+j+"]</li>";        
            }
        }            

        // 이전페이지 버튼
        sb = "<ul>"
        if(currentPage > s_page || totalCnt > _this.data.maxListCount && s_page > 1){
            sb += "<li class='first' onclick='PagingHelper.gotoPage(1);'>처음</li >"
            sb += "<li class='previous' onclick='PagingHelper.gotoPage(" + (currentPage - 1) + ");'>이전</li>"    
        }
        
        // 현재블럭의 페이지 목록
        sb += sbTemp
        
        // 다음페이지 버튼
        if(currentPage < totalPageCnt ){
            sb += "<li class='next' onclick='PagingHelper.gotoPage(" + (currentPage + 1) + ");'>다음</li>"
            sb += "<li class='last' onclick='PagingHelper.gotoPage(" + (totalPageCnt) +");'>마지막</li >"            
        }
        sb += "</ul>";
                    
        return sb;
	},
	gotoPage:function(pageNum){
		$('#startPage').val(pageNum);
	    $('#visiblePages').val(10);
	    $("#frmSearch").submit();
	}
}