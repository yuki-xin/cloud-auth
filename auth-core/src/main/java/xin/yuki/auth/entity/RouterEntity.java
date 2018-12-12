package xin.yuki.auth.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
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
@Entity
@Table(name = "routers", indexes = {@Index(name = "name_idx", unique = true, columnList = "name")})
public class RouterEntity extends BaseEntity {

	private static final long serialVersionUID = 3648916970723972562L;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
	private final List<RouterEntity> children = new ArrayList<>();
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private RouterEntity parent;
	private Boolean hidden;
	private Boolean alwaysShow;
	private String redirect;
	private String name;
	private String path;
	private String component;

	@Type(type = "json")
	@Column(columnDefinition = "json")
	private Map<String, Object> meta;

	public void addChildren(final RouterEntity router) {
		this.children.add(router);
	}
}
