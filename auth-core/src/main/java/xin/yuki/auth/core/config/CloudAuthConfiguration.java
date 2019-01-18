package xin.yuki.auth.core.config;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.autoconfigure.ConfigurationCustomizer;
import tk.mybatis.mapper.autoconfigure.MybatisProperties;
import tk.mybatis.mapper.autoconfigure.SpringBootVFS;
import xin.yuki.auth.core.mapper.*;
import xin.yuki.auth.core.service.GroupService;
import xin.yuki.auth.core.service.PermissionService;
import xin.yuki.auth.core.service.RoleService;
import xin.yuki.auth.core.service.UserService;
import xin.yuki.auth.core.service.impl.GroupServiceImpl;
import xin.yuki.auth.core.service.impl.PermissionServiceImpl;
import xin.yuki.auth.core.service.impl.RoleServiceImpl;
import xin.yuki.auth.core.service.impl.UserServiceImpl;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author zhang
 */
public class CloudAuthConfiguration {

	private final MybatisProperties properties;
	private final ResourceLoader resourceLoader;
	private final DatabaseIdProvider databaseIdProvider;
	private final List<ConfigurationCustomizer> configurationCustomizers;
	private final Interceptor[] interceptors;

	public CloudAuthConfiguration(final MybatisProperties mybatisProperties, final MybatisProperties properties,
	                              final ObjectProvider<Interceptor[]> interceptorsProvider,
	                              final ResourceLoader resourceLoader,
	                              final ObjectProvider<DatabaseIdProvider> databaseIdProvider,
	                              final ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
		this.properties = properties;
		this.resourceLoader = resourceLoader;
		this.databaseIdProvider = databaseIdProvider.getIfAvailable();
		this.configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
		this.interceptors = interceptorsProvider.getIfAvailable();
		//添加Mybatis配置属性
		if (mybatisProperties.getMapperLocations() == null) {
			mybatisProperties.setMapperLocations(new String[]{"classpath:cloud-auth/mapper/*.xml"});
		} else {
			final List<String> locations = new ArrayList<>(Arrays.asList(mybatisProperties.getMapperLocations()));
			locations.add("classpath:cloud-auth/mapper/*.xml");
			mybatisProperties.setMapperLocations(locations.toArray(new String[0]));
		}

	}

	@Bean
	@ConditionalOnMissingBean
	public SqlSessionFactory sqlSessionFactory(final DataSource dataSource) throws Exception {
		final SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);
		factory.setVfs(SpringBootVFS.class);
		if (StringUtils.hasText(this.properties.getConfigLocation())) {
			factory.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
		}

		org.apache.ibatis.session.Configuration configuration = this.properties.getConfiguration();
		if (configuration == null && !StringUtils.hasText(this.properties.getConfigLocation())) {
			configuration = new org.apache.ibatis.session.Configuration();
		}

		if (configuration != null && !CollectionUtils.isEmpty(this.configurationCustomizers)) {
			final Iterator var4 = this.configurationCustomizers.iterator();

			while (var4.hasNext()) {
				final ConfigurationCustomizer customizer = (ConfigurationCustomizer) var4.next();
				customizer.customize(configuration);
			}
		}

		factory.setConfiguration(configuration);
		if (this.properties.getConfigurationProperties() != null) {
			factory.setConfigurationProperties(this.properties.getConfigurationProperties());
		}

		if (!ObjectUtils.isEmpty(this.interceptors)) {
			factory.setPlugins(this.interceptors);
		}

		if (this.databaseIdProvider != null) {
			factory.setDatabaseIdProvider(this.databaseIdProvider);
		}

		if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
			factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
		}

		if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
			factory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
		}

		if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
			factory.setMapperLocations(this.properties.resolveMapperLocations());
		}

		return factory.getObject();
	}


	@Bean
	public UserService userService(final UserMapper userMapper, final PasswordEncoder passwordEncoder,
	                               final GroupService groupService, final RoleService roleService,
	                               final PermissionService permissionService) {
		return new UserServiceImpl(userMapper, passwordEncoder, groupService, roleService, permissionService);
	}

	@Bean
	public GroupService groupService(final GroupMapper groupMapper, final UserGroupMapper userGroupMapper,
	                                 final GroupRoleMapper groupRoleMapper) {
		return new GroupServiceImpl(groupMapper, userGroupMapper, groupRoleMapper);
	}

	@Bean
	public RoleService roleService(final RoleMapper roleMapper, final UserRoleMapper userRoleMapper,
	                               final GroupRoleMapper groupRoleMapper,
	                               final RolePermissionMapper rolePermissionMapper,
	                               final RoleRelMapper roleRelMapper) {
		return new RoleServiceImpl(roleMapper, userRoleMapper, groupRoleMapper, rolePermissionMapper, roleRelMapper);
	}

	@Bean
	public PermissionService permissionService(final PermissionMapper permissionMapper) {
		return new PermissionServiceImpl(permissionMapper);
	}

}
