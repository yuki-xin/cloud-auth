package xin.yuki.auth.core.mapper;

import org.apache.ibatis.annotations.Param;
import xin.yuki.auth.core.base.BaseListMapper;
import xin.yuki.auth.core.entity.GroupModel;

import java.util.List;

/**
 * @author zhang
 */
public interface GroupMapper extends BaseListMapper<GroupModel> {

	List<GroupModel> findByUserId(@Param("userId") Long userId);

	List<GroupModel> findByRoleId(@Param("roleId") Long roleId);


}
