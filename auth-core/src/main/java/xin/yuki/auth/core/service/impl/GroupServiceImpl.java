package xin.yuki.auth.core.service.impl;

import xin.yuki.auth.core.entity.GroupModel;
import xin.yuki.auth.core.mapper.GroupMapper;
import xin.yuki.auth.core.service.GroupService;

import java.util.List;

public class GroupServiceImpl implements GroupService {

	private final GroupMapper groupMapper;

	public GroupServiceImpl(final GroupMapper groupMapper) {
		this.groupMapper = groupMapper;
	}

	@Override
	public List<GroupModel> findUserGroups(final Long userId) {
		return this.groupMapper.findByUserId(userId);
	}
}
