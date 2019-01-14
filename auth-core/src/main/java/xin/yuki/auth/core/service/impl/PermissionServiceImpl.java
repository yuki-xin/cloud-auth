package xin.yuki.auth.core.service.impl;

import xin.yuki.auth.core.entity.PermissionModel;
import xin.yuki.auth.core.mapper.PermissionMapper;
import xin.yuki.auth.core.service.PermissionService;

import java.util.List;

public class PermissionServiceImpl implements PermissionService {

	private final PermissionMapper permissionMapper;

	public PermissionServiceImpl(final PermissionMapper permissionMapper) {
		this.permissionMapper = permissionMapper;
	}

	@Override
	public List<PermissionModel> findRolePermissions(final Long roleId) {
		return this.permissionMapper.findByRoleId(roleId);
	}
}
