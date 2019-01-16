package xin.yuki.auth.core.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import tk.mybatis.mapper.annotation.ColumnType;
import xin.yuki.auth.core.type.handler.JSONTypeHandler;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "roles")
public class RoleModel extends BaseModel {

	private static final long serialVersionUID = 221289698284488072L;
	/**
	 * 查询用的用户
	 */
	@Transient
	private List<UserModel> users = new ArrayList<>();
	/**
	 * 查询用的组
	 */
	@Transient
	private List<GroupModel> groups = new ArrayList<>();
	private String code;
	private String name;
	private String description;
	/**
	 * 查询用的权限
	 */
	@Transient
	private List<PermissionModel> permissions = new ArrayList<>();
	/**
	 * 保存用的关系
	 */
	@Transient
	private List<RolePermissionRel> rolePermission = new ArrayList<>();

	@ColumnType(typeHandler = JSONTypeHandler.class)
	private Map<String, Object> additionalInformation;

	public void addPermission(final PermissionModel permission) {
		this.permissions.add(permission);
		this.rolePermission.add(new RolePermissionRel(this.getId(), permission.getId()));
	}
}
