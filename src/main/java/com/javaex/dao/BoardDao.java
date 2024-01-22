package com.javaex.dao;

import java.util.List;
import com.javaex.vo.BoardVo;

//2024년 01월 19일 작성자 : 노신영 
public interface BoardDao {
	public List<BoardVo> getList();  // 게시물 전체 목록 조회
	public List<BoardVo> getList(int start, int end);  // 게시물 선택 목록 조회
	public List<BoardVo> getList(String keyField, String keyWord, int start, int end);  // 게시물 검색 목록 조회
	public BoardVo getBoard(int no); // 게시물 상세 조회
	public int insert(BoardVo vo);   // 게시물 등록
	public int delete(int no);       // 게시물 삭제
	public int update(BoardVo vo);   // 게시물 수정
}
