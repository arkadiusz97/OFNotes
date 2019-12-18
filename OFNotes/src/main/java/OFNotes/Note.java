package OFNotes;

import java.time.LocalDateTime;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class Note
{
	private int id, userId;
	private String name, note;
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime modified, created;
	public Note() {}
	public Note(String name, String note, int userId, LocalDateTime modified, LocalDateTime created)
	{
		this.name = name;
		this.note = note;
		this.userId = userId;
		this.modified = modified;
		this.created = created;
	}
	public int getId()
	{
		return this.id;
	}
	public String getName()
	{
		return this.name;
	}
	public String getNote()
	{
		return this.note;
	}
	public int getUserId()
	{
		return userId;
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
	public void setName(String name)
	{
		this.name = name;
	}
	public void setNote(String note)
	{
		this.note = note;
	}
	public void setUserId(int userId)
	{
		this.userId = userId;
	}
	public void setModified(LocalDateTime modified)
	{
		this.modified = modified;
	}
	public void setCreated(LocalDateTime created)
	{
		this.created = created;
	}
}