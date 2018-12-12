package xin.yuki.auth.service.impl;

import xin.yuki.auth.entity.RouterEntity;
import xin.yuki.auth.repository.RouterRepository;
import xin.yuki.auth.service.RouterService;

import java.util.List;

/**
 * @author zhang
 */
public class RouterServiceImpl implements RouterService {

	private final RouterRepository routerRepository;

	public RouterServiceImpl(final RouterRepository routerRepository) {
		this.routerRepository = routerRepository;
	}

	@Override
	public RouterEntity save(final RouterEntity security) {
		return this.routerRepository.save(security);
	}

	@Override
	public boolean existByName(final String name) {
		return this.routerRepository.existsByName(name);
	}

	@Override
	public List<RouterEntity> findAll() {
		return this.routerRepository.findAll();
	}
}
