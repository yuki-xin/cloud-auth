//package xin.yuki.auth.config;
//
//
//import org.springframework.boot.autoconfigure.security.SecurityProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.UserDetailsManager;
//import org.springframework.util.StringUtils;
//import xin.yuki.auth.entity.RoleEntity;
//import xin.yuki.auth.entity.UserEntity;
//import xin.yuki.auth.entity.UserGroupEntity;
//import xin.yuki.auth.repository.UserRepository;
//import xin.yuki.auth.service.impl.UserDetailsServiceImpl;
//
///**
// * @Title AuthorizationSecurityConfig
// * @Description 认证配置
// * @Author ZQian
// * @date: 2018/11/21 17:45
// */
//@Configuration
//@EnableWebSecurity
//public class AuthenticationSecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//	private static final String DEFAULT_USER = "admin";
//	private final SecurityProperties securityProperties;
//
//	public AuthenticationSecurityConfiguration(final SecurityProperties securityProperties) {
//		this.securityProperties = securityProperties;
//	}
//
//	@Bean
//	protected static PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	@Override
//	protected void configure(final HttpSecurity http) throws Exception {
//		http.authorizeRequests()
//				.antMatchers("/h2-console/**", "/error").permitAll()
//				.anyRequest().authenticated()
//				.and()
//				.formLogin().defaultSuccessUrl("/")
//				.and()
//				.headers().frameOptions().disable()
//				.and()
//				.csrf().disable();
//	}
//
//	@Override
//	public void configure(final WebSecurity web) throws Exception {
//		super.configure(web);
//	}
//
//	@Override
//	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
//		super.configure(auth);
//	}
//
//	@Override
//	@Bean
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}
//
//	@Bean
//	public UserDetailsManager userDetailsService(final UserRepository userRepository,
//	                                             final AuthenticationManager authenticationManager
//			, final PasswordEncoder passwordEncoder) {
//		final UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepository,
//				authenticationManager, passwordEncoder);
//		final String username = StringUtils.isEmpty(this.securityProperties.getUser().getName()) ? DEFAULT_USER :
//				this.securityProperties.getUser().getName();
//		final String password = StringUtils.isEmpty(this.securityProperties.getUser().getName()) ? DEFAULT_USER :
//				this.securityProperties.getUser().getName();
//		if (!userDetailsService.userExists(username)) {
//			//创建默认用户组和角色
//			final RoleEntity role = new RoleEntity();
//			role.setId(1L);
//			role.setName("admin");
//
//			final UserGroupEntity userGroup = new UserGroupEntity();
//			userGroup.setId(1L);
//			userGroup.setName("admin");
//			userGroup.addRole(role);
//
//			//保存默认用户
//			final UserEntity userDetails = new UserEntity(1L, username, password, true);
//			userDetails.addUserGroup(userGroup);
//
//			userDetailsService.createUser(userDetails);
//
//		}
//		return userDetailsService;
//	}
//
//
//}
