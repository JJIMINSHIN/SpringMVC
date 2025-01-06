package kr.board.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/*
 * root-context.xml 대신
 * DB 설정 대신하는 부분
 * @Configuration :config 설정
 * @MapperScan : <mybatis-spring:scan base-package="kr.board.mapper"/>을 대신하는 부분
 * @PropertySource : 기존 jdbc.driver=com.mysql.cj.jdbc.Driver 이런 설정을 대신해주는 부분 -> src/main/resources에서 properties를 가지고옴
 * 					 properties 참조하려면 Environment 필요함
 * @Autowired: 스프링 컨테이너에 등록한 빈에게 의존관계주입이 필요할 때, DI(의존성 주입)을 도와주는 어노테이션
 * @Bean : 기존 <bean></bean> 태그를 대신 생성해주는 객체.
 * SqlSessionFactoryBean : myDataSource의 정보를 가지고 myBatis용으로 커넥션 풀 만듦
 * SqlSessionFactory : SqlSessionFactoryBean이 만든 커넥션 풀을 데이터베이스 crud를 가능하게끔 스프링으로 넘겨주면
 */

@Configuration
@MapperScan(basePackages = { "kr.board.mapper" })
@PropertySource({ "classpath:persistence-mysql.properties" })
public class RootConfig {

	@Autowired
	private Environment env;

	@Bean
	public DataSource myDataSource() {
		HikariConfig hikariConfig = new HikariConfig();

		hikariConfig.setDriverClassName(env.getProperty("jdbc.driver"));
		hikariConfig.setJdbcUrl(env.getProperty("jdbc.url"));
		hikariConfig.setUsername(env.getProperty("jdbc.user"));
		hikariConfig.setPassword(env.getProperty("jdbc.password"));

		HikariDataSource myDataSource = new HikariDataSource(hikariConfig);

		return myDataSource;
	}

	@Bean
	public SqlSessionFactory sessionFactory() throws Exception {
		
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(myDataSource()); //hikariAPI와 연동
		return (SqlSessionFactory) sessionFactory.getObject();

	}

}
