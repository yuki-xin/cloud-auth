package xin.yuki.auth.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;
import xin.yuki.auth.core.entity.UserModel;
import xin.yuki.auth.core.exception.UserException;
import xin.yuki.auth.core.mapper.UserMapper;
import xin.yuki.auth.core.service.UserService;

import java.util.List;

@Slf4j
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;

	private final PasswordEncoder passwordEncoder;


	public UserServiceImpl(final UserMapper userMapper, final PasswordEncoder passwordEncoder) {
		this.userMapper = userMapper;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserModel findByUsername(final String username) {
		final Example.Builder builder = Example.builder(UserModel.class);
		builder.where(Sqls.custom().andEqualTo("username", username));
		return this.userMapper.selectOneByExample(builder.build());
	}

	@Override
	public UserModel findById(final Long id) {
		return this.userMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<UserModel> findUserByExample(final UserModel user) {

		final Example.Builder builder = Example.builder(UserModel.class);
		final Sqls where = Sqls.custom();
		if (StringUtils.isNotEmpty(user.getUsername())) {
			where.andEqualTo("username", user.getUsername());
		}
		if (StringUtils.isNotEmpty(user.getName())) {
			where.andEqualTo("name", user.getName());
		}
		if (StringUtils.isNotEmpty(user.getMobileNumber())) {
			where.andEqualTo("mobileNumber", user.getMobileNumber());
		}
		if (StringUtils.isNotEmpty(user.getEmail())) {
			where.andEqualTo("email", user.getEmail());
		}
		if (user.getActive() != null) {
			where.andEqualTo("active", user.getActive());
		}
		final Example build = builder.where(where).build();

		return this.userMapper.selectByExample(build);
	}

	@Override
	public void updateUser(final UserModel user) throws UserException {
		final int result = this.userMapper.updateByPrimaryKey(user);
		if (result == 0) {
			throw new UserException("更新失败！");
		}
	}

	@Override
	public void deleteUser(final String username) throws UserException {
		final UserModel u = new UserModel();
		u.setUsername(username);
		final int result = this.userMapper.delete(u);
		if (result == 0) {
			throw new UserException("删除失败！");
		}

	}


	@Override
	public void changePassword(final String username, final String newPassword) {
		log.debug("Changing password for user '" + username + "'");
		final UserModel user = this.findByUsername(username);
		user.setPassword(this.passwordEncoder.encode(newPassword));
		this.userMapper.updateByPrimaryKey(user);
	}

	@Override
	public boolean userExists(final String username) {
		final UserModel u = new UserModel();
		u.setUsername(username);
		return this.userMapper.selectCount(u) > 0;
	}

	@Override
	public void enableUser(final String username, final Boolean enable) {
		final UserModel user = this.findByUsername(username);
		user.setActive(enable);
		this.userMapper.updateByPrimaryKey(user);
	}

	@Override
	public UserModel createUser(final UserModel user) {
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		if (user.getVersion() == null) {
			user.setVersion(1L);
		}
		this.userMapper.insert(user);
		return user;
	}


}
