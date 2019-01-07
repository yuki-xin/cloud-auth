package xin.yuki.auth.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.JdbcClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestValidator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.Assert;
import xin.yuki.auth.server.service.impl.DynamicTokenEndpoint;
import xin.yuki.auth.server.service.impl.DynamicTokenGranter;

import javax.sql.DataSource;
import java.util.Collections;

/**
 *  AuthorizationSecurityConfig
 *
 * @author ZQian
 * 2018/11/21 16:45
 */
@EnableAuthorizationServer
@EnableConfigurationProperties({AuthorizationServerProperties.class})
@Slf4j
public class AuthorizationSecurityConfiguration extends AuthorizationServerConfigurerAdapter {

	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final DataSource dataSource;
	private final ClientDetailsService clientDetailsService;

	@Value("${security.oauth2.authorization.jwt.key-value}")
	private String keyValue;

	@Autowired
	public AuthorizationSecurityConfiguration(final AuthenticationConfiguration authenticationConfiguration,
	                                          final DataSource dataSource,
	                                          final PasswordEncoder passwordEncoder,
	                                          final ClientDetailsService clientDetailsService) throws Exception {
		this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
		this.passwordEncoder = passwordEncoder;
		this.dataSource = dataSource;
		this.clientDetailsService = clientDetailsService;
	}


	@Override
	public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
		//对ClientDetails的配置
		final JdbcClientDetailsServiceBuilder builder =
				clients.jdbc(this.dataSource).passwordEncoder(this.passwordEncoder);
		builder.build();
	}


	@Override
	public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
		endpoints.authenticationManager(this.authenticationManager);
		endpoints.tokenServices(this.jdbcTokenService());
	}

	@Override
	public void configure(final AuthorizationServerSecurityConfigurer security) {
		security.allowFormAuthenticationForClients()
				.tokenKeyAccess("permitAll()")
				.checkTokenAccess("isAuthenticated()")
				.passwordEncoder(this.passwordEncoder);
	}


	@Bean
	public AuthorizationServerTokenServices jdbcTokenService() {
		final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setAuthenticationManager(this.authenticationManager);
		defaultTokenServices.setClientDetailsService(this.clientDetailsService);
		defaultTokenServices.setTokenStore(this.jdbcTokenStore());
		return defaultTokenServices;
	}

	@Bean
	public AuthorizationServerTokenServices jwtTokenService() {
		final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setAuthenticationManager(this.authenticationManager);
		defaultTokenServices.setClientDetailsService(this.clientDetailsService);
		defaultTokenServices.setTokenStore(this.jwtTokenStore());
		defaultTokenServices.setTokenEnhancer(this.jwtAccessTokenConverter());
		return defaultTokenServices;
	}

	@Bean
	@Primary
	public JwtTokenStore jwtTokenStore() {
		return new JwtTokenStore(this.jwtAccessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		Assert.notNull(this.keyValue, "keyValue cannot be null");
		final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		if (!this.keyValue.startsWith("-----BEGIN")) {
			converter.setSigningKey(this.keyValue);
		}

		converter.setVerifierKey(this.keyValue);
		return converter;
	}

	@Bean
	public JdbcTokenStore jdbcTokenStore() {
		return new JdbcTokenStore(this.dataSource);
	}


	@Bean
	@Primary
	public DynamicTokenEndpoint tokenEndpoint(final ApplicationContext applicationContext) {
		final DefaultListableBeanFactory autowireCapableBeanFactory =
				(DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
		autowireCapableBeanFactory.removeBeanDefinition("tokenEndpoint");
		final DynamicTokenEndpoint dynamicTokenEndpoint = new DynamicTokenEndpoint();
		dynamicTokenEndpoint.setClientDetailsService(this.clientDetailsService);
		dynamicTokenEndpoint.setProviderExceptionHandler(new DefaultWebResponseExceptionTranslator());


		dynamicTokenEndpoint.setTokenGranter(this.dynamicTokenGranter());


		dynamicTokenEndpoint.setOAuth2RequestFactory(new DefaultOAuth2RequestFactory(this.clientDetailsService));
		dynamicTokenEndpoint.setOAuth2RequestValidator(new DefaultOAuth2RequestValidator());
		dynamicTokenEndpoint.setAllowedRequestMethods(Collections.singleton(HttpMethod.POST));
		return dynamicTokenEndpoint;
	}

	@Bean
	public AuthorizationCodeServices authorizationCodeServices() {
		return new JdbcAuthorizationCodeServices(this.dataSource);
	}

	@Bean
	public TokenGranter dynamicTokenGranter() {
		return new DynamicTokenGranter(this.clientDetailsService, this.jwtTokenService(), this.jdbcTokenService(),
				this.authorizationCodeServices(), this.authenticationManager);
	}
}

