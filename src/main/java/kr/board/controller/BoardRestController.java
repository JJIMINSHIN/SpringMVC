package kr.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import kr.board.entity.Board;
import kr.board.mapper.BoardMapper;
import lombok.Delegate;

@RequestMapping("/board")
@RestController //ajax통신시 사용하는 컨트롤러로서 기존의 생략가능
public class BoardRestController {
	

	@Autowired
	BoardMapper boardMapper;

	@GetMapping("/all")
	public List<Board> boardList() { //-> 스프링이 json으로 변환한다. jaskson-databind API를 사용해서
		List<Board> list = boardMapper.getLists();
		return list; //객체 리턴 -> JSON 데이터 형식으로 변환해 리턴
	}
	
	//@RequestMapping("/boardInsert.do")
	@PostMapping("/new")
	public void boardInsert(Board vo) {
		boardMapper.boardInsert(vo);  
	}
	
	@DeleteMapping("/{idx}")
	public void boardDelete(@PathVariable("idx") int idx) {
		boardMapper.boardDelete(idx);  
	}
	
	@PutMapping("/update")
	public void boardUpdate(@RequestBody Board vo) { //@RequestBody:json 데이터 받을때 사용해야함
		boardMapper.boardUpdate(vo);  
	}
	
	@GetMapping("/{idx}")
	public Board boardContent(@PathVariable("idx") int idx) {
		Board vo = boardMapper.boardContent(idx);
		return vo; //vo ->JSON
	}
	
	@PutMapping("/count/{idx}")
	public Board boardCount(@PathVariable("idx") int idx) {
		boardMapper.boardCount(idx);  //조회수 누적
		Board vo = boardMapper.boardContent(idx); //누적된 조회수 return 
		return vo;
	}

}
