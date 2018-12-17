package xin.yuki.auth.autoconfigure;


import org.springframework.context.annotation.Import;
import xin.yuki.auth.server.config.AuthenticationSecurityConfiguration;
import xin.yuki.auth.server.config.AuthorizationSecurityConfiguration;

@Import({AuthenticationSecurityConfiguration.class, AuthorizationSecurityConfiguration.class})
public @interface EnableCloudAuthClient {
}
