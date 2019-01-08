package xin.yuki.auth.manager.config;


import org.springframework.context.annotation.Bean;
import xin.yuki.auth.core.mapper.UserMapper;
import xin.yuki.auth.core.service.UserService;
import xin.yuki.auth.core.service.impl.UserServiceImpl;

public class AuthManagerConfiguration {

	@Bean
	public UserService userService(final UserMapper userMapper) {
		return new UserServiceImpl(userMapper);
	}
}
