package xin.yuki.auth.manager.config;


import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import xin.yuki.auth.core.mapper.*;
import xin.yuki.auth.core.service.GroupService;
import xin.yuki.auth.core.service.PermissionService;
import xin.yuki.auth.core.service.RoleService;
import xin.yuki.auth.core.service.UserService;
import xin.yuki.auth.core.service.impl.GroupServiceImpl;
import xin.yuki.auth.core.service.impl.PermissionServiceImpl;
import xin.yuki.auth.core.service.impl.RoleServiceImpl;
import xin.yuki.auth.core.service.impl.UserServiceImpl;

public class AuthManagerConfiguration {

	@Bean
	public UserService userService(final UserMapper userMapper, final PasswordEncoder passwordEncoder) {
		return new UserServiceImpl(userMapper, passwordEncoder);
	}

	@Bean
	public GroupService groupService(final GroupMapper groupMapper, final UserGroupMapper userGroupMapper) {
		return new GroupServiceImpl(groupMapper, userGroupMapper);
	}

	@Bean
	public RoleService roleService(final RoleMapper roleMapper, final UserRoleMapper userRoleMapper) {
		return new RoleServiceImpl(roleMapper, userRoleMapper);
	}

	@Bean
	public PermissionService permissionService(final PermissionMapper permissionMapper) {
		return new PermissionServiceImpl(permissionMapper);
	}
}
