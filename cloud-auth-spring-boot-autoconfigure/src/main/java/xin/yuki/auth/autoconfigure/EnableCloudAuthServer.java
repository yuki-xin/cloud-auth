package xin.yuki.auth.autoconfigure;


import org.springframework.context.annotation.Import;
import xin.yuki.auth.client.config.ResourceServerSecurityConfiguration;
import xin.yuki.auth.core.config.AuthClientCoreConfiguration;

@Import({ResourceServerSecurityConfiguration.class, AuthClientCoreConfiguration.class})
public @interface EnableCloudAuthServer {
}
