package kr.board.entity;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class MemberUser extends User{ // 스프링에서 제공해주는 User 사용, 멤버 정보, authVO를 memberUser에 넣어주기
	private Member member;

	public MemberUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		// TODO Auto-generated constructor stub
	}

	public MemberUser(Member mvo) {
		super(mvo.getMemID(), mvo.getMemPassword(), mvo.getAuthList().stream()
				.map(auth -> new SimpleGrantedAuthority(auth.getAuth())).collect(Collectors.toList()));
		this.member = mvo;
		// List<AuthVO> -> Collection<SimpleGrantedAuthority>
	}

}
