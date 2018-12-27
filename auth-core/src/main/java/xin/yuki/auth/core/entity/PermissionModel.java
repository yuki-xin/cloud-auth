package xin.yuki.auth.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Table;

/**
 * AuthorityEntity
 *
 *
 * @author ZQian
 * 2018/11/26 16:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "permissions")
public class PermissionModel extends BaseModel implements GrantedAuthority {

	private static final long serialVersionUID = -812812513295029240L;

	private String name;
	private String description;

	@Override
	public String getAuthority() {
		return String.valueOf(this.getName());
	}
}
