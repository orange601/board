package com.coinRank.board.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coinRank.board.dao.BoardDao;
import com.coinRank.board.service.BoardService;
import com.coinRank.board.service.BoardServiceImpl;

import mybatis.model.WriteVO;

@Controller
public class BoardController {
	
	@Autowired
    BoardService boardService;
	
	//게시글 리스트 조회
    @RequestMapping(value = "/board/list")
    public String boardList(@RequestParam Map<String, Object> paramMap, Model model) {
 
        
    	//조회 하려는 페이지
    	int startPage = (paramMap.get("startPage")!=null?Integer.parseInt(paramMap.get("startPage").toString()):1);
        
        //한페이지에 보여줄 리스트 수
        int visiblePages = (paramMap.get("visiblePages")!=null?Integer.parseInt(paramMap.get("visiblePages").toString()):10);
        
        
        //일단 전체 건수를 가져온다.
        int totalCnt = boardService.getContentCnt(paramMap);
 
 
        //아래 1,2는 실제 개발에서는 class로 빼준다. (여기서는 이해를 위해 직접 적음)
        //1.하단 페이지 네비게이션에서 보여줄 리스트 수를 구한다.
        BigDecimal decimal1 = new BigDecimal(totalCnt);
        BigDecimal decimal2 = new BigDecimal(visiblePages);
        BigDecimal totalPage = decimal1.divide(decimal2, 0, BigDecimal.ROUND_UP);
 
        int startLimitPage = 0;
        
        //limit 범위를 구하기 위해 계산
        if(startPage==1){
            startLimitPage = 0;
        }else{
            startLimitPage = (startPage-1)*visiblePages;
        }
 
        paramMap.put("start", startLimitPage);
        paramMap.put("end", visiblePages);
        //paramMap.put("end", startLimitPage+visiblePages);
 

        model.addAttribute("startPage", String.valueOf(startPage));//현재 페이지      
        model.addAttribute("totalCnt", totalCnt);//전체 게시물수
        model.addAttribute("totalPage", totalPage);//페이지 네비게이션에 보여줄 리스트 수
        model.addAttribute("boardList", boardService.getContentList(paramMap));//검색
 
        return "board/boardList";
 
    }
	
    //게시글 상세 보기
    @RequestMapping(value = "/board/view")
    public String boardView(@RequestParam Map<String, Object> paramMap, Model model) {
    	model.addAttribute("boardView", boardService.getContentView(paramMap));
        //model.addAttribute("replyList", boardService.getReplyList(paramMap));
        return "board/boardView";
 
    }
    
    //글쓰기(글수정) 페이지로 이동
    @RequestMapping(value = "/board/edit")
    public String boardEdit(HttpServletRequest request, @RequestParam Map<String, Object> paramMap, Model model) {
 
        //Referer 검사
        String Referer = request.getHeader("referer");
 
        if(Referer!=null){//URL로 직접 접근 불가
            if(paramMap.get("id") != null){ //게시글 수정
                if(Referer.indexOf("/board/view")>-1){
                    //정보를 가져온다.
                    model.addAttribute("boardView", boardService.getContentView(paramMap));
                    return "board/boardEdit";
                }else{
                    return "redirect:/board/list";
                }
            }else{ //게시글 등록
                if(Referer.indexOf("/board/list")>-1){
                    return "board/boardEdit";
                }else{
                    return "redirect:/board/list";
                }
            }
        }else{
            return "redirect:/board/list";
        }
    }
    
    //글 쓰기 및 수정
    @RequestMapping(value="/board/save", method=RequestMethod.POST)
    @ResponseBody
    public Object boardSave(@RequestParam Map<String, Object> paramMap) {
 
        //리턴값
        Map<String, Object> retVal = new HashMap<String, Object>();
 
        //패스워드 암호화
        //ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
        String password = paramMap.get("password").toString();//encoder.encodePassword(paramMap.get("password").toString(), null);
        paramMap.put("password", password);
 
        //정보입력
        int result = boardService.regContent(paramMap);
 
        if(result>0){
            retVal.put("code", "OK");
            retVal.put("message", "등록에 성공 하였습니다.");
        }else{
            retVal.put("code", "FAIL");
            retVal.put("message", "등록에 실패 하였습니다.");
        }
 
        return retVal;
 
    }
    
    
    /**
     * @category 글수정시 패스워드확인
     * @see
     * - id와 password에 해당하는 글 개수 return
     * @see
     * - 개수가 1이면 성공 (게시글이 있다는 얘기)
     * @see
     * - 개수가 0이면 실패 (게시글이 없거나 password가 다르다는얘기)
     * */
    @RequestMapping(value="/board/check", method=RequestMethod.POST)
    @ResponseBody
    public Object boardCheck(@RequestParam Map<String, Object> paramMap) {
 
        //리턴값
        Map<String, Object> retVal = new HashMap<String, Object>();
 
        //패스워드 암호화
        //ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
        String password = paramMap.get("password").toString();//encoder.encodePassword(paramMap.get("password").toString(), null);
        paramMap.put("password", password);
 
        //정보입력
        int result = boardService.getBoardCheck(paramMap);
 
        if(result>0){
            retVal.put("code", "OK");
        }else{
            retVal.put("code", "FAIL");
            retVal.put("message", "패스워드를 확인해주세요.");
        }
 
        return retVal;
 
    }
    
    /**
     * @category 글삭제
     * */
    @RequestMapping(value="/board/del", method=RequestMethod.POST)
    @ResponseBody
    public Object boardDel(@RequestParam Map<String, Object> paramMap) {
 
        //리턴값
        Map<String, Object> retVal = new HashMap<String, Object>();
 
        //패스워드 암호화
        //ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
        String password = paramMap.get("password").toString();//encoder.encodePassword(paramMap.get("password").toString(), null);
        paramMap.put("password", password);
 
        //정보입력
        int result = boardService.delBoard(paramMap);
 
        if(result>0){
            retVal.put("code", "OK");
        }else{
            retVal.put("code", "FAIL");
            retVal.put("message", "패스워드를 확인해주세요.");
        }
 
        return retVal;
 
    }
    
    
}
