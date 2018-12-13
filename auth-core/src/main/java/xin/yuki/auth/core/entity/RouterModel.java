package xin.yuki.auth.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Title MenuEntity
 * @Description
 * @Author ZQian
 * @date: 2018/11/27 10:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "routers")
public class RouterModel extends BaseModel {

	private static final long serialVersionUID = 3648916970723972562L;

	private Boolean hidden;
	private Boolean alwaysShow;
	private String redirect;
	private String name;
	private String path;
	private String component;

	private Map<String, Object> meta;

	@Transient
	private final List<RouterModel> children = new ArrayList<>();

	@Transient
	private RouterModel parent;

	public void addChildren(final RouterModel router) {
		this.children.add(router);
	}
}
