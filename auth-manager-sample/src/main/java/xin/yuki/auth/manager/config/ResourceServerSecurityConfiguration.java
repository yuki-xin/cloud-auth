package xin.yuki.auth.manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import xin.yuki.auth.core.config.ClientConfiguration;
import xin.yuki.auth.core.config.CoreConfiguration;
import xin.yuki.auth.manager.service.impl.DynamicTokenService;

/**
 * @Title AuthorizationSecurityConfig
 * @Description OAuth2授权配置
 * @Author ZQian
 * @date: 2018/11/21 16:45
 */
@Configuration
@EnableResourceServer
@Import({ClientConfiguration.class, CoreConfiguration.class})
public class ResourceServerSecurityConfiguration extends ResourceServerConfigurerAdapter {


	private final ResourceServerProperties resourceServerProperties;


	@Autowired
	public ResourceServerSecurityConfiguration(final ResourceServerProperties resourceServerProperties) {
		this.resourceServerProperties = resourceServerProperties;
	}


	@Bean
	public RemoteTokenServices remoteTokenServices() {
		final RemoteTokenServices services = new RemoteTokenServices();
		services.setCheckTokenEndpointUrl(this.resourceServerProperties.getTokenInfoUri());
		services.setClientId(this.resourceServerProperties.getClientId());
		services.setClientSecret(this.resourceServerProperties.getClientSecret());
		return services;
	}

	@Bean
	public DefaultTokenServices jwtTokenServices(final TokenStore jwtTokenStore) {
		final DefaultTokenServices services = new DefaultTokenServices();
		services.setTokenStore(jwtTokenStore);
		return services;
	}

	@Bean
	@Primary
	public ResourceServerTokenServices dynamicTokenServices(final DefaultTokenServices jwtTokenService) {
		return new DynamicTokenService(jwtTokenService, this.remoteTokenServices());
	}



}

