package xin.yuki.auth.manager.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import xin.yuki.auth.core.mapper.*;
import xin.yuki.auth.core.service.impl.UserServiceImpl;

/**
 * @Title AuthorizationSecurityConfig
 * @Description 认证配置
 * @Author ZQian
 * @date: 2018/11/21 17:45
 */
@Configuration
@EnableWebSecurity
public class AuthenticationSecurityConfiguration extends WebSecurityConfigurerAdapter {


	@Bean
	protected static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/error").permitAll()
				.antMatchers("/test").hasAnyAuthority("admin")
				.anyRequest().authenticated()
				.and()
				.headers().frameOptions().disable()
				.and()
				.csrf().disable();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Bean
	public UserDetailsManager userDetailsService(final AuthenticationManager authenticationManager
			, final PasswordEncoder passwordEncoder, final UserDao userDao, final GroupDao groupDao,
			                                     final UserGroupDao userGroupDao,
			                                     final UserRoleDao userRoleDao, final GroupRoleDao groupRoleDao,
			                                     final RoleDao roleDao,
			                                     final PermissionDao permissionDao,
			                                     final RolePermissionDao rolePermissionDao) {
		return new UserServiceImpl(authenticationManager, passwordEncoder, userDao, groupDao, userGroupDao,
				userRoleDao, groupRoleDao, roleDao, permissionDao, rolePermissionDao);
	}

}
