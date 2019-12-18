package OFNotes;

import java.time.LocalDateTime;

public class UserToReturn
{
	private int id;
	private String login;
	private LocalDateTime modified, created;
	
	public UserToReturn(int id, String login, LocalDateTime modified, LocalDateTime created)
	{
		this.id = id;
		this.login = login;
		this.modified = modified;
		this.created = created;
	}
	public int getId()
	{
		return id;
	}
	public String getLogin()
	{
		return login;
	}
	public LocalDateTime getModified()
	{
		return this.modified;
	}
	public LocalDateTime getCreated()
	{
		return this.created;
	}
}