package com.coinRank.board.dao;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.coinRank.board.vo.Board;
import com.coinRank.board.vo.BoardReply;

@Repository("boardDao")
public class BoardDaoImpl implements BoardDao {
	private final String PACKAGE_PATH = "BoardList.";
	private final String DB_PROPERTIES = "db.properties";
	private final String MYBATIS_CONFIG_FILE = "mybatis/MyBatis-config.xml";
	
    private SqlSession sqlSession = null;
    
    public BoardDaoImpl() {
		InputStream is = null;
		try {
			InputStream propInputStream = Resources.getResourceAsStream(this.DB_PROPERTIES);
			
			Properties prop = new Properties();
			prop.load(propInputStream);
			
			is = Resources.getResourceAsStream(this.MYBATIS_CONFIG_FILE);
			sqlSession = new SqlSessionFactoryBuilder().build(is, prop).openSession(false);
		} catch(Exception e) {
			e.printStackTrace();
			//logger.error("TickerDao에서 DB 접속 중 오류 발생", e);
		}
    }
    
    @Override
    public Board getContentView(Map<String, Object> paramMap) {
    	Board result = new Board();
		try {
			String statement = this.PACKAGE_PATH + "selectContentView";
			result = sqlSession.selectOne(statement, paramMap);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
    }
 
    public void setSqlSession(SqlSession sqlSession){
        this.sqlSession = sqlSession;
    }
    
    @Override
    public int regContent(Map<String, Object> paramMap) {
        return sqlSession.insert("insertContent", paramMap);
    }
    
    @Override
    public int modifyContent(Map<String, Object> paramMap) {
        return sqlSession.update("updateContent", paramMap);
    }
 
    @Override
    public int getContentCnt(Map<String, Object> paramMap) {
        return sqlSession.selectOne("selectContentCnt", paramMap);
    }
    
    @Override
    public List<Board> getContentList(Map<String, Object> paramMap) {
        return sqlSession.selectList("selectContent", paramMap);
    }
 
 
    @Override
    public int regReply(Map<String, Object> paramMap) {
        return sqlSession.insert("insertBoardReply", paramMap);
    }
 
    @Override
    public List<BoardReply> getReplyList(Map<String, Object> paramMap) {
        return sqlSession.selectList("selectBoardReplyList", paramMap);
    }
 
    @Override
    public int delReply(Map<String, Object> paramMap) {
        if(paramMap.get("r_type").equals("main")) {
            //부모부터 하위 다 지움
            return sqlSession.delete("deleteBoardReplyAll", paramMap);
        }else {
            //자기 자신만 지움
            return sqlSession.delete("deleteBoardReply", paramMap);
        }
    }
 
    @Override
    public int getBoardCheck(Map<String, Object> paramMap) {
        return sqlSession.selectOne("selectBoardCnt", paramMap);
    }
 
    @Override
    public int delBoard(Map<String, Object> paramMap) {
        return sqlSession.delete("deleteBoard", paramMap);
    }
 
    @Override
    public boolean checkReply(Map<String, Object> paramMap) {
        int result = sqlSession.selectOne("selectReplyPassword", paramMap);
                
        if(result>0) {
            return true;
        }else {
            return false;
        }
    }
    
    @Override
    public boolean updateReply(Map<String, Object> paramMap) {
        int result = sqlSession.update("updateReply", paramMap);
        
        if(result>0) {
            return true;
        }else {
            return false;
        }
    }
    
	public void close() {
		if(this.sqlSession != null)
			sqlSession.close();
		
		sqlSession = null;
	}
}
