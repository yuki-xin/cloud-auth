package xin.yuki.auth.boot;


import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({AuthResourceSecurityConfiguration.class, ResourceServerConfiguration.class})
public @interface EnableCloudAuthClient {
}
