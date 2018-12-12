package xin.yuki.auth.core.service.impl;

import xin.yuki.auth.core.entity.oauth.OauthClient;
import xin.yuki.auth.core.repository.ClientRepository;
import xin.yuki.auth.core.service.ClientService;

public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(final ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    @Override
    public OauthClient getByClientId(final String clientId) {
	    return this.clientRepository.getOne(clientId);
    }

    @Override
    public Boolean clientExists(final String clientId) {
	    return this.clientRepository.existsByClientId(clientId);
    }
}
