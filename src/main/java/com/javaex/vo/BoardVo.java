package com.javaex.vo;

/**
 * 2024년 01월 20일 작성자 : 이정언
 */
public class BoardVo {

	private int no;
	private String title;
	private String content;
	private int hit;
	private String regDate;
	private int userNo;
	private String userName;
	private String filename1;
	private long filesize1;
	private String filename2;
	private long filesize2;
	public BoardVo() {
	}
	
	public BoardVo(int no, String title, String content) {
		this.no = no;		
		this.title = title;
		this.content = content;
	}

	public BoardVo(String title, String content, int userNo) {
		this.title = title;
		this.content = content;
		this.userNo = userNo;
	}
	
	public BoardVo(String title, String content, int userNo, String filename1, long filesize1, String filename2, long filesize2) {
		this.title = title;
		this.content = content;
		this.userNo = userNo;
		this.filename1 = filename1;
		this.filesize1 = filesize1;
		this.filename2 = filename2;
		this.filesize2 = filesize2;
	}
	
	public BoardVo(int no, String title, int hit, String regDate, int userNo, String userName) {
		this.no = no;
		this.title = title;
		this.hit = hit;
		this.regDate = regDate;
		this.userNo = userNo;
		this.userName = userName;
	}
	
	
	public BoardVo(int no, String title, String content, String filename1, long filesize1, String filename2, long filesize2) {
		this.no = no;
		this.title = title;
		this.content = content;
		this.filename1 = filename1;
		this.filesize1 = filesize1;
		this.filename2 = filename2;
		this.filesize2 = filesize2;
		
	}
	
	public BoardVo(int no, String title, String content, int hit, String regDate, int userNo, String userName) {
		this(no, title, hit, regDate, userNo, userName);
		this.content = content;
	}

	public BoardVo(int no, String title, String content, int hit, String regDate, int userNo, String userName,
		String filename1, long filesize1, String filename2, long filesize2) {
		super();
		this.no = no;
		this.title = title;
		this.content = content;
		this.hit = hit;
		this.regDate = regDate;
		this.userNo = userNo;
		this.userName = userName;
		this.filename1 = filename1;
		this.filesize1 = filesize1;
		this.filename2 = filename2;
		this.filesize2 = filesize2;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public int getUserNo() {
		return userNo;
	}
	


	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFilename1() {
		return filename1;
	}

	public long getFilesize1() {
		return filesize1;
	}

	public String getFilename2() {
		return filename2;
	}

	public long getFilesize2() {
		return filesize2;
	}

	public void setFilename1(String filename1) {
		this.filename1 = filename1;
	}

	public void setFilesize1(long filesize1) {
		this.filesize1 = filesize1;
	}

	public void setFilename2(String filename2) {
		this.filename2 = filename2;
	}

	public void setFilesize2(long filesize2) {
		this.filesize2 = filesize2;
	}

	@Override
	public String toString() {
		return "BoardVo [no=" + no + ", title=" + title + ", content=" + content + ", hit=" + hit + ", regDate="
				+ regDate + ", userNo=" + userNo + ", userName=" + userName + ", filename1=" + filename1
				+ ", filesize1=" + filesize1 + ", filename2=" + filename2 + ", filesize2=" + filesize2 + "]";
	}

	
}
