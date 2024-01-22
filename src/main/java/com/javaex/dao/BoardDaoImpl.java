package com.javaex.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.javaex.vo.BoardVo;

//2024년 01월 19일 작성자 : 노신영 
public class BoardDaoImpl implements BoardDao {
  private Connection getConnection() throws SQLException {
    Connection conn = null;
    try {
      Class.forName("oracle.jdbc.driver.OracleDriver");
      String dburl = "jdbc:oracle:thin:@localhost:1521:xe";
      conn = DriverManager.getConnection(dburl, "webdb", "1234");
    } catch (ClassNotFoundException e) {
      System.err.println("JDBC 드라이버 로드 실패!");
    }
    return conn;
  }
<<<<<<< HEAD
  	
  	//총 게시물수
  	//2024년 01월 22일 작성자 : 노신영(작성), 이정언(수정 및 확인)
	public int getTotalCount(String keyfield , String keyword) {
=======
  
	public List<BoardVo> getList() {
>>>>>>> refs/remotes/origin/main
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
				
		int totalCount = 0;
		String query = "";

		try {
			conn = getConnection();
			if (keyword == null || keyword.equals("")) {
				query = query + "select count(*) from board";
				pstmt = conn.prepareStatement(query);
			} else {
				if("name".equals(keyfield)) {
					keyfield = "u." + keyfield;
				} else {
					keyfield = "b." + keyfield;
				}
				query = query + "select count(*) from board b, users u where " + keyfield  + " like ?  or b.user_no = u.no";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, "%" + keyword + "%");
			}
<<<<<<< HEAD
			rs = pstmt.executeQuery();
			if (rs.next()) {
				totalCount = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
=======
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
		}
		return list;
	}

	public List<BoardVo> getList(int start, int end) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVo> list = new ArrayList<BoardVo>();
		
		try {
			conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "SELECT NO, title, hit, regdate, uno, name \n"
					+ "FROM ( SELECT no, title, hit, regdate, uno, name, rownum AS rnum\n"
					+ "	   FROM (SELECT b.NO AS no, b.title AS title, b.hit AS hit, to_char(b.reg_date,'YY-MM-DD HH:MM') AS regdate, b.user_no AS uno, u.name AS name\n"
					+ "	   		 FROM BOARD b, USERS u \n"
					+ "	   		 WHERE b.USER_no = u.NO)\n"
					+ "	   WHERE rownum <= ? \n"
					+ "	   )\n"
					+ "WHERE ? <= rnum ";
			
			//바인딩
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			
			rs = pstmt.executeQuery();
			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("regdate");
				int userNo = rs.getInt("uno");
				String userName = rs.getString("name");
				
				BoardVo vo = new BoardVo(no, title, hit, regDate, userNo, userName);
				list.add(vo);
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
>>>>>>> refs/remotes/origin/main
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
		}
<<<<<<< HEAD
		return totalCount;
	}
	
	//2024년 01월 22일 작성자 : 노신영(작성), 이정언(수정 및 확인)
	public List<BoardVo> getList(String keyfield, String keyword, int start, int end) {		
		
		String query = "";
		
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVo> list = new ArrayList<BoardVo>();
		
		try {
			conn = getConnection();
			if (keyword == null || keyword.equals("")) {
				query = "SELECT * "
						+ "FROM ( "
						+ "    SELECT ROWNUM AS RNUM, A.board_no, A.title, A.content, A.hit, TO_CHAR(A.reg_date, 'YY-MM-DD HH24:MI') AS reg_date, A.user_no, A.name "
						+ "    FROM ( "
						+ "        SELECT b.NO AS board_no, b.title, b.content, b.hit, b.reg_date, b.filename1, b.filename2, u.NO AS user_no, u.name "
						+ "        FROM board b, USERS u "
						+ "        WHERE b.user_no = u.NO "
						+ "        ORDER BY b.no DESC "
						+ "    ) A  "
						+ "    WHERE ROWNUM <= ? + ? ) "
						+ "WHERE RNUM >= ? ";
			  
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);

			} else {
				
				query = "SELECT * "
						+ "FROM ( "
						+ "    SELECT ROWNUM AS RNUM, A.board_no, A.title, A.content, A.hit, TO_CHAR(A.reg_date, 'YY-MM-DD HH24:MI') AS reg_date, A.user_no, A.name ";
				
				if("content".equals(keyfield)) {
					query = query 
							+ "    FROM ( "
							+ "        SELECT b.NO AS board_no, b.title, b.content, b.hit, b.reg_date, b.filename1, b.filename2, u.NO AS user_no, u.name as name "
							+ "        FROM board b, USERS u "
							+ "        WHERE b.user_no = u.NO "
							+ "				and (content like ?  "
							+ "				or filename1 like ? "
							+ "				or filename2 like ? ) "
							+ " 		order by b.no desc ) A " 
							+ "WHERE ROWNUM <= ? + ? ) " 
						+ "WHERE RNUM >= ? ";
					
					pstmt = conn.prepareStatement(query);
					pstmt.setString(1, "%" + keyword + "%");
					pstmt.setString(2, "%" + keyword + "%");
					pstmt.setString(3, "%" + keyword + "%");
					pstmt.setInt(4, start);
					pstmt.setInt(5, end);
					pstmt.setInt(6, start);
				} else {
					if("name".equals(keyfield)) {
						keyfield = "u." + keyfield;
					} else {
						keyfield = "b." + keyfield;
					}					
					
					query = query 
							+ "    FROM ( "
							+ "        SELECT b.NO AS board_no, b.title, b.content, b.hit, b.reg_date, b.filename1, b.filename2, u.NO AS user_no, u.name as name "
							+ "        FROM board b, USERS u "
							+ "        WHERE b.user_no = u.NO "
							+ "				and "+ keyfield +  " like ? "
							+ " 		order by b.no desc ) A " 
							+ "WHERE ROWNUM <= ? + ? ) " 
						+ "WHERE RNUM >= ? ";
					
					pstmt = conn.prepareStatement(query);
					pstmt.setString(1, "%" + keyword + "%");
					pstmt.setInt(2, start);
					pstmt.setInt(3, end);
					pstmt.setInt(4, start);
				}
			}
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int no = rs.getInt("board_no");
				String title = rs.getString("title");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String userName = rs.getString("name");
				
				BoardVo vo = new BoardVo(no, title, hit, regDate, userNo, userName);
				list.add(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
		}
		return list;
		
		
		
=======
		return list;
>>>>>>> refs/remotes/origin/main
	}
	
	public List<BoardVo> getList(String keyField, String keyWord, int start, int end) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVo> list = new ArrayList<BoardVo>();

		try {
			conn = getConnection();
			
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "SELECT NO, title, hit, regdate, uno, name \n"
					+ "FROM ( SELECT no, title, hit, regdate, uno, name, rownum AS rnum\n"
					+ "	   FROM (SELECT b.NO AS no, b.title AS title, b.hit AS hit, to_char(b.reg_date,'YY-MM-DD HH:MM') AS regdate, b.user_no AS uno, u.name AS name, b.content AS content\n"
					+ "	   		 FROM BOARD b, USERS u \n"
					+ "	   		 WHERE b.USER_no = u.NO)\n"
					+ "	   WHERE " + keyField + " LIKE ? AND rownum <= ? \n"
					+ "	   )\n"
					+ "WHERE ? <= rnum ";
					
			//바인딩
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, "%" + keyWord + "%");
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);
			rs = pstmt.executeQuery();
			
			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("regdate");
				int userNo = rs.getInt("uno");
				String userName = rs.getString("name");
				
				BoardVo vo = new BoardVo(no, title, hit, regDate, userNo, userName);
				list.add(vo);
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
		}
		return list;
	}
	
	public BoardVo getBoard(int no) {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardVo boardVo = null;
		
		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "select b.no, b.title, b.content, b.hit, b.reg_date, b.user_no, u.name, b.filename1, b.filesize1, b.filename2, b.filesize2 "
					     + "from board b, users u "
					     + "where b.user_no = u.no "
					     + "and b.no = ?";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			
			rs = pstmt.executeQuery();
			// 4.결과처리
			while (rs.next()) {
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String userName = rs.getString("name");
				String fileName1 = rs.getString("filename1");
				long fileSize1 = rs.getInt("filesize1");
				String fileName2 = rs.getString("filename2");
				long fileSize2 = rs.getInt("filesize2");
				
				boardVo = new BoardVo(no, title, content, hit, regDate, userNo, userName, fileName1, fileSize1, fileName2, fileSize2);
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}
		System.out.println(boardVo);
		return boardVo;

	}
	
	public int insert(BoardVo vo) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();
		  
		  System.out.println("vo.userNo : ["+vo.getUserNo()+"]");
		  System.out.println("vo.title : ["+vo.getTitle()+"]");
		  System.out.println("vo.content : ["+vo.getContent()+"]");
		  System.out.println("vo.filename1 : ["+vo.getFilename1()+"]");
		  System.out.println("vo.filesize1 : ["+vo.getFilesize1()+"]");
		  System.out.println("vo.filename2 : ["+vo.getFilename2()+"]");
		  System.out.println("vo.filesize2 : ["+vo.getFilesize2()+"]");
      
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "insert into board(no, title, content, hit, reg_date, user_no, filename1, filesize1, filename2, filesize2) values (seq_board_no.nextval, ?, ?, 0, sysdate, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setInt(3, vo.getUserNo());
			pstmt.setString(4, vo.getFilename1());
			pstmt.setLong(5, vo.getFilesize1());
			pstmt.setString(6, vo.getFilename2());
			pstmt.setLong(7, vo.getFilesize2());
			
			System.out.println(vo);
      
			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 등록");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return count;
	}
	
	
	public int delete(int no) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "delete from board where no = ?";
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 삭제");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return count;
	}
	
	
	public int update(BoardVo vo) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "update board set title = ?, content = ?, filename1 = ?, filesize1 = ?, filename2 = ?, filesize2 = ?,  where no = ? ";
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setInt(3, vo.getNo());
			pstmt.setString(4, vo.getFilename1());
			pstmt.setLong(5, vo.getFilesize1());
			pstmt.setString(6, vo.getFilename2());
			pstmt.setLong(7, vo.getFilesize2());

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 수정");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return count;
	}
	//2024년 01월 21일 작성자 : 이정언
	public int hitup(int no) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "update board set hit = hit + 1 where no = ? ";
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "조회수 증가");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return count;
	}
}
