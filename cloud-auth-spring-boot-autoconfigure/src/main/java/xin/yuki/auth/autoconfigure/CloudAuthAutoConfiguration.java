package xin.yuki.auth.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.mapper.autoconfigure.MapperAutoConfiguration;
import tk.mybatis.mapper.autoconfigure.MybatisProperties;
import tk.mybatis.spring.annotation.MapperScan;
import xin.yuki.auth.core.properties.CloudAuthServerProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhang
 */
@Configuration
@MapperScan(basePackages = "xin.yuki.auth.core.mapper")
@AutoConfigureBefore(MapperAutoConfiguration.class)
@EnableConfigurationProperties(CloudAuthServerProperties.class)
public class CloudAuthAutoConfiguration {


	public CloudAuthAutoConfiguration(final MybatisProperties mybatisProperties) {

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