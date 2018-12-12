package xin.yuki.auth.manager.service;

import xin.yuki.auth.manager.entity.oauth.OauthClient;

public interface ClientService{

	/**
	 * Get Client By Id
	 * @param clientId
	 * @return
	 */
	OauthClient getByClientId(String clientId);


	Boolean clientExists(String clientId);

}
