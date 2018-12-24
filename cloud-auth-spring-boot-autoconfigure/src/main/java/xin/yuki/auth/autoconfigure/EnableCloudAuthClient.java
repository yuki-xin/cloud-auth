package xin.yuki.auth.autoconfigure;


import org.springframework.context.annotation.Import;


@Import({ResourceSecurityConfiguration.class})
public @interface EnableCloudAuthClient {
}
