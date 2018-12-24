package xin.yuki.auth.autoconfigure;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ResourceSecurityConfiguration.class})
public @interface EnableCloudAuthClient {
}
