package kr.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import kr.board.entity.AuthVO;
import kr.board.entity.Member;
import kr.board.mapper.MemberMapper;

/*
 * 보통 jsp로 보내는게 주기 때문에 String 메서드로 만든다.
 */
@Controller
public class MemberController {

	@Autowired
	MemberMapper memberMapper;

	// 회원 가입시 패스워드 암호화
	@Autowired
	PasswordEncoder pwEncoder;

	@RequestMapping("/memJoin.do")
	public String memJoin() {
		return "member/join";
	}

	// 회원 중복 처라
	@RequestMapping("/memRegisterCheck.do")
	public @ResponseBody int memRegisterCheck(@RequestParam("memID") String memID) {
		// id 체크
		Member m = memberMapper.registerCheck(memID);
		if (m != null || memID.equals(m)) {
			return 0; // 이미 존재하는 회원, 입력불가
		}
		return 1; // 사용 가능
	}

	// 회원 가입 처리
	@RequestMapping("/memRegister.do")
	public String memRegister(Member m, String memPassword1, String memPassword2, RedirectAttributes rttr,
			HttpSession session) {
		if (m.getMemID() == null || m.getMemID().equals("") || memPassword1 == null || memPassword1.equals("")
				|| memPassword2 == null || memPassword2.equals("") || m.getMemName() == null
				|| m.getMemName().equals("") || m.getMemAge() == 0 || m.getAuthList().size() == 0
				|| m.getMemGender() == null || m.getMemGender().equals("") || m.getMemEmail() == null
				|| m.getMemEmail().equals("")) {
			/*
			 * 하나라도 누락되면 다시 memJoin.do로 가야함(리다이렉트 redirect:/memJoin.do) 너무 비효율적 앞단에서 체크하는게
			 * 나음 누락메세지를 가지고 가기? =>jsp로 가는 경로가 아니기 때문에 객체바인딩(Model, HttpServletRequest,
			 * HttpSession) 불가 객체 바인딩을 위해 스프링에서 제공하는 redirectAttributes 사용
			 */
			rttr.addFlashAttribute("msgType", "실패 메세지"); // msgType -> 한번만 flash, 메세지 전달해준다.
			rttr.addFlashAttribute("msg", "가입 내용을 다시 한번 확인해 주세요."); // 전달할 msg
			return "redirect:/memJoin.do"; // ${msgType} , ${msg}
		}

		if (!memPassword1.equals(memPassword2)) {
			rttr.addFlashAttribute("msgType", "실패 메세지");
			rttr.addFlashAttribute("msg", "비밀번호가 서로 다릅니다.");
			return "redirect:/memJoin.do";

		}

		m.setMemProfile(""); // 사진 이미지는 현재 없으므로

		// 회원 테이블에 저장하기
		// 67강 추가 : 비밀번호 암호화하기 -> 스프링이 제공해주는 API사용
		String encyptPw = pwEncoder.encode(m.getMemPassword());
		m.setMemPassword(encyptPw);
		// 암호화된 패스워드 담아주기

		int res = memberMapper.register(m);
		if (res == 1) { // 회원가입 성공
			// 권한 테이블에 회원 권한 저장(추가)
			List<AuthVO> list = m.getAuthList();
			for (AuthVO authVO : list) {
				if (authVO.getAuth() != null) {
					AuthVO saveVO = new AuthVO();
					saveVO.setMemID(m.getMemID()); // 회원 아이디
					saveVO.setAuth(authVO.getAuth()); // 회원 권한 저장
					// 권한 저장
					memberMapper.authInsert(saveVO);
				}
			}

			rttr.addFlashAttribute("msgType", "성공 메세지");
			rttr.addFlashAttribute("msg", "회원가입에 성공했습니다.");
			/*
			 * 권한 추가로 인해 추가된 내용으로 다시 객체 생성해서 해당 아이디에 해당하는 값 mvo로 다시 전달 getMember() : 회원 정보 +
			 * 권한 정보
			 */
			Member mvo = memberMapper.getMember(m.getMemID());
			System.out.println("mvo = " + mvo);
			session.setAttribute("mvo", mvo);

			return "redirect:/";
		} else {
			rttr.addFlashAttribute("msgType", "실패 메세지");
			rttr.addFlashAttribute("msg", "회원가입에 실패했습니다.");
			return "redirect:/memJoin.do";
		}
	}

	// 로그아웃 처리
	@RequestMapping("/memLogout.do")
	public String memLogout(HttpSession session) {
		session.invalidate(); // 세션 무효화
		return "redirect:/";
	}

	// 로그인 처리 (시큐리티와 연관)
	@RequestMapping("/memLoginForm.do")
	public String memLoginForm(HttpSession session) {
		return "member/memLoginForm";
	}

	// 로그인 기능 구현 ->security로 분리

	// 회원 정보 수정화면
	@RequestMapping("/memUpdateForm.do")
	public String memUpdateForm() {
		return "member/memUpdateForm";
	}

	// 회원 정보 수정
	@RequestMapping("/memUpdate.do")
	public String memUpdate(Member m, RedirectAttributes rttr, String memPassword1, String memPassword2,
			HttpSession session) {
		if (m.getMemID() == null || m.getMemID().equals("") || memPassword1 == null || memPassword1.equals("")
				|| memPassword2 == null || memPassword2.equals("") || m.getMemName() == null
				|| m.getMemName().equals("") || m.getMemAge() == 0 || m.getMemGender() == null
				|| m.getMemGender().equals("") || m.getMemEmail() == null || m.getMemEmail().equals("")) {
			// 누락시 다시 수정화면으로 가게하기
			rttr.addFlashAttribute("msgType", "실패 메세지");
			rttr.addFlashAttribute("msg", "모든 내용을 입력하세요.");
			return "redirect:/memUpdateForm.do"; // ${msgType} , ${msg}
		}
		if (!memPassword1.equals(memPassword2)) {
			rttr.addFlashAttribute("msgType", "실패 메세지");
			rttr.addFlashAttribute("msg", "비밀번호가 서로 다릅니다.");
			return "redirect:/memUpdateForm.do"; // ${msgType} , ${msg}
		}
		// 회원을 수정저장하기
		// 추가 : 비밀번호 암호화
		String enPwd = pwEncoder.encode(m.getMemPassword());
		m.setMemPassword(enPwd);
		int result = memberMapper.memUpdate(m); // 1,0으로 던져줌
		if (result == 1) { // 수정성공 메세지
			/*
			 * 기존 권한 삭제 후 새로운 권한 추가
			 */
			memberMapper.authDelete(m.getMemID());

			List<AuthVO> list = m.getAuthList(); // 새로운 권한 추가
			for (AuthVO authVO : list) {
				if (authVO.getAuth() != null) {
					AuthVO saveVO = new AuthVO();
					saveVO.setMemID(m.getMemID());
					saveVO.setAuth(authVO.getAuth());
					memberMapper.authInsert(saveVO);
				}
			}

			rttr.addFlashAttribute("msgType", "성공 메세지");
			rttr.addFlashAttribute("msg", "회원정보 수정에 성공했습니다.");
			// 회원수정이 성공하면=>로그인처리하기
			// 수정된 정보를 다시 가지고 와서 더해주기 -> 이미지 안깨짐
			Member mvo = memberMapper.getMember(m.getMemID());
			session.setAttribute("mvo", mvo); // ${!empty mvo}
			return "redirect:/";
		} else {
			rttr.addFlashAttribute("msgType", "실패 메세지");
			rttr.addFlashAttribute("msg", "회원정보 수정에 실패했습니다.");
			return "redirect:/memUpdateForm.do";
		}
	}

	// 사진 등록 페이지
	@RequestMapping("/memImageForm.do")
	public String memImageForm() {
		return "member/memImageForm";
	}

	// 회원사진 이미지 업로드, (upload 폴더와, db테이블에 저장해야함)
	@RequestMapping("/memImageUpdate.do")
	public String memImageUpdate(HttpServletRequest request, HttpSession session, RedirectAttributes rttr)
			throws IOException {
		/*
		 * 파일 업로드 API 3가지 - cos.jar(고전 방법)
		 */
		MultipartRequest multi = null;
		int fileMaxSize = 10 * 2024 * 1024; // 10MB

		// 업로드 경로 (실제경로) 관련내용 MVC07 FileAddController 참고
		String savePath = request.getRealPath("resources/upload");
		try {
			// request -> memId, memProfile 같은 정보 가져온다. ->파일 중복시 파일이름 rename
			// 이미지 업로드
			multi = new MultipartRequest(request, savePath, fileMaxSize, "UTF-8", new DefaultFileRenamePolicy());
		} catch (Exception e) {
			e.printStackTrace();
			rttr.addFlashAttribute("msgType", "실패 메세지");
			rttr.addFlashAttribute("msg", "파일의 크기는 10MB를 넘을 수 없습니다.");
			return "redirect:/memImageForm.do";
		}

		// DB 테이블에 회원 이미지 업데이트 -> 회원 아이디 필요
		String memID = multi.getParameter("memID");
		String newProfile = "";// 업로드할 파일 이름

		File file = multi.getFile("memProfile"); // resources/upload를 가르키는 파일 객체(포인터)를 만듦

		if (file != null) { // 정상 업로드 -> png, jpg, gif, jpeg만 업로드 되야함
			// 이미지 파일 여부 체크, 이미지 파일이 아니면 삭제!
			String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1); // 확장자 111.jpeg -> +1이니깐 jpeg만
																						// 가져옴
			ext = ext.toUpperCase();
			if (ext.equals("PNG") || ext.equals("JPG") || ext.equals("GIF") || ext.equals("JPEG")) {

				// 새로 업로드된 이미지(new), 현재 DB에 존재하는 이미지(old) -> 새로 업로드된 이미지로 db에 저장하고 현재 db에 존재하는
				// 이미지는 삭제해줘야함
				String oldImg = memberMapper.getMember(memID).getMemProfile();
				File oldFile = new File(savePath + "/" + oldImg);
				if (oldFile.exists()) {
					oldFile.delete();
				}
				newProfile = file.getName();
			} else {
				if (file.exists()) {
					file.delete(); // 삭제
				}
				rttr.addFlashAttribute("msgType", "실패 메세지");
				rttr.addFlashAttribute("msg", "이미지 파일만 업로드 가능합니다.");
				return "redirect:/memImageForm.do";
			}

		}
		// 새로운 이미지를 테이블에 업데이트
		Member mvo = new Member();
		mvo.setMemID(memID);
		mvo.setMemProfile(newProfile);
		memberMapper.memProfileUpdate(mvo); // 이미지 업데이트 성공
		Member m = memberMapper.getMember(memID);

		// 세션을 새롭게 생성한다.
		session.setAttribute("mvo", m);
		rttr.addFlashAttribute("msgType", "성공 메세지");
		rttr.addFlashAttribute("msg", "이미지 변경이 성공했습니다.");
		return "redirect:/";
	}
	
	@GetMapping("/access-denied")
	public String showAccessDenied() {
		return "access-denied";
	}
}