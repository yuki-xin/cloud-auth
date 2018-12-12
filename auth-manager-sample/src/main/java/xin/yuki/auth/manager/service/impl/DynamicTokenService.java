//package xin.yuki.auth.service.impl;
//
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.oauth2.common.*;
//import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
//import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
//import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
//import org.springframework.security.oauth2.provider.*;
//import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Date;
//import java.util.Set;
//import java.util.UUID;
//
///**
// * 动态TokenService
// *
// * @author zhang
// */
//public class DynamicTokenService extends DefaultTokenServices {
//
//	private static final String JWT_TOKEN_STORE = "jwt";
//	private final ClientDetailsService clientDetailsService;
//	private final JwtTokenStore jwtTokenStore;
//	private final JwtAccessTokenConverter jwtAccessTokenConverter;
//	private final JdbcTokenStore jdbcTokenStore;
//	private final AuthenticationManager authenticationManager;
//	private final boolean reuseRefreshToken = true;
//
//
//	public DynamicTokenService(final ClientDetailsService clientDetailsService, final JwtTokenStore jwtTokenStore,
//	                           final JdbcTokenStore jdbcTokenStore, final AuthenticationManager authenticationManager,
//	                           final JwtAccessTokenConverter jwtAccessTokenConverter) {
//		this.clientDetailsService = clientDetailsService;
//		this.jwtTokenStore = jwtTokenStore;
//		this.jdbcTokenStore = jdbcTokenStore;
//		this.authenticationManager = authenticationManager;
//		this.jwtAccessTokenConverter = jwtAccessTokenConverter;
//	}
//
//	@Override
//	public void afterPropertiesSet() {
//
//	}
//
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public OAuth2AccessToken createAccessToken(final OAuth2Authentication authentication) throws
// AuthenticationException {
//
//		//获得Client
//
//		final TokenStore tokenStore = this.getDymanicTokenStore(authentication);
//		final OAuth2AccessToken existingAccessToken = tokenStore.getAccessToken(authentication);
//		OAuth2RefreshToken refreshToken = null;
//		if (existingAccessToken != null) {
//			if (!existingAccessToken.isExpired()) {
//				tokenStore.storeAccessToken(existingAccessToken, authentication);
//				return existingAccessToken;
//			}
//
//			if (existingAccessToken.getRefreshToken() != null) {
//				refreshToken = existingAccessToken.getRefreshToken();
//				tokenStore.removeRefreshToken(refreshToken);
//			}
//
//			tokenStore.removeAccessToken(existingAccessToken);
//		}
//
//		if (refreshToken == null) {
//			refreshToken = this.createRefreshToken(authentication);
//		} else if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
//			final ExpiringOAuth2RefreshToken expiring = (ExpiringOAuth2RefreshToken) refreshToken;
//			if (System.currentTimeMillis() > expiring.getExpiration().getTime()) {
//				refreshToken = this.createRefreshToken(authentication);
//			}
//		}
//
//		final OAuth2AccessToken accessToken = this.createAccessToken(authentication, refreshToken);
//		tokenStore.storeAccessToken(accessToken, authentication);
//		refreshToken = accessToken.getRefreshToken();
//		if (refreshToken != null) {
//			tokenStore.storeRefreshToken(refreshToken, authentication);
//		}
//
//		return accessToken;
//	}
//
//
//	@Transactional(
//			noRollbackFor = {InvalidTokenException.class, InvalidGrantException.class}
//	)
//	@Override
//	public OAuth2AccessToken refreshAccessToken(final String refreshTokenValue, final TokenRequest tokenRequest)
// throws AuthenticationException {
//		//JWT不支持refresh token
//		final TokenStore tokenStore = this.jdbcTokenStore;
//		final ClientDetails clientDetails = this.clientDetailsService.loadClientByClientId(tokenRequest.getClientId());
//		if (!this.isSupportRefreshToken(tokenRequest.createOAuth2Request(clientDetails))) {
//			throw new InvalidGrantException("Invalid refresh token: " + refreshTokenValue);
//		} else {
//			OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(refreshTokenValue);
//			if (refreshToken == null) {
//				throw new InvalidGrantException("Invalid refresh token: " + refreshTokenValue);
//			} else {
//				OAuth2Authentication authentication = tokenStore.readAuthenticationForRefreshToken(refreshToken);
//				if (this.authenticationManager != null && !authentication.isClientOnly()) {
//					Authentication user =
//							new PreAuthenticatedAuthenticationToken(authentication.getUserAuthentication(), "",
//									authentication.getAuthorities());
//					user = this.authenticationManager.authenticate(user);
//					final Object details = authentication.getDetails();
//					authentication = new OAuth2Authentication(authentication.getOAuth2Request(), user);
//					authentication.setDetails(details);
//				}
//
//				final String clientId = authentication.getOAuth2Request().getClientId();
//				if (clientId != null && clientId.equals(tokenRequest.getClientId())) {
//					tokenStore.removeAccessTokenUsingRefreshToken(refreshToken);
//					if (this.isExpired(refreshToken)) {
//						tokenStore.removeRefreshToken(refreshToken);
//						throw new InvalidTokenException("Invalid refresh token (expired): " + refreshToken);
//					} else {
//						authentication = this.createRefreshedAuthentication(authentication, tokenRequest);
//						if (!this.reuseRefreshToken) {
//							tokenStore.removeRefreshToken(refreshToken);
//							refreshToken = this.createRefreshToken(authentication);
//						}
//
//						final OAuth2AccessToken accessToken = this.createAccessToken(authentication, refreshToken);
//						tokenStore.storeAccessToken(accessToken, authentication);
//						if (!this.reuseRefreshToken) {
//							tokenStore.storeRefreshToken(accessToken.getRefreshToken(), authentication);
//						}
//
//						return accessToken;
//					}
//				} else {
//					throw new InvalidGrantException("Wrong client for this refresh token: " + refreshTokenValue);
//				}
//			}
//		}
//	}
//
//	@Override
//	public OAuth2AccessToken getAccessToken(final OAuth2Authentication authentication) {
//		return this.getDymanicTokenStore(authentication).getAccessToken(authentication);
//	}
//
//
//	@Override
//	public OAuth2AccessToken readAccessToken(final String accessToken) {
//		return this.getDymanicTokenStore(accessToken).readAccessToken(accessToken);
//	}
//
//	@Override
//	public OAuth2Authentication loadAuthentication(final String accessTokenValue) throws AuthenticationException,
//			InvalidTokenException {
//		final TokenStore tokenStore = this.getDymanicTokenStore(accessTokenValue);
//		final OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenValue);
//		if (accessToken == null) {
//			throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
//		} else if (accessToken.isExpired()) {
//			tokenStore.removeAccessToken(accessToken);
//			throw new InvalidTokenException("Access token expired: " + accessTokenValue);
//		} else {
//			final OAuth2Authentication result = tokenStore.readAuthentication(accessToken);
//			if (result == null) {
//				throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
//			} else {
//				if (this.clientDetailsService != null) {
//					final String clientId = result.getOAuth2Request().getClientId();
//
//					try {
//						this.clientDetailsService.loadClientByClientId(clientId);
//					} catch (final ClientRegistrationException var6) {
//						throw new InvalidTokenException("Client not valid: " + clientId, var6);
//					}
//				}
//
//				return result;
//			}
//		}
//	}
//
//	@Override
//	public String getClientId(final String tokenValue) {
//		final TokenStore tokenStore = this.getDymanicTokenStore(tokenValue);
//		final OAuth2Authentication authentication = tokenStore.readAuthentication(tokenValue);
//		if (authentication == null) {
//			throw new InvalidTokenException("Invalid access token: " + tokenValue);
//		} else {
//			final OAuth2Request clientAuth = authentication.getOAuth2Request();
//			if (clientAuth == null) {
//				throw new InvalidTokenException("Invalid access token (no client id): " + tokenValue);
//			} else {
//				return clientAuth.getClientId();
//			}
//		}
//	}
//
//	@Override
//	public boolean revokeToken(final String tokenValue) {
//		final TokenStore tokenStore = this.getDymanicTokenStore(tokenValue);
//		final OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
//		if (accessToken == null) {
//			return false;
//		} else {
//			if (accessToken.getRefreshToken() != null) {
//				tokenStore.removeRefreshToken(accessToken.getRefreshToken());
//			}
//
//			tokenStore.removeAccessToken(accessToken);
//			return true;
//		}
//	}
//
//	private OAuth2RefreshToken createRefreshToken(final OAuth2Authentication authentication) {
//		if (!this.isSupportRefreshToken(authentication.getOAuth2Request())) {
//			return null;
//		} else {
//			final int validitySeconds = this.getRefreshTokenValiditySeconds(authentication.getOAuth2Request());
//			final String value = UUID.randomUUID().toString();
//			return (validitySeconds > 0 ? new DefaultExpiringOAuth2RefreshToken(value,
//					new Date(System.currentTimeMillis() + (long) validitySeconds * 1000L)) :
//					new DefaultOAuth2RefreshToken(value));
//		}
//	}
//
//
//	private OAuth2AccessToken createAccessToken(final OAuth2Authentication authentication,
//	                                            final OAuth2RefreshToken refreshToken) {
//		final DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
//		final int validitySeconds = this.getAccessTokenValiditySeconds(authentication.getOAuth2Request());
//		if (validitySeconds > 0) {
//			token.setExpiration(new Date(System.currentTimeMillis() + (long) validitySeconds * 1000L));
//		}
//
//		token.setRefreshToken(refreshToken);
//		token.setScope(authentication.getOAuth2Request().getScope());
//		return (this.getDymanicTokenStore(authentication) instanceof JwtTokenStore ?
//				this.jwtAccessTokenConverter.enhance(token, authentication) : token);
//	}
//
//	private TokenStore getDymanicTokenStore(final OAuth2Authentication authentication) {
//		final String clientId = authentication.getOAuth2Request().getClientId();
//		final ClientDetails clientDetails = this.clientDetailsService.loadClientByClientId(clientId);
//		final String tokenType = (String) clientDetails.getAdditionalInformation().get("tokenType");
//		if (JWT_TOKEN_STORE.equals(tokenType)) {
//			return this.jwtTokenStore;
//		} else {
//			return this.jdbcTokenStore;
//		}
//	}
//
//	private TokenStore getDymanicTokenStore(final String tokenValue) {
//		try {
//			UUID.fromString(tokenValue);
//			return this.jdbcTokenStore;
//		} catch (final IllegalArgumentException e) {
//			return this.jwtTokenStore;
//		}
//	}
//
//	private OAuth2Authentication createRefreshedAuthentication(final OAuth2Authentication authentication,
//	                                                           final TokenRequest request) {
//		final Set<String> scope = request.getScope();
//		OAuth2Request clientAuth = authentication.getOAuth2Request().refresh(request);
//		if (scope != null && !scope.isEmpty()) {
//			final Set<String> originalScope = clientAuth.getScope();
//			if (originalScope == null || !originalScope.containsAll(scope)) {
//				throw new InvalidScopeException("Unable to narrow the scope of the client authentication to " + scope
// + ".", originalScope);
//			}
//
//			clientAuth = clientAuth.narrowScope(scope);
//		}
//
//		return new OAuth2Authentication(clientAuth, authentication.getUserAuthentication());
//	}
//
//}
