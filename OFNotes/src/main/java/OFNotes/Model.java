package OFNotes;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Model
{
	private static Configuration configuration;
	private static SessionFactory factory;
	private static BCryptPasswordEncoder passwordEncoder;
	public class InternalError extends Exception
	{
		private String message;
		public InternalError(String message)
		{
			super();
			this.message = message;
		}
		public String getMessage()
		{
			return this.message;
		}
	}
	public class Unauthorized extends Exception
	{
		private String message;
		public Unauthorized(String message)
		{
			super();
			this.message = message;
		}
		public String getMessage()
		{
			return this.message;
		}
	}
	static
	{
		passwordEncoder = new BCryptPasswordEncoder();
		try
		{
			AbstractApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
			configuration = (Configuration) context.getBean("getConfig");
			factory = configuration.buildSessionFactory();
			context.close();
		}
		catch (Throwable ex)
		{
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
	//Create
	public void createUser(String login, String password, int userId)
	throws Unauthorized, InternalError
	{
		if(userId == 1)
		{
			if(this.getUserByName(login) != null)
				throw new InternalError("User with given login already exists.");
			if(login.isEmpty())
				throw new InternalError("Login is not given.");
			User newUser = new User(login, passwordEncoder.encode(password), java.time.LocalDateTime.now(), java.time.LocalDateTime.now());
			Session session = factory.openSession();
			Transaction tx = null;
			try
			{
				tx = session.beginTransaction();
				session.save(newUser);
				tx.commit();
			}
			catch(HibernateException e)
			{
				if (tx!=null) tx.rollback();
				e.printStackTrace();
				throw new InternalError("Database error.");
		  }
			finally
			{
				session.close();
			}
		}
		else
		{
			throw new Unauthorized("You must be admin to create user.");
		}
	}
	public void createNote(String name, String note, int userId)
	throws Unauthorized, InternalError
	{
		try
		{
			this.checkSettings(name, note, userId);
		}
		catch(InternalError e)
		{
			throw e;
		}
		catch(Unauthorized e)
		{
			throw e;
		}
		Note newNote = new Note(name, note, userId, java.time.LocalDateTime.now(), java.time.LocalDateTime.now());
		Session session = factory.openSession();
		Transaction tx = null;
		try
		{
			tx = session.beginTransaction();
			session.save(newNote);
			tx.commit();
		}
		catch (HibernateException e)
		{
			if (tx!=null) tx.rollback();
			e.printStackTrace();
			throw new InternalError("Database error.");
		}
		finally
		{
			session.close();
		}
	}
	//Read
	public List getUsers(int idToGet, int userId)
	throws Unauthorized, InternalError
	{
		if(userId == 1 || idToGet == userId)
		{
			Session session = factory.openSession();
			Transaction tx = null;
			List<User> usersList;
			Query query = null;
			try
			{
				tx = session.beginTransaction();
				if(idToGet > 0 || userId != 1)
				{
					query = session.createQuery("FROM User WHERE id = :id");
					query.setParameter("id", idToGet);
				}
			  else
			    query = session.createQuery("FROM User");
				tx.commit();
				usersList = query.list();
				if(usersList.isEmpty())
					throw new InternalError("Couldn't find user with given id or users list is empty.");
			}
			catch(HibernateException e)
			{
				if(tx!=null) tx.rollback();
				e.printStackTrace();
				throw new InternalError("Database error.");
			}
			finally
			{
				session.close();
			}
			List<UserToReturn> usersListToReturn = new ArrayList<UserToReturn>();
			for(int i = 0; i < usersList.size(); ++i)
			{
				usersListToReturn.add(new UserToReturn(usersList.get(i).getId(), usersList.get(i).getLogin(), usersList.get(i).getModified(), usersList.get(i).getCreated()));
			}
			return usersListToReturn;
		}
		else
		{
			throw new Unauthorized("You don't have permissions to complete this action.");
		}
	}
	public List searchUsers(String loginLike, int userId)
	throws Unauthorized, InternalError
	{
		if(userId == 1)
		{
			Session session = factory.openSession();
			Transaction tx = null;
			List<User> usersList;
			Query query = null;
			try
			{
				tx = session.beginTransaction();
				query = session.createQuery("FROM User WHERE login like :login");
				query.setParameter("login", "%" + loginLike + "%");
				tx.commit();
				usersList = query.list();
				if(usersList.isEmpty())
					throw new InternalError("Couldn't find user with login containing " +
					loginLike + " or users list is empty.");
			}
			catch(HibernateException e)
			{
				if(tx!=null) tx.rollback();
				e.printStackTrace();
				throw new InternalError("Database error.");
			}
			finally
			{
				session.close();
			}
			List<UserToReturn> usersListToReturn = new ArrayList<UserToReturn>();
			for(int i = 0; i < usersList.size(); ++i)
			{
				usersListToReturn.add(new UserToReturn(usersList.get(i).getId(), usersList.get(i).getLogin(), usersList.get(i).getModified(), usersList.get(i).getCreated()));
			}
			return usersListToReturn;
		}
		else
		{
			throw new Unauthorized("You don't have permissions to complete this action.");
		}
	}
	public User getUserByName(String name)
	{
		Session session = factory.openSession();
		Transaction tx = null;
		List usersList = null;
		Query query = null;
		try
		{
			tx = session.beginTransaction();
			query = session.createQuery("FROM User WHERE login = :name");
			query.setParameter("name", name);
			tx.commit();
			usersList = query.list();
		}
		catch(HibernateException e)
		{
			if(tx!=null) tx.rollback();
			e.printStackTrace();
	  }
		finally
		{
			session.close();
		}
		if(usersList.isEmpty())
			return null;
		else
			return (User)usersList.get(0);
	}
	public List getNotes(int id, int userId)
	throws InternalError
	{
		Session session = factory.openSession();
		Transaction tx = null;
		List notesList = null;
		Query query = null;
		try
		{
			tx = session.beginTransaction();
			if(id > 0)
			{
				query = session.createQuery("FROM Note WHERE id = :id AND user = :userId");
				query.setParameter("id", id);
				query.setParameter("userId", userId);
			}
			else
			{
				query = session.createQuery("FROM Note WHERE user = :userId");
				query.setParameter("userId", userId);
			}
			tx.commit();
			notesList = query.list();
		}
		catch(HibernateException e)
		{
			if(tx!=null) tx.rollback();
			e.printStackTrace();
			throw new InternalError("Database error.");
		}
		finally
		{
			session.close();
		}
		return notesList;
	}
	public List searchNotes(String nameOrNoteLike, int userId)
	throws InternalError
	{
		Session session = factory.openSession();
		Transaction tx = null;
		List notesList = null;
		Query query = null;
		try
		{
			tx = session.beginTransaction();
			query = session.createQuery("FROM Note WHERE user = :user AND" +
			"(name like :nameOrNoteLike OR note like :nameOrNoteLike)");
			query.setParameter("user", userId);
			query.setParameter("nameOrNoteLike", "%" + nameOrNoteLike + "%");
			tx.commit();
			notesList = query.list();
			if(notesList.isEmpty())
				throw new InternalError("Couldn't find notes containing \""
			 	+ nameOrNoteLike + "\" or notes list is empty.");
		}
		catch(HibernateException e)
		{
			if(tx!=null) tx.rollback();
			e.printStackTrace();
			throw new InternalError("Database error.");
		}
		finally
		{
			session.close();
		}
		return notesList;
	}
	public List getSetups()
	throws InternalError
	{
		Session session = factory.openSession();
		Transaction tx = null;
		List<Setup> setupsList;
		try
		{
			tx = session.beginTransaction();
			setupsList = session.createQuery("FROM Setup").list();
			tx.commit();
		}
		catch(HibernateException e)
		{
			if(tx!=null) tx.rollback();
			e.printStackTrace();
			throw new InternalError("Database error.");
		}
		finally
		{
			session.close();
		}
		return setupsList;
	}
	//update
	public void updateUser(Integer idToGet, String login, String password, int userId)
	throws Unauthorized, InternalError
	{
		if(userId == 1 || userId == idToGet)
		{
			Session session = factory.openSession();
			Transaction tx = null;
			try
			{
				tx = session.beginTransaction();
				User user = (User)session.get(User.class, idToGet);
				if(user == null)
					throw new InternalError("Couldn't find user with given id.");
				if(!login.isBlank())
					user.setLogin(login);
				user.setPassword(passwordEncoder.encode(password));
				user.setModified(java.time.LocalDateTime.now());
				session.update(user);
				tx.commit();
			}
			catch(HibernateException e)
			{
				if(tx!=null) tx.rollback();
				e.printStackTrace();
				throw new InternalError("Database error.");
			}
			finally
			{
				session.close();
			}
		}
		else
		{
			throw new Unauthorized("You must be admin or user with given id.");
		}
	}
	public void updateNote(Integer id, String name, String note, int userId)
	throws InternalError, Unauthorized
	{
		try
		{
			this.checkSettings(name, note, userId);
		}
		catch(InternalError e)
		{
			throw e;
		}
		catch(Unauthorized e)
		{
			throw e;
		}
		Session session = factory.openSession();
		Transaction tx = null;
		try
		{
			tx = session.beginTransaction();
			Note noteObj = (Note)session.get(Note.class, id);
			if(noteObj == null)
				throw new InternalError("Couldn't find note with given id.");
			if(noteObj.getUserId() != userId)
			{
				tx.rollback();
				throw new Unauthorized("This note is not your.");
			}
			if(!name.isEmpty())
				noteObj.setName(name);
			if(!note.isEmpty())
				noteObj.setNote(note);
			noteObj.setModified(java.time.LocalDateTime.now());
			session.update(noteObj);
			tx.commit();
		}
		catch(HibernateException e)
		{
			if (tx!=null) tx.rollback();
			e.printStackTrace();
			throw new InternalError("Database error.");
		}
		finally
		{
			session.close();
		}
	}
	public void updateSetups(int maxNameLength, int maxNoteLength, int maxNoteCount, int userId)
	throws InternalError, Unauthorized
	{
		if(userId > 1)
		{
			throw new Unauthorized("You must be admin to change setups.");
		}
		List<Setup> setups = null;
		Setup sMaxNameLength = null, sMaxNoteLength = null, sMaxNoteCount = null;
		try
		{
			setups = this.getSetups();
		}
		catch(InternalError e)
		{
			throw e;
		}
		for(Setup i : setups)
		{
			if(i.getId() == 1 && maxNameLength > 0)
			{
				sMaxNameLength = i;
				sMaxNameLength.setValue(maxNameLength);
			}
			if(i.getId() == 2 && maxNoteLength > 0)
			{
				sMaxNoteLength = i;
				sMaxNoteLength.setValue(maxNoteLength);
			}
			if(i.getId() == 3 && maxNoteCount > 0)
			{
				sMaxNoteCount = i;
				sMaxNoteCount.setValue(maxNoteCount);
			}
		}
		Session session = factory.openSession();
		Transaction tx = null;
		try
		{
			tx = session.beginTransaction();
			if(sMaxNameLength != null)
				session.update(sMaxNameLength);
			if(sMaxNoteLength != null)
				session.update(sMaxNoteLength);
			if(sMaxNoteCount != null)
				session.update(sMaxNoteCount);
			tx.commit();
		}
		catch(HibernateException e)
		{
			if (tx!=null) tx.rollback();
			e.printStackTrace();
			throw new InternalError("Database error.");
		}
		finally
		{
			session.close();
		}
	}
	//delete
	void deleteUser(int id, int userId)
	throws Unauthorized, InternalError
	{
		if(userId == 1)
		{
			if(id == 1)
				throw new InternalError("You can't remove admin account.");
			Session session = factory.openSession();
			Transaction tx = null;
			try
			{
				tx = session.beginTransaction();
				User user = (User)session.get(User.class, id);
				if(user == null)
					throw new InternalError("Couldn't find user with given id.");
				session.delete(user);
				tx.commit();
			}
			catch(HibernateException e)
			{
				if(tx!=null) tx.rollback();
				e.printStackTrace();
				throw new InternalError("Database error.");
			}
			finally
			{
				session.close();
			}
		}
		else
		{
			throw new Unauthorized("You must be admin to remove user.");
		}
	}
	void deleteNote(int id, int userId)
	throws Unauthorized, InternalError
	{
		Session session = factory.openSession();
		Transaction tx = null;
		try
		{
			tx = session.beginTransaction();
			Note note = (Note)session.get(Note.class, id);
			if(note == null)
				throw new InternalError("Couldn't find note with given id.");
			if(note.getUserId() != userId)
			{
				tx.rollback();
				throw new Unauthorized("This note is not your.");
			}
			session.delete(note);
			tx.commit();
		}
		catch (HibernateException e)
		{
			if (tx!=null) tx.rollback();
			e.printStackTrace();
			throw new InternalError("Database error.");
		}
		finally
		{
			session.close();
		}
	}
	//other
	void checkSettings(String name, String note, int userId)
	throws Unauthorized, InternalError
	{
		int numberOfNotes = 0, foundedSettings = 0;
		List<Setup> setups;
		try
		{
			setups = this.getSetups();
			numberOfNotes = this.getNotes(0, userId).size();
		}
		catch(InternalError e)
		{
			throw e;
		}
		for(Setup i : setups)
		{
			if(i.getId() == 1)
			{
				if(name.length() > i.getValue())
				{
					throw new Unauthorized("Note name must have " + Integer.toString(i.getValue()) + " characters or less.");
				}
				foundedSettings++;
			}
			if(i.getId() == 2)
			{
				if(note.length() > i.getValue())
				{
					throw new Unauthorized("Note content must have " + Integer.toString(i.getValue()) + " characters or less.");
				}
				foundedSettings++;
			}
			if(i.getId() == 3)
			{
				if(numberOfNotes >= i.getValue())
				{
					throw new Unauthorized("You can't have more than " + Integer.toString(i.getValue()) + " notes.");
				}
				foundedSettings++;
			}
		}
		if(foundedSettings < 3)
		{
			throw new InternalError("Couldn't find all setups.");
		}
	}
}
