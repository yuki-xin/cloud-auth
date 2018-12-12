package xin.yuki.auth.service;

import xin.yuki.auth.entity.oauth.OauthClient;

public interface ClientService{

	/**
	 * Get Client By Id
	 * @param clientId
	 * @return
	 */
	OauthClient getByClientId(String clientId);


	Boolean clientExists(String clientId);

}
