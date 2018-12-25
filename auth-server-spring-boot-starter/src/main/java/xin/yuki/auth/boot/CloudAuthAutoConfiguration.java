package xin.yuki.auth.boot;

import tk.mybatis.mapper.autoconfigure.MybatisProperties;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhang
 */
@MapperScan(basePackages = "xin.yuki.auth.core.mapper")
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
