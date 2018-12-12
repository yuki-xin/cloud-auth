package xin.yuki.auth.manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xin.yuki.auth.manager.repository.ClientRepository;
import xin.yuki.auth.manager.service.ClientService;
import xin.yuki.auth.manager.service.impl.ClientServiceImpl;

@Configuration
public class ClientConfiguration {


	@Bean
	public ClientService clientService(final ClientRepository clientRepository) {
		return new ClientServiceImpl(clientRepository);
	}

}
