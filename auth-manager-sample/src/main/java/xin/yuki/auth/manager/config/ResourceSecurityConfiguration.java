package xin.yuki.auth.manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import xin.yuki.auth.core.config.ClientConfiguration;
import xin.yuki.auth.core.config.CoreConfiguration;
import xin.yuki.auth.core.service.ClientService;

import javax.sql.DataSource;

/**
 * @Title AuthorizationSecurityConfig
 * @Description OAuth2授权配置
 * @Author ZQian
 * @date: 2018/11/21 16:45
 */
@Configuration
@EnableResourceServer
@Import({ClientConfiguration.class, CoreConfiguration.class})
public class ResourceSecurityConfiguration extends ResourceServerConfigurerAdapter {

	private static final String DEFAULT_CLIENT = "client";

	private static final String DEFAULT_AUTH_CLIENT = "auth-server";

	private final AuthenticationManager authenticationManager;


	private final OAuth2ClientProperties oAuth2ClientProperties;

	private final DataSource dataSource;


	@Autowired
	public ResourceSecurityConfiguration(final AuthenticationConfiguration authenticationConfiguration,
	                                     final OAuth2ClientProperties oAuth2ClientProperties,
	                                     final DataSource dataSource,
	                                     final ClientService clientService) throws Exception {
		this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
		this.oAuth2ClientProperties = oAuth2ClientProperties;
		this.dataSource = dataSource;
	}


	@Override
	public void configure(final ResourceServerSecurityConfigurer resources) throws Exception {
		super.configure(resources);
	}

	@Override
	public void configure(final HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/error").permitAll()
				.antMatchers("/test").hasAnyAuthority("admin")
				.anyRequest().authenticated()
				.and()
				.headers().frameOptions().disable()
				.and()
				.csrf().disable();
	}
}

