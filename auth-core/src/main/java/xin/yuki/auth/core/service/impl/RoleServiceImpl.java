package xin.yuki.auth.core.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;
import xin.yuki.auth.core.entity.GroupRoleRel;
import xin.yuki.auth.core.entity.RoleModel;
import xin.yuki.auth.core.entity.UserRoleRel;
import xin.yuki.auth.core.mapper.GroupRoleMapper;
import xin.yuki.auth.core.mapper.RoleMapper;
import xin.yuki.auth.core.mapper.UserRoleMapper;
import xin.yuki.auth.core.service.RoleService;

import java.util.List;
import java.util.stream.Collectors;

public class RoleServiceImpl implements RoleService {

	private final RoleMapper roleMapper;

	private final UserRoleMapper userRoleMapper;

	private final GroupRoleMapper groupRoleMapper;

	public RoleServiceImpl(final RoleMapper roleMapper, final UserRoleMapper userRoleMapper,
	                       final GroupRoleMapper groupRoleMapper) {
		this.roleMapper = roleMapper;
		this.userRoleMapper = userRoleMapper;
		this.groupRoleMapper = groupRoleMapper;
	}

	@Override
	public List<RoleModel> findGroupRoles(final Long groupId) {
		return this.roleMapper.findByGroupId(groupId);
	}

	@Override
	public List<RoleModel> findUserRoles(final Long userId) {
		return this.roleMapper.findByUserId(userId);
	}

	@Override
	public List<RoleModel> findAll() {
		return this.roleMapper.selectAll();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void changeUserRoles(final Long userId, final List<Long> roleIds) {
		final Example.Builder delete = Example.builder(UserRoleRel.class);
		delete.where(Sqls.custom().andEqualTo("userId", userId));
		this.userRoleMapper.deleteByExample(delete.build());

		final List<UserRoleRel> rels = roleIds.stream().map(rid -> {
			UserRoleRel userRoleRel = new UserRoleRel();
			userRoleRel.setUserId(userId);
			userRoleRel.setRoleId(rid);
			return userRoleRel;
		}).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(rels)) {
			this.userRoleMapper.insertList(rels);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void changeGroupRoles(final Long groupId, final List<Long> roleIds) {
		final Example.Builder delete = Example.builder(GroupRoleRel.class);
		delete.where(Sqls.custom().andEqualTo("groupId", groupId));
		this.groupRoleMapper.deleteByExample(delete.build());

		final List<GroupRoleRel> rels = roleIds.stream().map(rid -> {
			GroupRoleRel userRoleRel = new GroupRoleRel();
			userRoleRel.setGroupId(groupId);
			userRoleRel.setRoleId(rid);
			return userRoleRel;
		}).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(rels)) {
			this.groupRoleMapper.insertList(rels);
		}
	}
}
