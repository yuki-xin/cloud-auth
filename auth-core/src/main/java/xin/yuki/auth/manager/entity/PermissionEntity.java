package xin.yuki.auth.manager.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @Title AuthorityEntity
 * @Description
 * @Author ZQian
 * @date: 2018/11/26 16:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "permissions", indexes = @Index(name = "name_idx", columnList = "name", unique = true))
class PermissionEntity extends BaseEntity implements GrantedAuthority {

	private static final long serialVersionUID = -812812513295029240L;

	private String name;
	private String description;

	@Override
	public String getAuthority() {
		return String.valueOf(this.getName());
	}
}
