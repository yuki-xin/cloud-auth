package xin.yuki.auth.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
	 * 查询用的组
	 */
	@Transient
	private final List<GroupModel> groups = new ArrayList<>();
	/**
	 * 查询用的角色
	 */
	@Transient
	private final List<RoleModel> roles = new ArrayList<>();
	/**
	 * 保存用的角色关系
	 */
	@Transient
	private final List<UserRoleRel> userRole = new ArrayList<>();
	/**
	 * 保存用的组关系
	 */
	@Transient
	private final List<UserGroupRel> userGroup = new ArrayList<>();

	private String username;
	private String name;
	private String password;
	@Column(name = "enabled")
	private Boolean active;

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
		final Collection<RoleModel> groupRoles =
				CollectionUtils.emptyIfNull(this.getGroups()).stream().flatMap(g -> g.getRoles().stream()).collect(Collectors.toList());
		final Collection<RoleModel> allRoles = CollectionUtils.union(CollectionUtils.emptyIfNull(this.getRoles()),
				groupRoles);

		final List<GrantedAuthority> premissions =
				allRoles.stream().flatMap(role -> role.getPermissions().stream()).collect(Collectors.toList());
		final List<SimpleGrantedAuthority> roles =
				allRoles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase())).collect(Collectors.toList());
		return CollectionUtils.union(premissions, roles);
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
