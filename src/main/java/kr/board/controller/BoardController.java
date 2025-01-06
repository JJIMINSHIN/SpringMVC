package kr.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.board.entity.Board;
import kr.board.mapper.BoardMapper;

@Controller //기존 컨트롤러들은 restcontroller쪽으로 분리
public class BoardController {

	@RequestMapping("/boardMain.do")
	public String main() {
		return "board/main";
	}

}
