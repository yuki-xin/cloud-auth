package xin.yuki.auth.core.mapper;

import org.apache.ibatis.annotations.Param;
import xin.yuki.auth.core.base.BaseListMapper;
import xin.yuki.auth.core.entity.PermissionModel;

import java.util.List;

/**
 * @author zhang
 */
public interface PermissionMapper extends BaseListMapper<PermissionModel> {

	List<PermissionModel> findByRoleId(@Param("roleId") Long roleId);

}
