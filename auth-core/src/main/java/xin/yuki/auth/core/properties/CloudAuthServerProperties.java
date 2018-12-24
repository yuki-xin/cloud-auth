package xin.yuki.auth.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhang
 */
@ConfigurationProperties(
		prefix = "cloud-auth.server"
)
@Data
public class CloudAuthServerProperties {

	private String jwtKey;

	private String username;

	private String password;
}
