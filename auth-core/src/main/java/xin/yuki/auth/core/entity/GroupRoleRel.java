package xin.yuki.auth.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author zhang
 */
@Table(name = "groups_roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupRoleRel {
	@Id
	private Long groupId;
	@Id
	private Long roleId;
}
