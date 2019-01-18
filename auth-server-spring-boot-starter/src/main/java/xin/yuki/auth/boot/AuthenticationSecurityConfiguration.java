package xin.yuki.auth.boot;


import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import xin.yuki.auth.core.service.UserService;
import xin.yuki.auth.core.service.impl.UserDetailsServiceImpl;

/**
 *  AuthorizationSecurityConfig
 *
 * @author ZQian
 * 2018/11/21 17:45
 */
@EnableWebSecurity
public class AuthenticationSecurityConfiguration extends WebSecurityConfigurerAdapter {


	@Bean
	protected PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/error").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin().defaultSuccessUrl("/")
				.and()
				.headers().frameOptions().disable().and()
				.csrf().disable();
	}

	@Override
	public void configure(final WebSecurity web) throws Exception {
		super.configure(web);
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}


	@Bean
	public UserDetailsService userDetailsService(final UserService userService) throws Exception {
		return new UserDetailsServiceImpl(userService);
	}


//	@Bean
//	public CreateUserRunner createUserRunner(final UserDetailsManager userDetailsService) {
//		return new CreateUserRunner(userDetailsService);
//	}

}
