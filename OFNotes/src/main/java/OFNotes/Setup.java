package OFNotes;

public class Setup
{
	private int id, value;
	private String name;
	
	public Setup() {}
	Setup(int id, int value, String name)
	{
		this.id = id;
		this.value = value;
		this.name = name;
	}
	public int getId()
	{
		return id;
	}
	public int getValue()
	{
		return value;
	}
	public String getName()
	{
		return name;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public void setValue(int value)
	{
		this.value = value;
	}
	public void setName(String name)
	{
		this.name = name;
	}
}