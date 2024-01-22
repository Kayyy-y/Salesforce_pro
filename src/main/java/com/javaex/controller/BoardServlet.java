package com.javaex.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.javaex.dao.BoardDao;
import com.javaex.dao.BoardDaoImpl;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;

@WebServlet("/board")
public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int maxFilecnt = 2;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String attachPath = getServletContext().getRealPath("/attaches/");
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		String actionName = "";
		
		if(!isMultipart) {
			//요청 구분
			actionName = request.getParameter("a");
			System.out.println("board:" + actionName);
			
			//case 처리
			//2024년 01월 22일 작성자 : 노신영(작성), 이정언(수정 및 확인)
			if ("list".equals(actionName)) {
				String keyfield = request.getParameter("keyfield");
				String keyword = request.getParameter("keyword");
				System.out.println("keyfield: "+keyfield);
				System.out.println("keyword: "+keyword);
				
				// 리스트 가져오기
				BoardDao dao = new BoardDaoImpl();
						
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

				totalRecord = dao.getTotalCount(keyfield, keyword);
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
				List<BoardVo> list = dao.getList(keyfield, keyword, start, end);
				request.setAttribute("list", list);
				request.setAttribute("pageNum", pageNum);
				request.setAttribute("nowBlock", nowBlock);
				request.setAttribute("totalBlock", totalBlock);
					
				RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/list.jsp");
				rd.forward(request, response); 
			
			} else if ("read".equals(actionName)) {
    			// 게시물 가져오기
				int no = Integer.parseInt(request.getParameter("no"));
    			BoardDao dao = new BoardDaoImpl();
    			BoardVo boardVo = dao.getBoard(no);
    			
    			request.setAttribute("boardVo", boardVo);
    			dao.hitup(no);
    			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");
    		
    		//2024년 01월 22일 작성자 : 이정언
    		} else if ("download".equals(actionName)){
    			String filename = request.getParameter("filename");

    		    // 파일 경로 설정 (예시)
    		    String filePath = attachPath + filename;
    		    
    		    
    		    // 파일 다운로드 설정
    		    response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
    		    response.setContentType("application/octet-stream");
    		    
    		    
    		 // 파일 스트림을 읽어와 응답으로 전송
    		    if(filename != null) {
    		    	try (InputStream inputStream = new FileInputStream(filePath);
    	    		         ServletOutputStream outputStream = response.getOutputStream()) {

    	    		        byte[] buffer = new byte[4096];
    	    		        int bytesRead = -1;
    	    		        while ((bytesRead = inputStream.read(buffer)) != -1) {
    	    		            outputStream.write(buffer, 0, bytesRead);
    	    		        }
    	    		    }
    		    }
    		    return;	
    			
    		} else if ("delete".equals(actionName)) {
				int no = Integer.parseInt(request.getParameter("no"));

				BoardDao dao = new BoardDaoImpl();
				dao.delete(no);

				WebUtil.redirect(request, response, "/mysite/board?a=list");

			} else if ("writeform".equals(actionName)) {
    			// 로그인 여부체크
    			UserVo authUser = getAuthUser(request);
    			if (authUser != null) { // 로그인했으면 작성페이지로
    				WebUtil.forward(request, response, "/WEB-INF/views/board/writeform.jsp");
    			} else { // 로그인 안했으면 리스트로
    				WebUtil.redirect(request, response, "/mysite/board?a=list");
    			}
			} else if ("modifyform".equals(actionName)) {
    			// 게시물 가져오기
				int no = Integer.parseInt(request.getParameter("no"));
    			BoardDao dao = new BoardDaoImpl();
    			BoardVo boardVo = dao.getBoard(no);

    			// 게시물 화면에 보내기
    			request.setAttribute("boardVo", boardVo);
    			
    			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyform.jsp");
    		} else {
				WebUtil.redirect(request, response, "/mysite/board?a=list");
    		}
		}
		// 2024년 01월 20일 작성자 : 이정언
		else {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			Map<String, String> fieldMap = new HashMap<>();
			request.setCharacterEncoding("UTF-8");
			
			try {
	            List<FileItem> items = upload.parseRequest(request);
	            
	            if(items.size()>5) {
	            	System.out.println("파일 개수 초과!");
	            	WebUtil.redirect(request, response, "/mysite/board?a=writeform");
	            	return;
	            }
	            
	            int filecnt = 0;
	            for (FileItem item : items) {
	                if (item.isFormField()) {
	                    String fieldName = item.getFieldName();
	                    String value = new String(item.getString().getBytes("ISO-8859-1"), "UTF-8");
	                    System.out.println("필드 이름: " + fieldName + ", 값: " + value);
	                    fieldMap.put(fieldName, value);
	                    
	                    if("a".equals(fieldName)) {
	                    	actionName = value;
	                    	System.out.println("actionName 저장 완료: " + actionName);
	                    }
	                    
	                } else {
	                    
	                	if(items.size() < 4) {
	                		break;
	                	}
	                	
	                	filecnt++;
	                	
	                	String fileName = new File(item.getName()).getName();
	                    long fileSize = item.getSize();
                		// 파일 처리
	                    System.out.println("파일 이름: " + fileName + ", 사이즈: " + fileSize);
	                    //Map에 저장
	                    fieldMap.put("filename" + filecnt, fileName);
	                    fieldMap.put("filesize" + filecnt, Long.toString(fileSize));
	                    // 파일 저장 로직
	                    try { 	
	                    	item.write(new File(attachPath, fileName));
							System.out.println(filecnt + "번째 파일 저장 완료!");
						} catch (Exception e) {
							System.out.println(filecnt + "번째 파일 저장 오류!");
							e.printStackTrace();
						}
	                }
	            }

	            System.out.println("board:" + actionName);
	            System.out.println(fieldMap);
	            
	            //요청 구분
	            if ("modify".equals(actionName)) {
	            	// 게시물 가져오기       	
	    			String title = fieldMap.get("title");
	    			String content = fieldMap.get("content");
	    			int no = Integer.parseInt(fieldMap.get("no"));
	    			String filename[] = {"", ""};
	    			long filesize[] = {0, 0};
	    			String namekey = null;
	    			String sizekey = null;
	    			
	    			for(int i = 0; i < filecnt; i++) {
	    				namekey = "filename" + (i+1);
	    				sizekey = "filesize" + (i+1);
	    				filename[i] = fieldMap.get(namekey);
		    			filesize[i] = Long.parseLong(fieldMap.get(sizekey));
	    			}
	    			
	    			BoardVo vo = new BoardVo(no, title, content, filename[0], filesize[0], filename[1], filesize[1]);
	    			BoardDao dao = new BoardDaoImpl();
	    			
	    			dao.update(vo);
	    			
	    			WebUtil.redirect(request, response, "/mysite/board?a=list");
	    		} 
	            // 2024년 01월 20일 작성자 : 이정언
	            else if ("write".equals(actionName)) {
	    			UserVo authUser = getAuthUser(request);

	    			String title = fieldMap.get("title");
	    			String content = fieldMap.get("content");
	    			
	    			String filename[] = {"", ""};
	    			long filesize[] = {0, 0};
	    			String namekey = null;
	    			String sizekey = null;
	    			
	    			for(int i = 0; i < filecnt; i++) {
	    				namekey = "filename" + (i+1);
	    				sizekey = "filesize" + (i+1);
	    				filename[i] = fieldMap.get(namekey);
		    			filesize[i] = Long.parseLong(fieldMap.get(sizekey));
	    			}	    			
	    			
	    			int userNo = authUser.getNo();

	    			BoardVo vo = new BoardVo(title, content, userNo, filename[0], filesize[0], filename[1], filesize[1]);
	    			BoardDao dao = new BoardDaoImpl();
	    			dao.insert(vo);
	    			
	    			WebUtil.redirect(request, response, "/mysite/board?a=list");
	    		}else {
	    			WebUtil.redirect(request, response, "/mysite/board?a=list");
	    		}
	            

	        } catch (FileUploadException e) {
	            e.printStackTrace();
	            // Handle file upload exception
	        }
			
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
