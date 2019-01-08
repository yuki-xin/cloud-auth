package xin.yuki.auth.boot;


import org.springframework.context.annotation.Import;
import xin.yuki.auth.core.bean.AuthMapperScannerRegistrar;
import xin.yuki.auth.core.config.CloudAuthConfiguration;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({AuthMapperScannerRegistrar.class, CloudAuthConfiguration.class, AuthenticationSecurityConfiguration.class,
		AuthorizationSecurityConfiguration.class})
public @interface EnableCloudAuthServer {
}
