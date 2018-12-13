package xin.yuki.auth.core.config;

import org.springframework.context.annotation.Configuration;

/**
 * @author zhang
 */
@Configuration
public class RouterConfiguration {

//	@Bean
//	public RouterService routerService(final RouterDao routerRepository) {
//		final RouterServiceImpl routerService = new RouterServiceImpl(routerRepository);
//		//创建默认的动态路由
//		if (!routerService.existByName("security")) {
//			final RouterEntity security = new RouterEntity();
//			security.setId(1L);
//			security.setPath("/security");
//			security.setName("security");
//			final HashMap<String, Object> securityMeta = new HashMap<>();
//			securityMeta.put("title", "security");
//			securityMeta.put("icon", "form");
//			securityMeta.put("roles", Collections.singletonList("admin"));
//			security.setMeta(securityMeta);
//			final RouterEntity router = new RouterEntity();
//			router.setId(2L);
//			router.setPath("/router");
//			router.setComponent("@/views/router/index");
//			router.setName("router");
//			final HashMap<String, Object> routerMeta = new HashMap<>();
//			routerMeta.put("title", "routerManage");
//			routerMeta.put("icon", "form");
//			router.setMeta(routerMeta);
//
//			//保存维护关系的那一头
//			router.setParent(security);
//			routerService.save(router);
//		}
//
//
//		return routerService;
//	}
}
