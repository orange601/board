package com.coinRank.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.coinRank.board.vo.Board;
import com.coinRank.board.vo.BoardReply;

public interface BoardService {
    int regContent(Map<String, Object> paramMap);
    int getContentCnt(Map<String, Object> paramMap);
    List<Board> getContentList(Map<String, Object> paramMap);
    Board getContentView(Map<String, Object> paramMap);
    int regReply(Map<String, Object> paramMap);
    List<BoardReply> getReplyList(Map<String, Object> paramMap);
    int delReply(Map<String, Object> paramMap);
    int getBoardCheck(Map<String, Object> paramMap);
    int delBoard(Map<String, Object> paramMap);
    boolean checkReply(Map<String, Object> paramMap);
    boolean updateReply(Map<String, Object> paramMap);
}
