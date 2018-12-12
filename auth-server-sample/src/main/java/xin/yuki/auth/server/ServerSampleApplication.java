package xin.yuki.auth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SpringBootApplication
@RestController
public class ServerSampleApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ServerSampleApplication.class, args);
    }

    @RequestMapping("/user")
    public Principal user(final Principal user) {
        return user;
    }
}
