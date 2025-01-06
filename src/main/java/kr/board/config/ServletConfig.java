package kr.board.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/*
 * Servlet-context.xml 대신
 * =>ViewResolver 설정 부분, resources의 매핑, annotation-driven으로 annotation 활성화, pojo(controller)를 자동으로 스프링으로 자동으로 올리는 부분 담당 
 * @Configuration : config로 선언
 * @EnableWebMvc : Spring Framework에서여러 Config 값을 알아서 세팅해줌
 * @ComponentScan : controller 스캔
 * WebMvcConfigurer : view resolver, resources 오버라이드 해주는 인터페이스
 *  => <resources mapping="/resources/**" location="/resources/" />
		<beans:bean
		class="org.springframework.web.servlet.view.InternalResourceView
		Resolver">
		<beans:property name="prefix" value="/WEB-INF/views/" />
		<beans:property name="suffix" value=".jsp" />
		</beans:bean>
 * 
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"kr.board.controller"})
public class ServletConfig implements WebMvcConfigurer{
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		InternalResourceViewResolver bean=new InternalResourceViewResolver();
		bean.setPrefix("/WEB-INF/views/");
		bean.setSuffix(".jsp");
		registry.viewResolver(bean);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//  <resources mapping="/resources/**" location="/resources/" />
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	

}
