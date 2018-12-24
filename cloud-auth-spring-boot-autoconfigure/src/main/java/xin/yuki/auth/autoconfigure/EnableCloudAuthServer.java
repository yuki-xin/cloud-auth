package xin.yuki.auth.autoconfigure;


import org.springframework.context.annotation.Import;

@Import({AuthenticationSecurityConfiguration.class, AuthorizationSecurityConfiguration.class
})
public @interface EnableCloudAuthServer {
}
