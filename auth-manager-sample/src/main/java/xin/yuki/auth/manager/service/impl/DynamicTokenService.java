package xin.yuki.auth.manager.service.impl;


import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.UUID;

public class DynamicTokenService implements ResourceServerTokenServices {

	private final DefaultTokenServices defaultTokenServices;
	private final RemoteTokenServices remoteTokenServices;

	public DynamicTokenService(final DefaultTokenServices defaultTokenServices,
	                           final RemoteTokenServices remoteTokenServices) {
		this.defaultTokenServices = defaultTokenServices;
		this.remoteTokenServices = remoteTokenServices;
	}

	@Override
	public OAuth2Authentication loadAuthentication(final String token) throws AuthenticationException,
			InvalidTokenException {
		try {
			UUID.fromString(token);
			return this.remoteTokenServices.loadAuthentication(token);
		} catch (final Exception e) {
			// ignore
		}
		return this.defaultTokenServices.loadAuthentication(token);
	}

	@Override
	public OAuth2AccessToken readAccessToken(final String token) {
		try {
			UUID.fromString(token);
			return this.remoteTokenServices.readAccessToken(token);
		} catch (final Exception e) {
			// ignore
		}
		return this.defaultTokenServices.readAccessToken(token);
	}
}
