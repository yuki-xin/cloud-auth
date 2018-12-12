package xin.yuki.auth.core.service;

import xin.yuki.auth.core.entity.oauth.OauthClient;

public interface ClientService{

	/**
	 * Get Client By Id
	 * @param clientId
	 * @return
	 */
	OauthClient getByClientId(String clientId);


	Boolean clientExists(String clientId);

}
