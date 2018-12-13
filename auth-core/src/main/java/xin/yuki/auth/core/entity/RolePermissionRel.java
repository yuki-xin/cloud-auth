package xin.yuki.auth.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "roles_permissions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionRel {
	@Id
	private Long roleId;
	@Id
	private Long permissionId;
}
