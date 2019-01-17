package xin.yuki.auth.core.service;

import xin.yuki.auth.core.entity.GroupModel;
import xin.yuki.auth.core.exception.GroupException;

import java.util.List;

public interface GroupService {

	List<GroupModel> findUserGroups(Long userId);

	List<GroupModel> findAll();

	void changeUserGroups(Long userId, List<Long> groupIds);

	List<GroupModel> findGroupByExample(GroupModel group);

	void updateGroup(GroupModel group) throws GroupException;

	void deleteGroup(Long id) throws GroupException;

	void deleteGroups(List<Long> id) throws GroupException;

	GroupModel findById(Long id);

	GroupModel createGroup(GroupModel group);
}
