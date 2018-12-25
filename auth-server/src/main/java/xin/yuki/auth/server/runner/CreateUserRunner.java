package xin.yuki.auth.server.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.provisioning.UserDetailsManager;
import xin.yuki.auth.core.entity.GroupModel;
import xin.yuki.auth.core.entity.PermissionModel;
import xin.yuki.auth.core.entity.RoleModel;
import xin.yuki.auth.core.entity.UserModel;

/**
 * 用CommandLineRunner运行可以保证事务的正常使用
 *
 * @author Zhang
 */
public class CreateUserRunner implements CommandLineRunner {

	private static final String DEFAULT_USER = "admin";
	private static final String DEFAULT_PASSWORD = "admin";
	private final UserDetailsManager userDetailsService;

	public CreateUserRunner(final UserDetailsManager userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	public void run(final String... args) {
		if (!this.userDetailsService.userExists(DEFAULT_USER)) {
			//创建默认用户组和角色
			final RoleModel role = new RoleModel();
			role.setId(1L);
			role.setName("admin");

			final GroupModel group = new GroupModel();
			group.setId(1L);
			group.setName("admin");
			group.addRole(role);

			//保存默认用户
			final UserModel userDetails = new UserModel(1L, DEFAULT_USER, DEFAULT_PASSWORD, true);
			userDetails.addGroup(group);

			final PermissionModel permission = new PermissionModel();
			permission.setName("admin");
			permission.setId(1L);
			role.addPermission(permission);

			this.userDetailsService.createUser(userDetails);

		}
	}
}
