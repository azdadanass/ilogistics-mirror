package ma.azdad;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.google.common.collect.ImmutableList;

import ma.azdad.model.Role;

@SuppressWarnings("deprecation")
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;

	@Autowired
	UserDetailsService userDetailsService;

	@Value("${applicationCode}")
	public String applicationCode;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors();
		http.csrf().disable();
		http.headers().cacheControl().disable();
		http.headers().frameOptions().disable();
		http.authorizeRequests().antMatchers("/resources/**", "/login.xhtml", "/blank", "/rest/**", "/passwordReset.xhtml", "/.well-known/**").permitAll()
				//
				.antMatchers(getPages("Warehouse", "Brand")).hasRole(Role.ROLE_ILOGISTICS_ADMIN.getRole())
				//
				.antMatchers("/partNumberConfiguration.xhtml").hasRole(Role.ROLE_ILOGISTICS_ADMIN.getRole())
				//
				.antMatchers(getPages("PartNumber")).hasRole(Role.ROLE_ILOGISTICS_SE.getRole())
				//
				.antMatchers("/**").hasRole(applicationCode)
				//
				.and().formLogin().loginPage("/login.xhtml").successForwardUrl("/index.xhtml?faces-redirect=true").defaultSuccessUrl("/index.xhtml?faces-redirect=true", true).failureUrl("/login.xhtml?error1").and().exceptionHandling().accessDeniedPage("/login.xhtml?error2").and().logout().permitAll();

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		String usernameQuery = "select login,password, 1 from users where login=?";
		String rolesQuery = "select b.login,a.role from user_role a,users b where a.user_username = b.username and b.login=?";
		auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(usernameQuery).authoritiesByUsernameQuery(rolesQuery);
	}

	private String[] getPages(String... models) {
		String[] pages = new String[3 * models.length];
		for (int i = 0; i < models.length; i++) {
			String str1 = models[i];
			String str2 = str1.substring(0, 1).toLowerCase() + str1.substring(1);
			pages[i * 3] = "/" + str2 + "List.xhtml";
			pages[i * 3 + 1] = "/addEdit" + str1 + ".xhtml";
			pages[i * 3 + 2] = "/view" + str1 + ".xhtml";
		}
		return pages;
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(ImmutableList.of("*"));
		configuration.setAllowedMethods(ImmutableList.of("GET", "POST", "PUT", "DELETE"));
		configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public static NoOpPasswordEncoder passwordEncoder() {
		return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}

//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
////		auth.userDetailsService(userDetailsService).passwordEncoder(new Md5PasswordEncoder()); // md5 encoding level page 
//		auth.userDetailsService(userDetailsService);
//	}

//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		String usernameQuery = "select username,password, 1 from users where username=?";
//		String rolesQuery = "select a.username,a.authority from authorities a,users b where a.username = a.username and a.username=?";
//		auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(usernameQuery).authoritiesByUsernameQuery(rolesQuery).passwordEncoder(new Md5PasswordEncoder());
//	}

}