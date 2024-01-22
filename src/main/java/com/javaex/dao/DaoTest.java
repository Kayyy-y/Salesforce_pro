package com.javaex.dao;

import com.javaex.vo.BoardVo;

public class DaoTest {

	public static void main(String[] args) {
	  BoardDao dao = new BoardDaoImpl();
		
		BoardVo vo = new BoardVo();
		vo.setTitle("제목이름");
		vo.setUserName("글쓴이이름");
		vo.setContent("테스트 합니다." );
		
		int count = dao.insert(vo);
	  System.out.println(count + "건 insert 완료");
	  
    System.out.println(dao.getList());
	}
	
}
