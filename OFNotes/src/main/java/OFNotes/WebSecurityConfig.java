package OFNotes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter 
{
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http
		.csrf().disable()
		.authorizeRequests().anyRequest().authenticated()
		.and()
		.httpBasic()
		.and()
		.cors();
	}
  
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
	throws Exception
	{
		auth.userDetailsService(new UserDetailsService() 
		{
			@Override
			public UserDetails loadUserByUsername(String username) 
			throws UsernameNotFoundException 
			{
				Model model = new Model();
				return model.getUserByName(username);
			}
		}).passwordEncoder(new BCryptPasswordEncoder());;
	}
}
