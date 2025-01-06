package kr.board.entity;

import java.util.List;

import lombok.Data;

@Data
public class Member {
	private int memIdx;
	private String memID;
	private String memPassword;
	private String memName;
	private int memAge; // <-null, 0
	private String memGender;
	private String memEmail;
	private String memProfile; // 사진정보
	// 회원 한명이 여러 권한을 가지고 있을 수 있으므로 list로 받아주기
	private List<AuthVO> authList;  //authList[0].auth, authList[1].auth, authList[2].auth으로 파라미터 넘겨주기
}
