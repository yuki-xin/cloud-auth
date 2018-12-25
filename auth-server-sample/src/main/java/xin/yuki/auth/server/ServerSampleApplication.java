package xin.yuki.auth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import xin.yuki.auth.boot.EnableCloudAuthServer;

@SpringBootApplication
@EnableCloudAuthServer
public class ServerSampleApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ServerSampleApplication.class, args);
    }
}
