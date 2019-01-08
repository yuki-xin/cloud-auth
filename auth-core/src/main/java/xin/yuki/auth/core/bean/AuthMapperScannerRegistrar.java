package xin.yuki.auth.core.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;
import tk.mybatis.spring.mapper.ClassPathMapperScanner;

import java.util.Collections;

public class AuthMapperScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware,
		EnvironmentAware {
	public static final Logger LOGGER = LoggerFactory.getLogger(AuthMapperScannerRegistrar.class);
	private ResourceLoader resourceLoader;
	private Environment environment;

	public AuthMapperScannerRegistrar() {
	}

	@Override
	public void registerBeanDefinitions(final AnnotationMetadata importingClassMetadata,
	                                    final BeanDefinitionRegistry registry) {
		final ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
		if (this.resourceLoader != null) {
			scanner.setResourceLoader(this.resourceLoader);
		}

		try {
			scanner.setMapperProperties(this.environment);
		} catch (final Exception var14) {
			LOGGER.warn("只有 Spring Boot 环境中可以通过 Environment(配置文件,环境变量,运行参数等方式) 配置通用 Mapper，其他环境请通过 @MapperScan 注解中的 " +
					"mapperHelperRef 或 properties 参数进行配置!如果你使用 tk.mybatis.mapper.session.Configuration 配置的通用 " +
					"Mapper，你可以忽略该错误!", var14);
		}

		scanner.registerFilters();
		scanner.doScan(StringUtils.toStringArray(Collections.singletonList("xin.yuki.auth.core.mapper")));
	}

	@Override
	public void setEnvironment(final Environment environment) {
		this.environment = environment;
	}

	@Override
	public void setResourceLoader(final ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
}
