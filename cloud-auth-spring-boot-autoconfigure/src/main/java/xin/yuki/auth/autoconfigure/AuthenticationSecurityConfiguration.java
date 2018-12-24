package xin.yuki.auth.autoconfigure;


import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import xin.yuki.auth.core.mapper.*;
import xin.yuki.auth.core.properties.CloudAuthServerProperties;
import xin.yuki.auth.core.service.impl.UserServiceImpl;
import xin.yuki.auth.server.runner.CreateUserRunner;

/**
 * @Title AuthorizationSecurityConfig
 * @Description 认证配置
 * @Author ZQian
 * @date: 2018/11/21 17:45
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
				.headers().frameOptions().disable()
				.and()
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
	public UserDetailsManager userDetailsService(final PasswordEncoder passwordEncoder,
	                                             final UserMapper userDao,
	                                             final GroupMapper groupDao, final UserGroupMapper userGroupDao,
	                                             final UserRoleMapper userRoleDao, final GroupRoleMapper groupRoleDao,
	                                             final RoleMapper roleDao, final PermissionMapper permissionDao,
	                                             final RolePermissionMapper rolePermissionDao) throws Exception {
		return new UserServiceImpl(this.authenticationManager(), passwordEncoder, userDao, groupDao, userGroupDao,
				userRoleDao, groupRoleDao, roleDao, permissionDao, rolePermissionDao);
	}


	@Bean
	public CreateUserRunner createUserRunner(final UserDetailsManager userDetailsService,
	                                         final CloudAuthServerProperties cloudAuthServerProperties) {
		return new CreateUserRunner(userDetailsService, cloudAuthServerProperties);
	}

}
