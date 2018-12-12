package xin.yuki.auth.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xin.yuki.auth.core.repository.ClientRepository;
import xin.yuki.auth.core.service.ClientService;
import xin.yuki.auth.core.service.impl.ClientServiceImpl;

@Configuration
public class ClientConfiguration {


	@Bean
	public ClientService clientService(final ClientRepository clientRepository) {
		return new ClientServiceImpl(clientRepository);
	}

}
