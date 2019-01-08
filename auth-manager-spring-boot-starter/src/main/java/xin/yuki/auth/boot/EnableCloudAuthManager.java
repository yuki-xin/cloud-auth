package xin.yuki.auth.boot;


import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import xin.yuki.auth.core.bean.AuthMapperScannerRegistrar;
import xin.yuki.auth.core.config.CloudAuthConfiguration;
import xin.yuki.auth.manager.config.AuthManagerConfiguration;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({CloudAuthConfiguration.class, AuthMapperScannerRegistrar.class, AuthManagerWebSecurityConfiguration.class,
		AuthManagerResourceConfiguration.class,
		ResourceServerConfiguration.class, AuthManagerConfiguration.class})
public @interface EnableCloudAuthManager {
}
