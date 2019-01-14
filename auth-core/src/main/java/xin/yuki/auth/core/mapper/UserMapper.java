package xin.yuki.auth.core.mapper;

import org.apache.ibatis.annotations.Param;
import xin.yuki.auth.core.base.BaseListMapper;
import xin.yuki.auth.core.entity.UserModel;

/**
 * @author zhang
 */
public interface UserMapper extends BaseListMapper<UserModel> {


	UserModel findOneByUsername(@Param("username") String username);

//	UserModel findByGroupId(@Param("groupId") String groupId);

//	UserModel findByRoleId(@Param("roleId") String roleId);

}
