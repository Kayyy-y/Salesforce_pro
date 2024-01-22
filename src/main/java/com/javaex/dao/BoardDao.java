package com.javaex.dao;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.javaex.vo.BoardVo;

public interface BoardDao {
	//2024년 01월 22일 작성자 : 노신영(작성), 이정언(수정 및 확인)
	public List<BoardVo> getList(String keyfield, String keyword, int start, int end);  // 게시물 전체 목록 조회
	public int getTotalCount(String keyfield, String keyword); // 게시글 총 길이 계산
	
	public BoardVo getBoard(int no); // 게시물 상세 조회
	public int delete(int no);       // 게시물 삭제
	public int update(BoardVo vo);   // 게시물 수정
	
	//2024년 01월 22일 작성자 : 이정언
	public int insert(BoardVo vo);   // 게시물 등록
	public int hitup(int no);		//조회수 증가
}
