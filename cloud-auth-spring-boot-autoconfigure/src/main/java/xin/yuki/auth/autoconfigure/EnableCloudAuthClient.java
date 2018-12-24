package xin.yuki.auth.autoconfigure;


import org.springframework.context.annotation.Import;


@Import({ClientSecurityConfiguration.class})
public @interface EnableCloudAuthClient {
}
