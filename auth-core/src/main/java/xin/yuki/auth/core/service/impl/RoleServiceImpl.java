package xin.yuki.auth.core.service.impl;

import xin.yuki.auth.core.entity.RoleModel;
import xin.yuki.auth.core.mapper.RoleMapper;
import xin.yuki.auth.core.service.RoleService;

import java.util.List;

public class RoleServiceImpl implements RoleService {

	private final RoleMapper roleMapper;

	public RoleServiceImpl(final RoleMapper roleMapper) {
		this.roleMapper = roleMapper;
	}

	@Override
	public List<RoleModel> findGroupRoles(final Long groupId) {
		return this.roleMapper.findByGroupId(groupId);
	}

	@Override
	public List<RoleModel> findUserRoles(final Long userId) {
		return this.roleMapper.findByUserId(userId);
	}
}
