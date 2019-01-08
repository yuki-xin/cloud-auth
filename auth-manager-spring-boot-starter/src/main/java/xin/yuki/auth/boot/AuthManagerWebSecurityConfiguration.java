package xin.yuki.auth.boot;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import xin.yuki.auth.core.mapper.*;
import xin.yuki.auth.core.service.UserDetailService;
import xin.yuki.auth.core.service.impl.UserDetailServiceImpl;

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
	public UserDetailService userDetailsService(final PasswordEncoder passwordEncoder,
	                                            final UserMapper userDao,
	                                            final GroupMapper groupDao, final UserGroupMapper userGroupDao,
	                                            final UserRoleMapper userRoleDao, final GroupRoleMapper groupRoleDao,
	                                            final RoleMapper roleDao, final PermissionMapper permissionDao,
	                                            final RolePermissionMapper rolePermissionDao) throws Exception {
		return new UserDetailServiceImpl(this.authenticationManager(), passwordEncoder, userDao, groupDao,
				userGroupDao,
				userRoleDao, groupRoleDao, roleDao, permissionDao, rolePermissionDao);
	}

}
