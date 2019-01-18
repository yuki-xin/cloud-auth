package xin.yuki.auth.boot;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import xin.yuki.auth.core.service.UserService;
import xin.yuki.auth.core.service.impl.UserDetailsServiceImpl;

public class AuthManagerWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(final HttpSecurity http) {

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Bean
	public UserDetailsService userDetailsService(final UserService userService) throws Exception {
		return new UserDetailsServiceImpl(userService);
	}

}
