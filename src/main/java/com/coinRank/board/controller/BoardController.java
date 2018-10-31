package com.coinRank.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coinRank.board.dao.BoardDao;

import mybatis.model.WriteVO;

@Controller
public class BoardController {
	@RequestMapping("/board")
	@ResponseBody
	public String boad() {
		BoardDao dao = null;
		try {
			dao = new BoardDao();
			
			WriteVO wvo = new WriteVO();
			wvo.setTitle("TEST TITLE");
			wvo.setWriter("작성자");
			
			dao.insertWrite(wvo);
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		return "board OK";
	}

}
