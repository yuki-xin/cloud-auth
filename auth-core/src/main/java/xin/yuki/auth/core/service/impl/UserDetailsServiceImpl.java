package xin.yuki.auth.core.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;
import xin.yuki.auth.core.entity.UserModel;
import xin.yuki.auth.core.mapper.UserMapper;

/**
 * @author zhang
 */
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserMapper userMapper;

	public UserDetailsServiceImpl(final UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Override
	public UserModel loadUserByUsername(final String username) throws UsernameNotFoundException {
		final Example.Builder builder = Example.builder(UserModel.class);
		final Sqls custom = Sqls.custom();
		builder.where(custom.andEqualTo("username", username).andEqualTo("active", true));
		return this.userMapper.selectOneByExample(builder.build());
	}



}
