package xin.yuki.auth.server.runner;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.provisioning.UserDetailsManager;
import xin.yuki.auth.core.entity.GroupModel;
import xin.yuki.auth.core.entity.PermissionModel;
import xin.yuki.auth.core.entity.RoleModel;
import xin.yuki.auth.core.entity.UserModel;
import xin.yuki.auth.core.properties.CloudAuthServerProperties;

/**
 * 用CommandLineRunner运行可以保证事务的正常使用
 *
 * @author Zhang
 */
public class CreateUserRunner implements CommandLineRunner {

	private static final String DEFAULT_USER = "admin";
	private static final String DEFAULT_PASSWORD = "admin";
	private final UserDetailsManager userDetailsService;
	private final CloudAuthServerProperties cloudAuthServerProperties;

	public CreateUserRunner(final UserDetailsManager userDetailsService,
	                        final CloudAuthServerProperties cloudAuthServerProperties) {
		this.userDetailsService = userDetailsService;
		this.cloudAuthServerProperties = cloudAuthServerProperties;
	}

	@Override
	public void run(final String... args) {
		final String username = StringUtils.isEmpty(this.cloudAuthServerProperties.getUsername()) ? DEFAULT_USER :
				this.cloudAuthServerProperties.getUsername();
		final String password = StringUtils.isEmpty(this.cloudAuthServerProperties.getPassword()) ? DEFAULT_PASSWORD :
				this.cloudAuthServerProperties.getPassword();
		if (!this.userDetailsService.userExists(username)) {
			//创建默认用户组和角色
			final RoleModel role = new RoleModel();
			role.setId(1L);
			role.setName("admin");

			final GroupModel group = new GroupModel();
			group.setId(1L);
			group.setName("admin");
			group.addRole(role);

			//保存默认用户
			final UserModel userDetails = new UserModel(1L, username, password, true);
			userDetails.addGroup(group);

			final PermissionModel permission = new PermissionModel();
			permission.setName("admin");
			permission.setId(1L);
			role.addPermission(permission);

			this.userDetailsService.createUser(userDetails);

		}
	}
}
