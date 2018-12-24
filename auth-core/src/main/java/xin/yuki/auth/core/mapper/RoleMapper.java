package xin.yuki.auth.core.mapper;

import org.apache.ibatis.annotations.Param;
import xin.yuki.auth.core.base.BaseListMapper;
import xin.yuki.auth.core.entity.RoleModel;

/**
 * @author zhang
 */
public interface RoleMapper extends BaseListMapper<RoleModel> {

	RoleModel findByUserId(@Param("userId") Long userId);

	RoleModel findByGroupId(@Param("groupId") Long groupId);


}
