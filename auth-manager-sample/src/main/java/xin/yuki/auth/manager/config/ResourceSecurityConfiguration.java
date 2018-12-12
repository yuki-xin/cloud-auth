//package xin.yuki.auth.manager.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
//import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.config.annotation.builders.JdbcClientDetailsServiceBuilder;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.ClientDetailsService;
//import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
//import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//import org.springframework.util.Assert;
//import org.springframework.util.StringUtils;
//import xin.yuki.auth.core.service.ClientService;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//
///**
// * @Title AuthorizationSecurityConfig
// * @Description OAuth2授权配置
// * @Author ZQian
// * @date: 2018/11/21 16:45
// */
//@Configuration
//@EnableAuthorizationServer
//@EnableConfigurationProperties(AuthorizationServerProperties.class)
//public class AuthorizationSecurityConfiguration extends AuthorizationServerConfigurerAdapter {
//
//	private static final String DEFAULT_CLIENT = "client";
//
//	private static final String DEFAULT_AUTH_CLIENT = "auth-server";
//
//	private final AuthenticationManager authenticationManager;
//
//	private final PasswordEncoder passwordEncoder;
//
//	private final OAuth2ClientProperties oAuth2ClientProperties;
//	private final AuthorizationServerProperties authorizationServerProperties;
//
//	private final DataSource dataSource;
//
//
//	private final ClientDetailsService clientDetailsService;
//	private final ClientService clientService;
//
//
//	@Autowired
//	public AuthorizationSecurityConfiguration(final AuthenticationConfiguration authenticationConfiguration,
//	                                          final OAuth2ClientProperties oAuth2ClientProperties,
//	                                          final DataSource dataSource,
//	                                          final PasswordEncoder passwordEncoder,
//	                                          final AuthorizationServerProperties authorizationServerProperties,
//	                                          final ClientDetailsService clientDetailsService,
//	                                          final ClientService clientService) throws Exception {
//		this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
//		this.oAuth2ClientProperties = oAuth2ClientProperties;
//		this.passwordEncoder = passwordEncoder;
//		this.dataSource = dataSource;
//		this.authorizationServerProperties = authorizationServerProperties;
//		this.clientDetailsService = clientDetailsService;
//		this.clientService = clientService;
//	}
//
//
//	@Override
//	public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
//		//对ClientDetails的配置
//		super.configure(clients);
//		final JdbcClientDetailsServiceBuilder builder =
//				clients.jdbc(this.dataSource).passwordEncoder(this.passwordEncoder);
//
//
//		final String client = StringUtils.isEmpty(this.oAuth2ClientProperties.getClientId()) ? DEFAULT_CLIENT :
//				this.oAuth2ClientProperties.getClientSecret();
//		final HashMap<String, Object> additionalInformation = new HashMap<>(1);
//		additionalInformation.put("tokenType", "default");
//		if (!this.clientService.clientExists(client)) {
//			builder.withClient(client).secret(client)
//					.scopes("all")
//					.authorizedGrantTypes("password,authorization_code,refresh_token,implicit".split(","))
//					//两个小时有效
//					.accessTokenValiditySeconds(7200)
//					//一个月有效期
//					.refreshTokenValiditySeconds(2592000)
//					//Token 类型
//					.additionalInformation(additionalInformation);
//		}
//
//		//自己也是一个ResourceServer 添加自己
//		if (!this.clientService.clientExists(DEFAULT_AUTH_CLIENT)) {
//			builder.withClient(DEFAULT_AUTH_CLIENT).secret(DEFAULT_AUTH_CLIENT)
//					.scopes("all")
//					.authorizedGrantTypes("client_credentials".split(","))
//					//两个小时有效
//					.accessTokenValiditySeconds(7200)
//					//一个月有效期
//					.refreshTokenValiditySeconds(2592000)
//					//Token 类型
//					.additionalInformation(additionalInformation);
//		}
//
//	}
//
//
//	@Override
//	public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
//		endpoints.authenticationManager(this.authenticationManager);
//		//动态Token Service
//		endpoints.tokenServices(this.tokenService());
//	}
//
//	@Override
//	public void configure(final AuthorizationServerSecurityConfigurer security) {
//		security.allowFormAuthenticationForClients()
//				.tokenKeyAccess("permitAll()")
//				.checkTokenAccess("isAuthenticated()")
//				.passwordEncoder(this.passwordEncoder);
//	}
//
//	@Bean
//	public AuthorizationServerTokenServices tokenService() {
//		return new DynamicTokenService(this.clientDetailsService, this.jwtTokenStore(), this.jdbcTokenStore(),
//				this.authenticationManager,
//				this.jwtAccessTokenConverter());
//	}
//
//	@Bean
//	@Primary
//	public JwtTokenStore jwtTokenStore() {
//		return new JwtTokenStore(this.jwtAccessTokenConverter());
//	}
//
//	@Bean
//	public JwtAccessTokenConverter jwtAccessTokenConverter() {
//		final String keyValue = this.authorizationServerProperties.getJwt().getKeyValue();
//		Assert.notNull(this.authorizationServerProperties.getJwt().getKeyValue(), "keyValue cannot be null");
//		final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//		if (!keyValue.startsWith("-----BEGIN")) {
//			converter.setSigningKey(keyValue);
//		}
//
//		converter.setVerifierKey(keyValue);
//		return converter;
//	}
//
//	@Bean
//	public JdbcTokenStore jdbcTokenStore() {
//		return new JdbcTokenStore(this.dataSource);
//	}
//
//
//}
//
