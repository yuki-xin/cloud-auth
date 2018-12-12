package xin.yuki.auth.manager.service;

import xin.yuki.auth.manager.entity.RouterEntity;

import java.util.List;

public interface RouterService {
	RouterEntity save(RouterEntity security);

	boolean existByName(String name);

	List<RouterEntity> findAll();
}
