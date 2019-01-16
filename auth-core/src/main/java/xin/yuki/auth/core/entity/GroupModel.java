package xin.yuki.auth.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.ColumnType;
import xin.yuki.auth.core.type.handler.JSONTypeHandler;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  GroupEntity
 *
 * @author ZQian
 * 2018/11/26 16:04
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

	private String code;
	private String name;
	private String description;
	@ColumnType(typeHandler = JSONTypeHandler.class)
	private Map<String, Object> additionalInformation;

	public void addRole(final RoleModel role) {
		this.roles.add(role);
		this.groupRole.add(new GroupRoleRel(this.getId(), role.getId()));
	}
}
