package xin.yuki.auth.core.service;

import xin.yuki.auth.core.entity.GroupModel;

import java.util.List;

public interface GroupService {

	List<GroupModel> findUserGroups(Long userId);

	List<GroupModel> findAll();

	void changeUserGroups(Long userId, List<Long> groupIds);
}
