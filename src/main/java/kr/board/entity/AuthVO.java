package kr.board.entity;

import lombok.Data;

@Data
public class AuthVO {
	private int no;
	private String memID;
	private String auth; //user, manager. admin 3가지 권한 부여
	
}
