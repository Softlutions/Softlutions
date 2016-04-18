package com.cenfotec.dondeEs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.cenfotec.dondeEs.config.PassthroughFilter;
import com.cenfotec.dondeEs.config.WSFilter;

@SpringBootApplication
@ComponentScan(basePackages = {"com.cenfotec.dondeEs"})
@EnableAutoConfiguration

@EnableJpaRepositories("com.cenfotec.dondeEs.repositories")
@EnableTransactionManagement
@EntityScan(basePackages = "com.cenfotec.dondeEs.ejb")
//@ImportResource(value = { "config/spring-mvc.xml" })
@ImportResource(value = { "/WEB-INF/classes/config/spring-mvc.xml" })
public class DondeEsApplication{
	
    public static void main(String[] args) {
        SpringApplication.run(DondeEsApplication.class, args);
    }
    
	@Bean	
	public FilterRegistrationBean filterRegistrationBean() {
		
		List<String> urls = new ArrayList<String>();
		urls.add("/*");
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		PassthroughFilter passFilter = new PassthroughFilter();
		registrationBean.setFilter(passFilter);
		registrationBean.setUrlPatterns(urls);
		return registrationBean;
	}
	
	@Bean	
	public FilterRegistrationBean filterRegistrationBean2() {
		
		List<String> urls = new ArrayList<String>();
		urls.add("/rest/protected/*");
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		WSFilter wsFilter = new WSFilter();
		registrationBean.setFilter(wsFilter);
		registrationBean.setUrlPatterns(urls);
		return registrationBean;
	}
}
