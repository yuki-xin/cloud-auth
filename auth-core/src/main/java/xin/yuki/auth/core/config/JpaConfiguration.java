package xin.yuki.auth.core.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author zhang
 */
@EnableJpaRepositories(basePackages = "xin.yuki.auth.core")
@EntityScan(basePackages = "xin.yuki.auth.core")
@Configuration
public class JpaConfiguration {
}
