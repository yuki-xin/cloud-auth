package xin.yuki.auth.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "users_roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleRel {
	@Id
	private Long userId;
	@Id
	private Long roleId;
}
