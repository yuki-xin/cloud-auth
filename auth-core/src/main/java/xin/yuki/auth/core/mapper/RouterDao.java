package xin.yuki.auth.core.mapper;

import xin.yuki.auth.core.base.BaseListMapper;
import xin.yuki.auth.core.entity.RouterModel;

public interface RouterDao extends BaseListMapper<RouterModel> {


	boolean existsByName(String name);
}
