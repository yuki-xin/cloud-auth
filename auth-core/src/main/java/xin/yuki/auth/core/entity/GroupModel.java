package xin.yuki.auth.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title GroupEntity
 * @Description
 * @Author ZQian
 * @date: 2018/11/26 16:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "groups")
@NoArgsConstructor
public class GroupModel extends BaseModel {

	private static final long serialVersionUID = 3070075343090187811L;
	@Transient
	private final List<RoleModel> roles = new ArrayList<>();
	@Transient
	private final List<UserModel> users = new ArrayList<>();

	@Transient
	private final List<GroupRoleRel> groupRole = new ArrayList<>();

	private String name;

	public void addRole(final RoleModel role) {
		this.roles.add(role);
		this.groupRole.add(new GroupRoleRel(this.getId(), role.getId()));
	}
}
