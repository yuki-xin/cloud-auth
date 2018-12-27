package xin.yuki.auth.boot;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import xin.yuki.auth.client.service.impl.DynamicTokenService;

/**
 *  AuthorizationSecurityConfig
 *
 * @author ZQian
 * 2018/11/21 16:45
 */
public class AuthResourceSecurityConfiguration extends ResourceServerConfigurerAdapter {


	public final ResourceServerProperties resourceServerProperties;

	public AuthResourceSecurityConfiguration(final ResourceServerProperties resourceServerProperties) {
		this.resourceServerProperties = resourceServerProperties;
	}

	@Override
	public void configure(final HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/error").permitAll()
				.anyRequest().authenticated()
				.and()
				.headers().frameOptions().disable()
				.and()
				.csrf().disable();
	}

	@Bean
	public UserDetailsService userDetailsServiceOne() {
		return s -> null;
	}

	@Bean
	public UserDetailsService userDetailsServiceTwo() {
		return s -> null;
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

