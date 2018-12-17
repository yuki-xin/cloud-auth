package xin.yuki.auth.core.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import tk.mybatis.mapper.autoconfigure.MapperAutoConfiguration;
import tk.mybatis.mapper.autoconfigure.MybatisProperties;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhang
 */
@MapperScan(basePackages = "xin.yuki.auth.core.mapper")
@PropertySource("auth-core.properties")
@Configuration
@AutoConfigureBefore(MapperAutoConfiguration.class)
public class AuthServerCoreConfiguration {


	public AuthServerCoreConfiguration(final MybatisProperties mybatisProperties) {

		//添加Mybatis配置属性
		if (mybatisProperties.getMapperLocations() == null) {
			mybatisProperties.setMapperLocations(new String[]{"classpath:cloud-auth/mapper/*.xml"});
		} else {
			final List<String> locations = new ArrayList<>(Arrays.asList(mybatisProperties.getMapperLocations()));
			locations.add("classpath:cloud-auth/mapper/*.xml");
			mybatisProperties.setMapperLocations(locations.toArray(new String[0]));
		}

	}


}
