package security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import kr.board.entity.Member;
import kr.board.entity.MemberUser;
import kr.board.mapper.MemberMapper;

public class MemberUserDetailsService implements UserDetailsService  {

	@Autowired
	private MemberMapper memberMapper;
	
	//사용자 인증처리
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member mvo=memberMapper.memLogin(username);
		//-->UserDetails -> implements--->User -> extends--->MemberUser
		if(mvo != null) {
			return new MemberUser(mvo); // 멤버 정보, authVO를 memberUser에 넣어주기 new MemberUser(mvo); // Member, AuthVO
		}else {
	   	   throw new UsernameNotFoundException("user with username" + username + "does not exist."); 	
		}
	}

}
