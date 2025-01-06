package kr.board.entity;

import lombok.Data;

/*
 * Lombok : setter, getter, toString 등을 대신 자동화해주는 API
 * @Data 
 * */

@Data
public class Board {
	
	private int idx;
	private String memID;
	private String title;
	private String content;
	private String writer;
	private String indate; // 작성일
	private int count; // 조회수
	
}
