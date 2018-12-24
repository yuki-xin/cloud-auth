package xin.yuki.auth.core.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import xin.yuki.auth.core.entity.*;
import xin.yuki.auth.core.mapper.*;
import xin.yuki.auth.core.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhang
 */
@Slf4j
public class UserServiceImpl implements UserService {


	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userDao;
	private final GroupMapper groupDao;
	private final UserGroupMapper userGroupDao;
	private final UserRoleMapper userRoleDao;
	private final GroupRoleMapper groupRoleDao;
	private final RoleMapper roleDao;
	private final PermissionMapper permissionDao;
	private final RolePermissionMapper rolePermissionDao;

	public UserServiceImpl(final AuthenticationManager authenticationManager,
	                       final PasswordEncoder passwordEncoder, final UserMapper userDao, final GroupMapper groupDao,
	                       final UserGroupMapper userGroupDao, final UserRoleMapper userRoleDao,
	                       final GroupRoleMapper groupRoleDao,
	                       final RoleMapper roleDao, final PermissionMapper permissionDao,
	                       final RolePermissionMapper rolePermissionDao) {
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.userDao = userDao;
		this.groupDao = groupDao;
		this.userGroupDao = userGroupDao;
		this.userRoleDao = userRoleDao;
		this.groupRoleDao = groupRoleDao;
		this.roleDao = roleDao;
		this.permissionDao = permissionDao;
		this.rolePermissionDao = rolePermissionDao;
	}


	@Override
	public UserModel loadUserByUsername(final String username) throws UsernameNotFoundException {
		return this.userDao.findOneByUsername(username);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void createUser(final UserDetails userDetails) {
		final UserModel user = ((UserModel) userDetails);
		user.setPassword(this.passwordEncoder.encode(userDetails.getPassword()));
		this.userDao.insert(user);
		//保存Groups
		final List<GroupModel> groups = user.getGroups();
		this.groupDao.insertList(groups);
		//保存UserGroup
		if (CollectionUtils.isNotEmpty(user.getUserGroup())) {
			this.userGroupDao.insertList(user.getUserGroup());
		}

		//保存Role(User的和Group的)
		final List<RoleModel> allRoles = ListUtils.union(user.getRoles(),
				user.getGroups().stream().flatMap(g -> g.getRoles().stream()).collect(Collectors.toList()));
		if (CollectionUtils.isNotEmpty(allRoles)) {
			this.roleDao.insertList(allRoles);
		}

		//保存UserRole
		if (CollectionUtils.isNotEmpty(user.getUserRole())) {
			this.userRoleDao.insertList(user.getUserRole());
		}
		//保存GroupRole
		final List<GroupRoleRel> groupRoles =
				user.getGroups().stream().flatMap(g -> g.getGroupRole().stream()).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(groupRoles)) {
			this.groupRoleDao.insertList(groupRoles);
		}
		//保存Permission
		final List<PermissionModel> permissions =
				allRoles.stream().flatMap(r -> r.getPermissions().stream()).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(permissions)) {
			this.permissionDao.insertList(permissions);
		}
		//保存Role Permission
		final List<RolePermissionRel> rolePermissions =
				allRoles.stream().flatMap(r -> r.getRolePermission().stream()).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(rolePermissions)) {
			this.rolePermissionDao.insertList(rolePermissions);
		}
	}

	@Override
	public void updateUser(final UserDetails userDetails) {
		this.userDao.updateByPrimaryKey(((UserModel) userDetails));
	}

	@Override
	public void deleteUser(final String username) {
		final UserModel u = new UserModel();
		u.setUsername(username);
		this.userDao.delete(u);
	}

	@Override
	public void changePassword(final String oldPassword, final String newPassword) {
		final Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		if (currentUser == null) {
			throw new AccessDeniedException("Can't change password as no Authentication object found in context for " +
					"current user.");
		} else {
			final String username = currentUser.getName();
			if (this.authenticationManager != null) {
				log.debug("Reauthenticating user '" + username + "' for password change request.");
				this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
						oldPassword));
			} else {
				log.debug("No authentication manager set. Password won't be re-checked.");
			}

			log.debug("Changing password for user '" + username + "'");
			final UserModel user = this.userDao.findOneByUsername(username);
			user.setPassword(this.passwordEncoder.encode(newPassword));
			this.userDao.updateByPrimaryKey(user);

			SecurityContextHolder.getContext().setAuthentication(this.createNewAuthentication(currentUser,
					this.passwordEncoder.encode(newPassword)));
		}
	}

	@Override
	public boolean userExists(final String username) {
		final UserModel u = new UserModel();
		u.setUsername(username);
		return this.userDao.selectCount(u) > 0;
	}

	private Authentication createNewAuthentication(final Authentication currentAuth, final String newPassword) {
		final UserDetails user = this.loadUserByUsername(currentAuth.getName());
		final UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(user,
				newPassword, user.getAuthorities());
		newAuthentication.setDetails(currentAuth.getDetails());
		return newAuthentication;
	}

}
