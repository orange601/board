package com.coinRank.board.dao;

import java.io.InputStream;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import mybatis.model.WriteVO;

public class daoBack {
	private final String PACKAGE_PATH = "BoardList.";
	private final String DB_PROPERTIES = "db.properties";
	private final String MYBATIS_CONFIG_FILE = "mybatis/MyBatis-config.xml";
	
	private SqlSession sqlSession = null;
	
	
	public int insertWrite(WriteVO wvo){
		int result = -1;
		try {
			String statement = this.PACKAGE_PATH + "boardInsert";
			result = sqlSession.insert(statement, wvo);
			if(result == 1) {
				sqlSession.commit();
			} else {
				sqlSession.rollback();
			}
		} catch(Exception e) {
			e.printStackTrace();
			//logger.error("DAO에서 Upsert 중 오류 발생", e);
		}
		return result;
	}
	
	
	public daoBack() {
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
	
	public void close() {
		if(this.sqlSession != null)
			sqlSession.close();
		
		sqlSession = null;
	}

}
