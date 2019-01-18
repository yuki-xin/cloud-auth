package xin.yuki.auth.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

/**
 * 角色上下级关系
 */
@Table(name = "role_rels")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleRel {
	private Long parentId;
	private Long childId;
}
