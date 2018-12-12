package xin.yuki.auth.server.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DynamicTokenGranter extends CompositeTokenGranter {

	private static final String JWT_TOKEN = "jwt";

	private final ClientDetailsService clientDetailsService;
	private final AuthorizationServerTokenServices jwtTokenService;
	private final AuthorizationServerTokenServices jdbcTokenService;
	private final AuthorizationCodeServices authorizationCodeServices;
	private final AuthenticationManager authenticationManager;

	public DynamicTokenGranter(final ClientDetailsService clientDetailsService,
	                           final AuthorizationServerTokenServices jwtTokenService,
	                           final AuthorizationServerTokenServices jdbcTokenService,
	                           final AuthorizationCodeServices authorizationCodeServices,
	                           final AuthenticationManager authenticationManager) {
		super(Collections.emptyList());
		this.clientDetailsService = clientDetailsService;
		this.jwtTokenService = jwtTokenService;
		this.jdbcTokenService = jdbcTokenService;
		this.authorizationCodeServices = authorizationCodeServices;
		this.authenticationManager = authenticationManager;
	}

	@PostConstruct
	public void init() {
		this.getDefaultTokenGranters().forEach(this::addTokenGranter);

	}

	private List<TokenGranter> getDefaultTokenGranters() {
		final ClientDetailsService clientDetails = this.clientDetailsService;
		final AuthorizationServerTokenServices tokenServices = null;
		final AuthorizationCodeServices authorizationCodeServices = this.authorizationCodeServices;
		final OAuth2RequestFactory requestFactory = new DefaultOAuth2RequestFactory(this.clientDetailsService);
		;
		final List<TokenGranter> tokenGranters = new ArrayList<>();

		final AuthorizationCodeTokenGranter authorizationCode = new AuthorizationCodeTokenGranter(null,
				authorizationCodeServices,
				clientDetails,
				requestFactory) {
			@Override
			protected OAuth2AccessToken getAccessToken(final ClientDetails client, final TokenRequest tokenRequest) {
				final String tokenType = tokenRequest.getRequestParameters().get("tokenType");
				if (StringUtils.isNotEmpty(tokenType) && DynamicTokenGranter.JWT_TOKEN.equals(tokenType)) {
					return DynamicTokenGranter.this.jwtTokenService.createAccessToken(this.getOAuth2Authentication(client, tokenRequest));
				} else {
					return DynamicTokenGranter.this.jdbcTokenService.createAccessToken(this.getOAuth2Authentication(client, tokenRequest));
				}
			}
		};
		tokenGranters.add(authorizationCode);

		final RefreshTokenGranter refreshToken = new RefreshTokenGranter(null, clientDetails, requestFactory) {
			@Override
			protected OAuth2AccessToken getAccessToken(final ClientDetails client, final TokenRequest tokenRequest) {
				final String tokenType = tokenRequest.getRequestParameters().get("tokenType");
				if (StringUtils.isNotEmpty(tokenType) && DynamicTokenGranter.JWT_TOKEN.equals(tokenType)) {
					return DynamicTokenGranter.this.jwtTokenService.createAccessToken(this.getOAuth2Authentication(client, tokenRequest));
				} else {
					return DynamicTokenGranter.this.jdbcTokenService.createAccessToken(this.getOAuth2Authentication(client, tokenRequest));
				}
			}
		};
		tokenGranters.add(refreshToken);

		final ImplicitTokenGranter implicit = new ImplicitTokenGranter(null, clientDetails, requestFactory) {
			@Override
			protected OAuth2AccessToken getAccessToken(final ClientDetails client, final TokenRequest tokenRequest) {
				final String tokenType = tokenRequest.getRequestParameters().get("tokenType");
				if (StringUtils.isNotEmpty(tokenType) && DynamicTokenGranter.JWT_TOKEN.equals(tokenType)) {
					return DynamicTokenGranter.this.jwtTokenService.createAccessToken(this.getOAuth2Authentication(client, tokenRequest));
				} else {
					return DynamicTokenGranter.this.jdbcTokenService.createAccessToken(this.getOAuth2Authentication(client, tokenRequest));
				}
			}
		};
		tokenGranters.add(implicit);
		final ClientCredentialsTokenGranter clientCredentials = new ClientCredentialsTokenGranter(null, clientDetails,
				requestFactory){
			@Override
			protected OAuth2AccessToken getAccessToken(final ClientDetails client, final TokenRequest tokenRequest) {
				final String tokenType = tokenRequest.getRequestParameters().get("tokenType");
				if (StringUtils.isNotEmpty(tokenType) && DynamicTokenGranter.JWT_TOKEN.equals(tokenType)) {
					return DynamicTokenGranter.this.jwtTokenService.createAccessToken(this.getOAuth2Authentication(client, tokenRequest));
				} else {
					return DynamicTokenGranter.this.jdbcTokenService.createAccessToken(this.getOAuth2Authentication(client, tokenRequest));
				}
			}
		};
		tokenGranters.add(clientCredentials);
		if (this.authenticationManager != null) {
			final ResourceOwnerPasswordTokenGranter resourceOwnerPassword =
					new ResourceOwnerPasswordTokenGranter(this.authenticationManager,
							tokenServices,
							clientDetails, requestFactory) {
						@Override
						protected OAuth2AccessToken getAccessToken(final ClientDetails client,
						                                           final TokenRequest tokenRequest) {
							final String tokenType = tokenRequest.getRequestParameters().get("tokenType");
							if (StringUtils.isNotEmpty(tokenType) && DynamicTokenGranter.JWT_TOKEN.equals(tokenType)) {
								return DynamicTokenGranter.this.jwtTokenService.createAccessToken(this.getOAuth2Authentication(client, tokenRequest));
							} else {
								return DynamicTokenGranter.this.jdbcTokenService.createAccessToken(this.getOAuth2Authentication(client, tokenRequest));
							}
						}
					};
			tokenGranters.add(resourceOwnerPassword);
		}

		return tokenGranters;
	}

}
