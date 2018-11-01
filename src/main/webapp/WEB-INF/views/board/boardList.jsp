<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>게시판</title>
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/common/paging/paging.js"></script>
        <script type="text/javascript" src="http://code.jquery.com/jquery-1.11.3.js"></script>
        <style>
            #paging > ul {
        list-style-type:none;
}
    
    #paging > ul li {
        font-size: 1.1em;
        display:inline-block;
        cursor:pointer;
        padding: 0 3px;
}
    #paging > ul > li:hover {
        /*background: #fbfbfb;*/
        /*font-weight:bold;*/
        color: #0080ff;
    }
    

    #paging > ul > li.selected {
        color: #0080ff;
        font-size:1.2em;
        font-weight:bold;
        cursor:default;

}
        </style>
        <script type="text/javascript">
            $(document).ready(function(){
                 
                //--페이지 셋팅
                var totalPage = ${totalPage}; //전체 페이지  (1|2|3|4|5)
                var startPage = ${startPage}; //현재 페이지
                var totalCnt = ${totalCnt}; //전체 글 개수
                 
                $("#paging").append(PagingHelper.pagingHtml(totalCnt, startPage, totalPage));
                
                //글 자세히 보기 페이지로 이동
                $("a[name='subject']").click(function(){
                    location.href = "/board/view?id="+$(this).attr("content_id");
                });
                 
                // 글쓰기페이지로 이동
                $("#write").click(function(){
                    location.href = "/board/edit";
                });
            });
        </script>
        <style>
            .mouseOverHighlight {
                   border-bottom: 1px solid blue;
                   cursor: pointer !important;
                   color: blue;
                   pointer-events: auto;
                }
        </style>
    </head>
    <body>
        <form class="form-inline" id="frmSearch" action="/board/list">
            <input type="hidden" id="startPage" name="startPage" value=""><!-- 페이징을 위한 hidden타입 추가 -->
            <input type="hidden" id="visiblePages" name="visiblePages" value=""><!-- 페이징을 위한 hidden타입 추가 -->
            <div align="center">
                <table width="1200px">
                    <tr>
                        <td align="right">
                            <button type="button" id="write" name="write">글 작성</button>
                        </td>
                    </tr>
                </table>
                <table border="1" width="1200px">
                    <tr>
                        <th width="50px">
                            No
                        </th>
                        <th width="850px">
                            제목
                        </th>
                        <th width="100px">
                            작성자
                        </th>
                        <th width="200px">
                            작성일
                        </th>
                    </tr>
                    <c:choose>
                        <c:when test="${fn:length(boardList) == 0}">
                            <tr>
                                <td colspan="4" align="center">
                                   	조회결과가 없습니다.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="boardList" items="${boardList}" varStatus="status">
                                <tr>
                                    <td align="center">${boardList.id}</td>
                                    <td>
                                        <a name="subject" class="mouseOverHighlight" content_id="${boardList.id}">${boardList.subject}</a>
                                    </td>
                                    <td align="center">${boardList.writer}</td>
                                    <td align="center">${boardList.register_datetime}</td>
                                </tr>
                            </c:forEach>
                        </c:otherwise> 
                    </c:choose>
                </table>
                <br>
                <div id="pagination"></div>
                <div id="paging"></div>
            </div>
        </form>
    </body>
</html>