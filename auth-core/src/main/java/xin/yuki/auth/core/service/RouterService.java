package xin.yuki.auth.core.service;

import xin.yuki.auth.core.entity.RouterEntity;

import java.util.List;

public interface RouterService {
	RouterEntity save(RouterEntity security);

	boolean existByName(String name);

	List<RouterEntity> findAll();
}
