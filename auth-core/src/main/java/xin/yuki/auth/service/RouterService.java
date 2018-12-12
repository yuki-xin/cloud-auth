package xin.yuki.auth.service;

import xin.yuki.auth.entity.RouterEntity;

import java.util.List;

public interface RouterService {
	RouterEntity save(RouterEntity security);

	boolean existByName(String name);

	List<RouterEntity> findAll();
}
