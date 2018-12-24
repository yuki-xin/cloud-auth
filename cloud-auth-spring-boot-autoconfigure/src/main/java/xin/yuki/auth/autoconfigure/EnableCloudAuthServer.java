package xin.yuki.auth.autoconfigure;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({AuthenticationSecurityConfiguration.class, AuthorizationSecurityConfiguration.class,
		CloudAuthAutoConfiguration.class})
public @interface EnableCloudAuthServer {
}
