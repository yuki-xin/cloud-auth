package xin.yuki.auth.core.entity;

import lombok.Data;
import tk.mybatis.mapper.annotation.Version;

import javax.persistence.Id;
import java.io.Serializable;

@Data
class BaseModel implements Serializable {

	private static final long serialVersionUID = -7668104131112527106L;

	@Id
	private Long id;

	@Version
	private Long version;

}