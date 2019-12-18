package OFNotes;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.time.LocalDateTime;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class User implements UserDetails
{
	private int id;
	private String login, password;
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime modified, created;
	public User() {}
	public User(String login, String password, LocalDateTime modified, LocalDateTime created)
	{
		this.login = login;
		this.password = password;
		this.modified = modified;
		this.created = created;
	}
	public int getId()
	{
		return this.id;
	}
	public String getLogin()
	{
		return this.login;
	}
	public String getPassword()
	{
		return this.password;
	}
	public LocalDateTime getModified()
	{
		return this.modified;
	}
	public LocalDateTime getCreated()
	{
		return this.created;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public void setLogin(String login)
	{
		this.login = login;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public void setModified(LocalDateTime modified)
	{
		this.modified = modified;
	}
	public void setCreated(LocalDateTime created)
	{
		this.created = created;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() 
	{
		// TODO Auto-generated method stub
		return Arrays.asList(new SimpleGrantedAuthority("USER"));
	}
	@Override
	public String getUsername()
	{
		// TODO Auto-generated method stub
		return this.login;
	}
	@Override
	public boolean isAccountNonExpired() 
	{
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() 
	{
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() 
	{
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled() 
	{
		// TODO Auto-generated method stub
		return true;
	}
}