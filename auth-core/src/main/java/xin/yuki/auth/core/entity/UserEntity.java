package xin.yuki.auth.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Title UserEntity
 * @Description
 * @Author ZQian
 * @date: 2018/11/26 15:43
 */
@EqualsAndHashCode(callSuper = true, exclude = {"roles", "groups"})
@Entity
@Table(name = "users", indexes = {@Index(name = "username_idx", unique = true, columnList = "username")})
@NoArgsConstructor
@Data
public class UserEntity extends BaseEntity implements UserDetails {


	private static final long serialVersionUID = 5407560146864572541L;
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private final Set<UserGroupEntity> groups = new HashSet<>();
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private final Set<RoleEntity> roles = new HashSet<>();
	private String username;
	private String password;
	private Boolean enabled;

	public UserEntity(final Long id, final String username, final String password, final boolean enable) {
		this.setId(id);
		this.username = username;
		this.password = password;
		this.enabled = enable;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		final Collection<RoleEntity> groupRoles =
				CollectionUtils.emptyIfNull(this.groups).stream().flatMap(g -> g.getRoles().stream()).collect(Collectors.toList());
		final Collection<RoleEntity> allRoles = CollectionUtils.union(CollectionUtils.emptyIfNull(this.roles),
				groupRoles);

		return allRoles.stream().flatMap(role -> role.getPermissions().stream()).collect(Collectors.toList());
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
		return this.enabled;
	}

	public void addUserGroup(final UserGroupEntity userGroup) {
		this.groups.add(userGroup);
	}
}
