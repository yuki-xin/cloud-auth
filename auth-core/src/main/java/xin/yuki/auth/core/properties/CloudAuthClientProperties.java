package xin.yuki.auth.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhang
 */
@ConfigurationProperties(
		prefix = "cloud-auth.client"
)
@Data
public class CloudAuthClientProperties {
}
