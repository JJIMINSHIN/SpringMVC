package kr.bit.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lombok.extern.log4j.Log4j;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
	"file:src/main/webapp/WEB-INF/spring/root-context.xml",
	"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"
})
public class BoardControllerTest {
	
	@Autowired
	private WebApplicationContext ctx; //Spring Container 메모리 공간
	
	//가상의 MVC framework처럼 동작시킬수있음 , 가상의 MVC 환경을 만들어준다.
	private MockMvc mockMVC;
	
	@Before
	public void setUp() {
		this.mockMVC = MockMvcBuilders.webAppContextSetup(ctx).build();
	}

	
	@Test
	public void testList() throws Exception{
		log.info(
				//컨트롤러한테 실제로 요청을 날려주는 메서드
				mockMVC.perform(MockMvcRequestBuilders.get("/board/list"))
					   .andReturn()
					   .getModelAndView()
				);
	}
}
