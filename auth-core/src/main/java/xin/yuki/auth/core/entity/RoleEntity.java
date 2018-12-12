package xin.yuki.auth.core.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zhang
 */
@EqualsAndHashCode(callSuper = true, exclude = {"users", "groups"})
@Data
@Entity
@Table(name = "roles", indexes = {@Index(name = "name_idx", unique = true, columnList = "name")})
public class RoleEntity extends BaseEntity {

	private static final long serialVersionUID = 221289698284488072L;


	private String name;
	private String description;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<PermissionEntity> permissions = new HashSet<>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "roles")
	private final Set<UserEntity> users = new HashSet<>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "roles")
	private final Set<UserGroupEntity> groups = new HashSet<>();

	public void addPermission(final PermissionEntity permission) {
		this.permissions.add(permission);
	}
}
