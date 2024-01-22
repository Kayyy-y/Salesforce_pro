<<<<<<< HEAD
package com.javaex.dao;

import org.junit.jupiter.api.Test;

import com.javaex.vo.BoardVo;

//2024년 01월 22일 작성자 : 박형준
class BoardDaoImplTest {

	@Test
	void testGetList() {
		BoardDao dao = new BoardDaoImpl();
		BoardVo vo = new BoardVo();
		vo.setTitle("getlist");
		vo.setUserName("테스트");
		vo.setContent("getlist 테스트 합니다.");
		dao.getList("title", "%제목%", 1, 10);
	}

	@Test
	void testGetBoard() {
		BoardDao dao = new BoardDaoImpl();
		dao.getBoard(10);
	}

	@Test
	void testInsert() {
		BoardDao dao = new BoardDaoImpl();
		BoardVo vo = new BoardVo();
		vo.setTitle("insert");
		vo.setUserNo(0);
		vo.setContent("insert 테스트 합니다.");
		dao.insert(vo);
	}

	@Test
	void testDelete() {
		BoardDao dao = new BoardDaoImpl();
		dao.delete(1);
	}

	@Test
	void testUpdate() {
		BoardDao dao = new BoardDaoImpl();
		BoardVo vo = new BoardVo();
		vo.setTitle("update");
		vo.setNo(1);
		vo.setContent("update 테스트 합니다.");
		dao.update(vo);
	}

}
=======
package com.javaex.dao;

import org.junit.jupiter.api.Test;

import com.javaex.vo.BoardVo;

//2024년 01월 22일 작성자 : 노신영 
class BoardDaoImplTest {

	@Test
	void testGetList() {
		BoardDao dao = new BoardDaoImpl();
		BoardVo vo = new BoardVo();
		vo.setTitle("getlist");
		vo.setUserName("테스트");
		vo.setContent("getlist 테스트 합니다.");
		dao.getList();
		dao.getList(1, 10);
		dao.getList("title", "%제목%", 1, 10);
	}

	@Test
	void testGetBoard() {
		BoardDao dao = new BoardDaoImpl();
		dao.getBoard(10);
	}

	@Test
	void testInsert() {
		BoardDao dao = new BoardDaoImpl();
		BoardVo vo = new BoardVo();
		vo.setTitle("insert");
		vo.setUserNo(0);
		vo.setContent("insert 테스트 합니다.");
		dao.insert(vo);
	}

	@Test
	void testDelete() {
		BoardDao dao = new BoardDaoImpl();
		dao.delete(1);
	}

	@Test
	void testUpdate() {
		BoardDao dao = new BoardDaoImpl();
		BoardVo vo = new BoardVo();
		vo.setTitle("update");
		vo.setNo(1);
		vo.setContent("update 테스트 합니다.");
		dao.update(vo);
	}

}
>>>>>>> refs/remotes/origin/main
