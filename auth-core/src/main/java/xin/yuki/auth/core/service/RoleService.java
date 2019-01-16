package xin.yuki.auth.core.service;

import xin.yuki.auth.core.entity.RoleModel;

import java.util.List;

public interface RoleService {

	List<RoleModel> findGroupRoles(Long groupId);

	List<RoleModel> findUserRoles(Long userId);

	List<RoleModel> findAll();

	void changeUserRoles(Long userId, List<Long> roleIds);
}
