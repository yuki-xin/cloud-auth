package xin.yuki.auth.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tk.mybatis.mapper.annotation.ColumnType;
import xin.yuki.auth.core.type.handler.JSONTypeHandler;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * UserEntity
 *
 * @author ZQian
 * 2018/11/26 15:43
 */
@EqualsAndHashCode(callSuper = true, exclude = {"roles", "groups"})
@Table(name = "users")
@NoArgsConstructor
@Data
public class UserModel extends BaseModel implements UserDetails {


	private static final long serialVersionUID = 5407560146864572541L;
	/**
	 * 组
	 */
	@Transient
	private List<GroupModel> groups = new ArrayList<>();
	/**
	 * 角色
	 */
	@Transient
	private List<RoleModel> roles = new ArrayList<>();
	/**
	 * 权限
	 */
	@Transient
	private List<PermissionModel> permissions = new ArrayList<>();
	/**
	 * 保存用的角色关系
	 */
	@Transient
	private List<UserRoleRel> userRole = new ArrayList<>();
	/**
	 * 保存用的组关系
	 */
	@Transient
	private List<UserGroupRel> userGroup = new ArrayList<>();

	private String username;
	private String name;
	private String password;
	@Column(name = "enabled")
	private Boolean active;

	private String mobileNumber;
	private String email;

	@ColumnType(typeHandler = JSONTypeHandler.class)
	private Map<String, Object> additionalInformation;


	public UserModel(final Long id, final String username, final String password, final String name,
	                 final boolean enabled) {
		this.setId(id);
		this.username = username;
		this.password = password;
		this.name = name;
		this.active = enabled;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		final List<GrantedAuthority> permissions =
				this.getPermissions().stream().map(per -> new SimpleGrantedAuthority(per.getName())).collect(Collectors.toList());
		return permissions;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}


	@Override
	public boolean isEnabled() {
		return this.active;
	}


	public void addGroup(final GroupModel group) {
		this.groups.add(group);
		this.userGroup.add(new UserGroupRel(this.getId(), group.getId()));
	}

	public void addRole(final RoleModel role) {
		this.roles.add(role);
		this.userGroup.add(new UserGroupRel(this.getId(), role.getId()));
	}
}
