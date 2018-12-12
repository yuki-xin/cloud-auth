package xin.yuki.auth.service.impl;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2RequestValidator;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestValidator;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.*;


public class DynamicTokenEndpoint extends TokenEndpoint {
	private final OAuth2RequestValidator oAuth2RequestValidator = new DefaultOAuth2RequestValidator();
	private final Set<HttpMethod> allowedRequestMethods;

	public DynamicTokenEndpoint() {
		this.allowedRequestMethods = new HashSet(Arrays.asList(HttpMethod.POST));
	}

	@Override
	@RequestMapping(
			value = {"/oauth/token"},
			method = {RequestMethod.GET}
	)
	public ResponseEntity<OAuth2AccessToken> getAccessToken(final Principal principal,
	                                                        @RequestParam final Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
		if (!this.allowedRequestMethods.contains(HttpMethod.GET)) {
			throw new HttpRequestMethodNotSupportedException("GET");
		} else {
			return this.postAccessToken(principal, parameters);
		}
	}

	@RequestMapping(
			value = {"/oauth/token"},
			method = {RequestMethod.POST}
	)
	@Override
	public ResponseEntity<OAuth2AccessToken> postAccessToken(final Principal principal,
	                                                         @RequestParam final Map<String, String> parameters) {
		if (!(principal instanceof Authentication)) {
			throw new InsufficientAuthenticationException("There is no client authentication. Try adding an " +
					"appropriate authentication filter.");
		} else {
			final String clientId = this.getClientId(principal);
			final ClientDetails authenticatedClient = this.getClientDetailsService().loadClientByClientId(clientId);
			final TokenRequest tokenRequest = this.getOAuth2RequestFactory().createTokenRequest(parameters,
					authenticatedClient);
			if (!StringUtils.isEmpty(clientId) && !clientId.equals(tokenRequest.getClientId())) {
				throw new InvalidClientException("Given client ID does not match authenticated client");
			} else {
				if (authenticatedClient != null) {
					this.oAuth2RequestValidator.validateScope(tokenRequest, authenticatedClient);
				}

				if (!StringUtils.hasText(tokenRequest.getGrantType())) {
					throw new InvalidRequestException("Missing grant type");
				} else if ("implicit".equals(tokenRequest.getGrantType())) {
					throw new InvalidGrantException("Implicit grant type not supported from token endpoint");
				} else {
					if (this.isAuthCodeRequest(parameters) && !tokenRequest.getScope().isEmpty()) {
						this.logger.debug("Clearing scope of incoming token request");
						tokenRequest.setScope(Collections.emptySet());
					}

					if (this.isRefreshTokenRequest(parameters)) {
						tokenRequest.setScope(OAuth2Utils.parseParameterList(parameters.get("scope")));
					}

					final OAuth2AccessToken token = this.getTokenGranter().grant(tokenRequest.getGrantType(),
							tokenRequest);
					if (token == null) {
						throw new UnsupportedGrantTypeException("Unsupported grant type: " + tokenRequest.getGrantType());
					} else {
						return this.getResponse(token);
					}
				}
			}
		}
	}


	private ResponseEntity<OAuth2AccessToken> getResponse(final OAuth2AccessToken accessToken) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("Cache-Control", "no-store");
		headers.set("Pragma", "no-cache");
		headers.set("Content-Type", "application/json;charset=UTF-8");
		return new ResponseEntity(accessToken, headers, HttpStatus.OK);
	}

	private boolean isRefreshTokenRequest(final Map<String, String> parameters) {
		return "refresh_token".equals(parameters.get("grant_type")) && parameters.get("refresh_token") != null;
	}

	private boolean isAuthCodeRequest(final Map<String, String> parameters) {
		return "authorization_code".equals(parameters.get("grant_type")) && parameters.get("code") != null;
	}

}
