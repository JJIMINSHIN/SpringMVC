package kr.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

/*
 * Spring security 환경설정 파일
 * @@EnableWebSecurity : 웹에서 스프링과 스프링 시큐리티를 연결해주는 어노테이션
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//요청에대한 보안 설정~~
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		http.addFilterBefore(filter,CsrfFilter.class);	
		
		/*
		 * 회원 인증 권한 설정
		 * authorizeRequests() : 권한에 따라 요청 설정
		 * antMatchers("/") : url 매칭
		 */
		http 
			.authorizeRequests()
				.antMatchers("/")
				.permitAll() // 모두 허용
				.and() //요청 추가
			.formLogin()
				.loginPage("/memLoginForm")  //custom url
				.loginProcessingUrl("/memLogin.do") //spring이 제공하는 인증해줌
				.permitAll()
				.and()
			.logout()
				.invalidateHttpSession(true)
				.logoutSuccessUrl("/")
				.and()
			.exceptionHandling().accessDeniedPage("/access-denied")
				;
			
			
	}	
	
	/*
	 * 패스워드 인코딩 객체 설정
	 * 왜? ->autowired 의존성 주입을 받아 써야하니깐
	 */
	@Bean
	public PasswordEncoder psswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}