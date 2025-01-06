package kr.board.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.board.entity.AuthVO;
import kr.board.entity.Member;

/*
 * @Mapper -Mybatis API ���
 */
@Mapper
public interface MemberMapper {
	
	public Member registerCheck(String memId); //아이디 중복 체크
	public int register(Member m); //회원 등록 (성공 1, 실패0)
	public Member memLogin(Member mvo); // 로그인 체크
	public int memUpdate(Member mvo);
	public Member getMember(String memID); //해당하는 멤버의 정보 가져오기
	public void memProfileUpdate(Member mvo); // id, profile 들어오면 sql 쿼리로 업데이트 실행
	public void authInsert(AuthVO saveVO);
	public void authDelete(String memID);
}
