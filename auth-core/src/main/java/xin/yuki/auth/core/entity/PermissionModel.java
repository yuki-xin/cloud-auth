package xin.yuki.auth.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import tk.mybatis.mapper.annotation.ColumnType;
import xin.yuki.auth.core.type.handler.JSONTypeHandler;

import javax.persistence.Table;
import java.util.Map;

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

	@ColumnType(typeHandler = JSONTypeHandler.class)
	private Map<String, Object> additionalInformation;

	@Override
	public String getAuthority() {
		return String.valueOf(this.getName());
	}
}
