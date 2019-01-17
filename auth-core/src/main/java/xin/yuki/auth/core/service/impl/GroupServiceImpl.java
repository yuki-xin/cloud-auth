package xin.yuki.auth.core.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;
import xin.yuki.auth.core.entity.GroupModel;
import xin.yuki.auth.core.entity.GroupRoleRel;
import xin.yuki.auth.core.entity.UserGroupRel;
import xin.yuki.auth.core.exception.GroupException;
import xin.yuki.auth.core.mapper.GroupMapper;
import xin.yuki.auth.core.mapper.GroupRoleMapper;
import xin.yuki.auth.core.mapper.UserGroupMapper;
import xin.yuki.auth.core.service.GroupService;

import java.util.List;
import java.util.stream.Collectors;

public class GroupServiceImpl implements GroupService {

	private final GroupMapper groupMapper;
	private final UserGroupMapper userGroupMapper;
	private final GroupRoleMapper groupRoleMapper;

	public GroupServiceImpl(final GroupMapper groupMapper, final UserGroupMapper userGroupMapper,
	                        final GroupRoleMapper groupRoleMapper) {
		this.groupMapper = groupMapper;
		this.userGroupMapper = userGroupMapper;
		this.groupRoleMapper = groupRoleMapper;
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

	@Override
	public List<GroupModel> findGroupByExample(final GroupModel group) {
		final Example.Builder builder = Example.builder(GroupModel.class);
		final Sqls where = Sqls.custom();
		if (StringUtils.isNotEmpty(group.getCode())) {
			where.andEqualTo("code", group.getCode());
		}
		if (StringUtils.isNotEmpty(group.getName())) {
			where.andEqualTo("name", group.getName());
		}
		final Example build = builder.where(where).build();

		return this.groupMapper.selectByExample(build);
	}

	@Override
	public void updateGroup(final GroupModel group) throws GroupException {
		final int result = this.groupMapper.updateByPrimaryKey(group);
		if (result == 0) {
			throw new GroupException("更新失败！");
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteGroup(final Long id) throws GroupException {
		final int result = this.groupMapper.deleteByPrimaryKey(id);
		if (result == 0) {
			throw new GroupException("删除失败！");
		}
		final GroupRoleRel groupRoleRel = new GroupRoleRel();
		groupRoleRel.setGroupId(id);
		this.groupRoleMapper.delete(groupRoleRel);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteGroups(final List<Long> ids) throws GroupException {
		for (final Long id : ids) {
			this.deleteGroup(id);
		}
	}

	@Override
	public GroupModel findById(final Long id) {
		return this.groupMapper.selectByPrimaryKey(id);
	}

	@Override
	public GroupModel createGroup(final GroupModel group) {
		if (group.getVersion() == null) {
			group.setVersion(1L);
		}
		this.groupMapper.insert(group);
		return group;
	}
}
