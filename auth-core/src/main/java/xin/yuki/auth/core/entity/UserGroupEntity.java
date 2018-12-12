package xin.yuki.auth.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @Title GroupEntity
 * @Description
 * @Author ZQian
 * @date: 2018/11/26 16:04
 */
@EqualsAndHashCode(callSuper = true, exclude = {"roles", "users"})
@Data
@Entity
@Table(name = "user_groups", indexes = {@Index(name = "name_idx", unique = true, columnList = "name")})
public class UserGroupEntity extends BaseEntity {

	private static final long serialVersionUID = 3070075343090187811L;
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private final Set<RoleEntity> roles = new HashSet<>();
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "groups")
	private final Set<UserEntity> users = new HashSet<>();
	private String name;

	public void addRole(final RoleEntity role) {
		this.roles.add(role);
	}
}
