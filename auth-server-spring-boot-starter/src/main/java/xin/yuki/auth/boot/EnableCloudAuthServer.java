package xin.yuki.auth.boot;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
		CloudAuthAutoConfiguration.class, AuthenticationSecurityConfiguration.class,
		AuthorizationSecurityConfiguration.class})
public @interface EnableCloudAuthServer {
}
