package xin.yuki.auth.core.service;

import xin.yuki.auth.core.entity.PermissionModel;

import java.util.List;

public interface PermissionService {

	List<PermissionModel> findRolePermissions(Long roleId);
}
