package xin.yuki.auth.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;
import xin.yuki.auth.core.entity.GroupModel;
import xin.yuki.auth.core.entity.PermissionModel;
import xin.yuki.auth.core.entity.RoleModel;
import xin.yuki.auth.core.entity.UserModel;
import xin.yuki.auth.core.exception.UserException;
import xin.yuki.auth.core.mapper.UserMapper;
import xin.yuki.auth.core.service.GroupService;
import xin.yuki.auth.core.service.PermissionService;
import xin.yuki.auth.core.service.RoleService;
import xin.yuki.auth.core.service.UserService;
import xin.yuki.auth.core.util.DistinctUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;

	private final PasswordEncoder passwordEncoder;

	private final GroupService groupService;
	private final RoleService roleService;
	private final PermissionService permissionService;


	public UserServiceImpl(final UserMapper userMapper, final PasswordEncoder passwordEncoder,
	                       final GroupService groupService, final RoleService roleService,
	                       final PermissionService permissionService) {
		this.userMapper = userMapper;
		this.passwordEncoder = passwordEncoder;
		this.groupService = groupService;
		this.roleService = roleService;
		this.permissionService = permissionService;
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

	@Override
	public UserModel userDetailsInfo(final String username) throws UsernameNotFoundException {
		final UserModel user = this.findByUsername(username);
		//Groups
		final List<GroupModel> groups = this.groupService.findUserGroups(user.getId());
		user.setGroups(ListUtils.emptyIfNull(groups));

		//Roles
		final List<RoleModel> groupRoles =
				ListUtils.emptyIfNull(groups).stream()
						.flatMap(g -> {
							List<RoleModel> roles = this.roleService.findGroupRoles(g.getId());
							return roles.stream();
						}).collect(Collectors.toList());

		final List<RoleModel> allRoles = ListUtils
				.union(ListUtils.emptyIfNull(this.roleService.findUserRoles(user.getId())),
						groupRoles);

		List<RoleModel> finalRoles = new ArrayList<>(allRoles);
		//All Role 继续递归找到 父角色
		for (final RoleModel role : allRoles) {
			finalRoles = ListUtils.union(this.roleService.findAllParentRoles(role.getId()), finalRoles);
		}
		//去重
		final List<RoleModel> distinctRoles =
				finalRoles.stream().filter(DistinctUtils.distinctByKey(RoleModel::getCode)).collect(Collectors.toList());
		user.setRoles(distinctRoles);

		//Authorities
		final List<PermissionModel> permissions =
				distinctRoles.stream().flatMap(
						role -> this.permissionService.findRolePermissions(role.getId()).stream())
						.collect(Collectors.toList());
		user.setPermissions(permissions);
		return user;
	}




}
