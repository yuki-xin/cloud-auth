package xin.yuki.auth.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author zhang
 */
@MapperScan(basePackages = "xin.yuki.auth.core.mapper")
@PropertySource("mybatis.properties")
@Configuration
public class CoreConfiguration {


}
