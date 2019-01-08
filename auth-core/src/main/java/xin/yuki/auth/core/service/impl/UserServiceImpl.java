package xin.yuki.auth.core.service.impl;

import xin.yuki.auth.core.entity.UserModel;
import xin.yuki.auth.core.mapper.UserMapper;
import xin.yuki.auth.core.service.UserService;

public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;

	public UserServiceImpl(final UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Override
	public UserModel findByUsername(final String username) {
		return this.userMapper.findOneByUsername(username);
	}
}
