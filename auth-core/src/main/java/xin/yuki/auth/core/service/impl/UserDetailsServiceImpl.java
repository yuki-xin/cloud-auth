package xin.yuki.auth.core.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import xin.yuki.auth.core.entity.UserModel;
import xin.yuki.auth.core.service.UserService;

/**
 * @author zhang
 */
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserService userService;

	public UserDetailsServiceImpl(final UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserModel loadUserByUsername(final String username) throws UsernameNotFoundException {
		return this.userService.userDetailsInfo(username);
	}



}
