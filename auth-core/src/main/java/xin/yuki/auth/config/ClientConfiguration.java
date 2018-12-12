package xin.yuki.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xin.yuki.auth.repository.ClientRepository;
import xin.yuki.auth.service.ClientService;
import xin.yuki.auth.service.impl.ClientServiceImpl;

@Configuration
public class ClientConfiguration {


	@Bean
	public ClientService clientService(final ClientRepository clientRepository) {
		return new ClientServiceImpl(clientRepository);
	}

}
