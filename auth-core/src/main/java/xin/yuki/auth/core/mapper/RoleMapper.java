package xin.yuki.auth.core.mapper;

import org.apache.ibatis.annotations.Param;
import xin.yuki.auth.core.base.BaseListMapper;
import xin.yuki.auth.core.entity.RoleModel;

import java.util.List;

/**
 * @author zhang
 */
public interface RoleMapper extends BaseListMapper<RoleModel> {

	List<RoleModel> findByUserId(@Param("userId") Long userId);

	List<RoleModel> findByGroupId(@Param("groupId") Long groupId);

	List<RoleModel> findParentById(@Param("id") Long id);

	List<RoleModel> findChildrenById(@Param("id") Long id);



}
