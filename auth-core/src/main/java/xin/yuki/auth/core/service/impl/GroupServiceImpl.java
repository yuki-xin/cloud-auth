package xin.yuki.auth.core.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;
import xin.yuki.auth.core.entity.GroupModel;
import xin.yuki.auth.core.entity.UserGroupRel;
import xin.yuki.auth.core.mapper.GroupMapper;
import xin.yuki.auth.core.mapper.UserGroupMapper;
import xin.yuki.auth.core.service.GroupService;

import java.util.List;
import java.util.stream.Collectors;

public class GroupServiceImpl implements GroupService {

	private final GroupMapper groupMapper;
	private final UserGroupMapper userGroupMapper;

	public GroupServiceImpl(final GroupMapper groupMapper, final UserGroupMapper userGroupMapper) {
		this.groupMapper = groupMapper;
		this.userGroupMapper = userGroupMapper;
	}

	@Override
	public List<GroupModel> findUserGroups(final Long userId) {
		return this.groupMapper.findByUserId(userId);
	}

	@Override
	public List<GroupModel> findAll() {
		return this.groupMapper.selectAll();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void changeUserGroups(final Long userId, final List<Long> groupIds) {
		final Example.Builder delete = Example.builder(UserGroupRel.class);
		delete.where(Sqls.custom().andEqualTo("userId", userId));
		this.userGroupMapper.deleteByExample(delete.build());

		final List<UserGroupRel> rels = groupIds.stream().map(gid -> {
			UserGroupRel userGroupRel = new UserGroupRel();
			userGroupRel.setUserId(userId);
			userGroupRel.setGroupId(gid);
			return userGroupRel;
		}).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(rels)) {
			this.userGroupMapper.insertList(rels);
		}
	}
}
