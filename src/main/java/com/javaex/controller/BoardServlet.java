package com.javaex.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.BoardDao;
import com.javaex.dao.BoardDaoImpl;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;

@WebServlet("/board")
//2024년 01월 19일 작성자 : 노신영
public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String actionName = request.getParameter("a");
		System.out.println("board:" + actionName);

		if ("list".equals(actionName)) {
			// 리스트 가져오기
			BoardDao dao = new BoardDaoImpl();
			List<BoardVo> list1 = dao.getList();
	
			//페이징 필수 변수 선언하기 
			int totalRecord = 0; //전체레코드수
			int numPerPage = 10; // 페이지당 레코드 수 
			int pagePerBlock = 10; //블럭당 페이지수 

			int totalPage = 0; //전체 페이지 수
			int totalBlock = 0; //전체 블럭수 

			int nowPage = 1; // 현재페이지
			int nowBlock = 1; //현재블럭

			int start = 0; //디비의 select 시작번호
			int end = 0; //시작번호로 부터 가져올 select 갯수

			totalRecord = list1.size(); 
			totalPage = (int) Math.ceil((double) totalRecord / numPerPage); //전체페이지수 
			nowBlock = (int) Math.ceil((double) nowPage / pagePerBlock); //현재블럭 계산
			totalBlock = (int) Math.ceil((double) totalPage / pagePerBlock); //전체블럭계산
			
			if (request.getParameter("nowBlock") != null) {
				nowBlock = Integer.parseInt(request.getParameter("nowBlock"));
			}
			
			if (request.getParameter("nowPage") != null) {
				nowPage = Integer.parseInt(request.getParameter("nowPage"));
			}
			
			start = (nowPage * numPerPage) - numPerPage + 1;
			end = start + numPerPage - 1;

			//페이징 처리 
			int pageStart = (nowBlock - 1) * pagePerBlock + 1; //하단 페이지 시작번호
			int pageEnd = ((pageStart + pagePerBlock) <= totalPage) ? (pageStart + pagePerBlock) : totalPage + 1; //하단 페이지 끝번호
			List<Integer> pageNum = new ArrayList<Integer>(); 
			
			  for (; pageStart < pageEnd; pageStart++) {
				 pageNum.add(pageStart);				 
			 }

			// 리스트 화면에 보내기
			List<BoardVo> list = dao.getList(start, end);
			request.setAttribute("list", list);
			request.setAttribute("pageNum", pageNum);
			request.setAttribute("nowBlock", nowBlock);
			request.setAttribute("totalBlock", totalBlock);
				
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/list.jsp");
			rd.forward(request, response); 
		} 
		
		else if ("search".equals(actionName)) {
			// 리스트 가져오기
			BoardDao dao = new BoardDaoImpl();
			List<BoardVo> list = dao.getList();
			
			//페이징 필수 변수 선언하기 
			int totalRecord = 0; //전체레코드수
			int numPerPage = 10; // 페이지당 레코드 수 
			int pagePerBlock = 10; //블럭당 페이지수 

			int totalPage = 0; //전체 페이지 수
			int totalBlock = 0; //전체 블럭수 

			int nowPage = 1; // 현재페이지
			int nowBlock = 1; //현재블럭

			int start = 0; //디비의 select 시작번호
			int end = 0; //시작번호로 부터 가져올 select 갯수

			int listSize = list.size(); //현재 읽어온 게시물의 수
			
			String keyField = request.getParameter("keyField");
			String keyWord = request.getParameter("keyWord");
			
			List<BoardVo> list1 = dao.getList(keyField, keyWord, start, listSize);
			
			totalRecord = list1.size();
			totalPage = (int) Math.ceil((double) totalRecord / numPerPage); //전체페이지수 
			nowBlock = (int) Math.ceil((double) nowPage / pagePerBlock); //현재블럭 계산
			totalBlock = (int) Math.ceil((double) totalPage / pagePerBlock); //전체블럭계산
			
			if (request.getParameter("nowBlock") != null) {
				nowBlock = Integer.parseInt(request.getParameter("nowBlock"));
			}
			
			if (request.getParameter("nowPage") != null) {
				nowPage = Integer.parseInt(request.getParameter("nowPage"));
			}
			
			start = (nowPage * numPerPage) - numPerPage + 1;
			end = start + numPerPage - 1;
			
			//페이징 처리 
			int pageStart = (nowBlock - 1) * pagePerBlock + 1; //하단 페이지 시작번호
			int pageEnd = ((pageStart + pagePerBlock) <= totalPage) ? (pageStart + pagePerBlock) : totalPage + 1; //하단 페이지 끝번호
			List<Integer> pageNum = new ArrayList<Integer>(); 
						
			for (; pageStart < pageEnd; pageStart++) {
				 pageNum.add(pageStart);				 
			 }

			// 리스트 화면에 보내기
			List<BoardVo> list2 = dao.getList(keyField, keyWord, start, end);
			request.setAttribute("list", list2);
			request.setAttribute("pageNum", pageNum);
			request.setAttribute("nowBlock", nowBlock);
			request.setAttribute("totalBlock", totalBlock);
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/list.jsp");
			rd.forward(request, response); 
		} 
		
		else if ("read".equals(actionName)) {
			// 게시물 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			BoardDao dao = new BoardDaoImpl();
			BoardVo boardVo = dao.getBoard(no);

			System.out.println(boardVo.toString());

			// 게시물 화면에 보내기
			request.setAttribute("boardVo", boardVo);
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");
		} else if ("modifyform".equals(actionName)) {
			// 게시물 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			BoardDao dao = new BoardDaoImpl();
			BoardVo boardVo = dao.getBoard(no);

			// 게시물 화면에 보내기
			request.setAttribute("boardVo", boardVo);
			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyform.jsp");
		} else if ("modify".equals(actionName)) {
			// 게시물 가져오기
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int no = Integer.parseInt(request.getParameter("no"));
			
			BoardVo vo = new BoardVo(no, title, content);
			BoardDao dao = new BoardDaoImpl();
			
			dao.update(vo);
			
			WebUtil.redirect(request, response, "/mysite/board?a=list");
		} else if ("writeform".equals(actionName)) {
			// 로그인 여부체크
			UserVo authUser = getAuthUser(request);
			if (authUser != null) { // 로그인했으면 작성페이지로
				WebUtil.forward(request, response, "/WEB-INF/views/board/writeform.jsp");
			} else { // 로그인 안했으면 리스트로
				WebUtil.redirect(request, response, "/mysite/board?a=list");
			}

		} else if ("write".equals(actionName)) {
			UserVo authUser = getAuthUser(request);

			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			int userNo = authUser.getNo();
			System.out.println("userNo : ["+userNo+"]");
			System.out.println("title : ["+title+"]");
			System.out.println("content : ["+content+"]");

			BoardVo vo = new BoardVo(title, content, userNo);
			BoardDao dao = new BoardDaoImpl();
			dao.insert(vo);

			WebUtil.redirect(request, response, "/mysite/board?a=list");

		} else if ("delete".equals(actionName)) {
			int no = Integer.parseInt(request.getParameter("no"));

			BoardDao dao = new BoardDaoImpl();
			dao.delete(no);

			WebUtil.redirect(request, response, "/mysite/board?a=list");

		} else {
			WebUtil.redirect(request, response, "/mysite/board?a=list");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	// 로그인 되어 있는 정보를 가져온다.
	protected UserVo getAuthUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");

		return authUser;
	}

}
