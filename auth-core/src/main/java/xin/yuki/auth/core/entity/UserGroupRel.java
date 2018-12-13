package xin.yuki.auth.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "users_groups")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupRel {
	@Id
	private Long userId;
	@Id
	private Long groupId;
}
