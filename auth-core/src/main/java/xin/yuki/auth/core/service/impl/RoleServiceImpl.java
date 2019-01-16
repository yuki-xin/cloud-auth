package xin.yuki.auth.core.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;
import xin.yuki.auth.core.entity.RoleModel;
import xin.yuki.auth.core.entity.UserGroupRel;
import xin.yuki.auth.core.entity.UserRoleRel;
import xin.yuki.auth.core.mapper.RoleMapper;
import xin.yuki.auth.core.mapper.UserRoleMapper;
import xin.yuki.auth.core.service.RoleService;

import java.util.List;
import java.util.stream.Collectors;

public class RoleServiceImpl implements RoleService {

	private final RoleMapper roleMapper;

	private final UserRoleMapper userRoleMapper;

	public RoleServiceImpl(final RoleMapper roleMapper, final UserRoleMapper userRoleMapper) {
		this.roleMapper = roleMapper;
		this.userRoleMapper = userRoleMapper;
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
	public void changeUserRoles(final Long userId, final List<Long> roleIds) {
		final Example.Builder delete = Example.builder(UserGroupRel.class);
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
}
