package xin.yuki.auth.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import xin.yuki.auth.boot.EnableCloudAuthClient;

@SpringBootApplication
@EnableCloudAuthClient
public class ManagerSampleApplication {

	public static void main(final String[] args) {
		SpringApplication.run(ManagerSampleApplication.class, args);
	}


}
