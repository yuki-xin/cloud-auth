package xin.yuki.auth.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

@Configuration
public class UserConfiguration {

	@Bean
	public FilterRegistrationBean registerOpenEntityManagerInViewFilterBean() {
		final FilterRegistrationBean<OpenEntityManagerInViewFilter> registrationBean = new FilterRegistrationBean<>();
		final OpenEntityManagerInViewFilter filter = new OpenEntityManagerInViewFilter();
		registrationBean.addUrlPatterns("/*");
		registrationBean.setOrder(5);
		registrationBean.setFilter(filter);
		return registrationBean;
	}
}