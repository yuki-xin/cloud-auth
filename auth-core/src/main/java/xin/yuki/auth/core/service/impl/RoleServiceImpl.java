package xin.yuki.auth.core.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;
import xin.yuki.auth.core.entity.*;
import xin.yuki.auth.core.exception.GroupException;
import xin.yuki.auth.core.exception.RoleException;
import xin.yuki.auth.core.mapper.*;
import xin.yuki.auth.core.service.RoleService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoleServiceImpl implements RoleService {

	private final RoleMapper roleMapper;

	private final UserRoleMapper userRoleMapper;

	private final GroupRoleMapper groupRoleMapper;

	private final RolePermissionMapper rolePermissionMapper;

	private final RoleRelMapper roleRelMapper;

	public RoleServiceImpl(final RoleMapper roleMapper, final UserRoleMapper userRoleMapper,
	                       final GroupRoleMapper groupRoleMapper, final RolePermissionMapper rolePermissionMapper,
	                       final RoleRelMapper roleRelMapper) {
		this.roleMapper = roleMapper;
		this.userRoleMapper = userRoleMapper;
		this.groupRoleMapper = groupRoleMapper;
		this.rolePermissionMapper = rolePermissionMapper;
		this.roleRelMapper = roleRelMapper;
	}

	@Override
	public List<RoleModel> findGroupRoles(final Long groupId) {
		return this.roleMapper.findByGroupId(groupId);
	}

	@Override
	public List<RoleModel> findUserRoles(final Long userId) {
		return this.roleMapper.findByUserId(userId);
	}

	@Override
	public List<RoleModel> findAll() {
		return this.roleMapper.selectAll();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void changeUserRoles(final Long userId, final List<Long> roleIds) {
		final Example.Builder delete = Example.builder(UserRoleRel.class);
		delete.where(Sqls.custom().andEqualTo("userId", userId));
		this.userRoleMapper.deleteByExample(delete.build());

		final List<UserRoleRel> rels = roleIds.stream().map(rid -> {
			UserRoleRel userRoleRel = new UserRoleRel();
			userRoleRel.setUserId(userId);
			userRoleRel.setRoleId(rid);
			return userRoleRel;
		}).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(rels)) {
			this.userRoleMapper.insertList(rels);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void changeGroupRoles(final Long groupId, final List<Long> roleIds) {
		final Example.Builder delete = Example.builder(GroupRoleRel.class);
		delete.where(Sqls.custom().andEqualTo("groupId", groupId));
		this.groupRoleMapper.deleteByExample(delete.build());

		final List<GroupRoleRel> rels = roleIds.stream().map(rid -> {
			GroupRoleRel userRoleRel = new GroupRoleRel();
			userRoleRel.setGroupId(groupId);
			userRoleRel.setRoleId(rid);
			return userRoleRel;
		}).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(rels)) {
			this.groupRoleMapper.insertList(rels);
		}
	}

	@Override
	public List<RoleModel> findRoleByExample(final RoleModel role) {
		final Example.Builder builder = Example.builder(RoleModel.class);
		final Sqls where = Sqls.custom();
		if (StringUtils.isNotEmpty(role.getCode())) {
			where.andEqualTo("code", role.getCode());
		}
		if (StringUtils.isNotEmpty(role.getName())) {
			where.andEqualTo("name", role.getName());
		}
		final Example build = builder.where(where).build();

		return this.roleMapper.selectByExample(build);
	}

	@Override
	public List<RoleModel> findParentRoles(final Long roleId) {
		return this.roleMapper.findParentById(roleId);
	}

	@Override
	public List<RoleModel> findChildRoles(final Long roleId) {
		return this.roleMapper.findChildrenById(roleId);
	}

	@Override
	public RoleModel findById(final Long id) {
		return this.roleMapper.selectByPrimaryKey(id);
	}

	@Override
	public void updateRole(final RoleModel role) throws RoleException {
		final int result = this.roleMapper.updateByPrimaryKey(role);
		if (result == 0) {
			throw new RoleException("更新失败！");
		}
	}

	@Override
	public RoleModel createRole(final RoleModel role) {
		if (role.getVersion() == null) {
			role.setVersion(1L);
		}
		this.roleMapper.insert(role);
		return role;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteRole(final Long id) throws GroupException {
		final int result = this.roleMapper.deleteByPrimaryKey(id);
		if (result == 0) {
			throw new GroupException("删除失败！");
		}
		//删除用户与Role的绑定
		final UserRoleRel userRoleRel = new UserRoleRel();
		userRoleRel.setRoleId(id);
		this.userRoleMapper.delete(userRoleRel);

		//删除Group与Role的绑定
		final GroupRoleRel groupRoleRel = new GroupRoleRel();
		groupRoleRel.setRoleId(id);
		this.groupRoleMapper.delete(groupRoleRel);

		//删除Role与Permission的绑定
		final RolePermissionRel rolePermissionRel = new RolePermissionRel();
		rolePermissionRel.setRoleId(id);
		this.rolePermissionMapper.delete(rolePermissionRel);

		//删除role与role之间的父子关系
		final RoleRel parentRel = new RoleRel();
		parentRel.setParentId(id);
		this.roleRelMapper.delete(parentRel);

		final RoleRel childRel = new RoleRel();
		childRel.setChildId(id);
		this.roleRelMapper.delete(childRel);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteRoles(final List<Long> ids) throws GroupException {
		for (final Long id : ids) {
			this.deleteRole(id);
		}
	}

	@Override
	public List<RoleModel> findAllParentRoles(final Long roleId) {
		final List<RoleModel> parentRoles = ListUtils.emptyIfNull(this.findParentRoles(roleId));
		List<RoleModel> result = new ArrayList<>(parentRoles);
		for (final RoleModel parentRole : parentRoles) {
			final List<RoleModel> parents = this.findAllParentRoles(parentRole.getId());
			result = ListUtils.union(result, parents);
		}
		return result;
	}

	@Override
	public List<RoleModel> findAllChildRoles(final Long roleId) {
		final List<RoleModel> childRoles = ListUtils.emptyIfNull(this.findChildRoles(roleId));
		List<RoleModel> result = new ArrayList<>(childRoles);
		for (final RoleModel parentRole : childRoles) {
			final List<RoleModel> parents = this.findAllChildRoles(parentRole.getId());
			result = ListUtils.union(result, parents);
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void changeParentRoles(final Long roleId, final List<Long> roleIds) {
		final Example.Builder delete = Example.builder(RoleRel.class);
		delete.where(Sqls.custom().andEqualTo("childId", roleId));
		this.roleRelMapper.deleteByExample(delete.build());

		final List<RoleRel> rels = roleIds.stream().map(rid -> {
			RoleRel roleRel = new RoleRel();
			roleRel.setParentId(rid);
			roleRel.setChildId(roleId);
			return roleRel;
		}).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(rels)) {
			this.roleRelMapper.insertList(rels);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void changeChildRoles(final Long roleId, final List<Long> roleIds) {
		final Example.Builder delete = Example.builder(RoleRel.class);
		delete.where(Sqls.custom().andEqualTo("parentId", roleId));
		this.roleRelMapper.deleteByExample(delete.build());

		final List<RoleRel> rels = roleIds.stream().map(rid -> {
			RoleRel roleRel = new RoleRel();
			roleRel.setParentId(roleId);
			roleRel.setChildId(rid);
			return roleRel;
		}).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(rels)) {
			this.roleRelMapper.insertList(rels);
		}
	}
}
