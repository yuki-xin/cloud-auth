package xin.yuki.auth.core.service;

import org.springframework.transaction.annotation.Transactional;
import xin.yuki.auth.core.entity.RoleModel;
import xin.yuki.auth.core.exception.GroupException;
import xin.yuki.auth.core.exception.RoleException;

import java.util.List;

public interface RoleService {

	List<RoleModel> findGroupRoles(Long groupId);

	List<RoleModel> findUserRoles(Long userId);

	List<RoleModel> findAll();

	void changeUserRoles(Long userId, List<Long> roleIds);

	void changeGroupRoles(Long groupId, List<Long> roleIds);

	List<RoleModel> findRoleByExample(RoleModel role);

	List<RoleModel> findParentRoles(Long roleId);

	List<RoleModel> findChildRoles(Long roleId);

	RoleModel findById(Long id);

	void updateRole(RoleModel role) throws RoleException;

	RoleModel createRole(RoleModel role);

	void deleteRole(Long id) throws GroupException;

	void deleteRoles(List<Long> ids) throws GroupException;

	List<RoleModel> findAllParentRoles(Long roleId);

	List<RoleModel> findAllChildRoles(Long roleId);

	@Transactional(rollbackFor = Exception.class)
	void changeParentRoles(Long roleId, List<Long> roleIds);

	@Transactional(rollbackFor = Exception.class)
	void changeChildRoles(Long roleId, List<Long> roleIds);
}
