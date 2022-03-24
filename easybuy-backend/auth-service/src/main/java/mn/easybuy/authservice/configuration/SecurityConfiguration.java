package mn.easybuy.authservice.configuration;

import mn.easybuy.authservice.filter.AuthenticationFilter;
import mn.easybuy.authservice.filter.AuthorizationFilter;
import mn.easybuy.authservice.service.CustomerDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler;

	@Autowired
	CustomerDetailsService userDetailService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeRequests()
				.antMatchers("/","/shop/**","/register","/refresh/**","/error").permitAll()
				.antMatchers("/customer/**","/admin/**").hasAuthority("ROLE_ADMIN")
				.antMatchers("/customer/**").hasAuthority("ROLE_USER")
				.anyRequest()
				.authenticated();

		http.formLogin()
				.failureUrl("/login?error=true")
				.defaultSuccessUrl("/",true)
				.usernameParameter("username")
				.passwordParameter("password");

		http.oauth2Login()
				.loginPage("/login")
				.successHandler(googleOAuth2SuccessHandler);

		http.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login")
				.invalidateHttpSession(true)
				.deleteCookies("remove");

		http.exceptionHandling();

		http.headers().frameOptions().disable();

		http.addFilter(new AuthenticationFilter(authenticationManagerBean()));

		http.addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**", "/images/**", "/productImages/**", "/css/**", "/js/**");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).passwordEncoder(bCryptPasswordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}