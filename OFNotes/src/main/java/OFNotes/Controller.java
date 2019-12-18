package OFNotes;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import java.security.Principal;

@CrossOrigin(origins = {"${server.cors_origin}"})
@RestController
public class Controller
{
	private static Model model;
	@Autowired
	private BuildProperties buildProperties;
	static
	{
		model = new Model();
	}
	//About
	@RequestMapping("/")
	public String aboutApplication()
	{
		return "OFNotes " + this.buildProperties.getVersion() +
		"<br>Author: Arkadiusz97";
	}
	//Create
	@PostMapping("/user")
	public ResponseEntity<String> createUser(@RequestParam(value="login") String login,
	@RequestParam(value="password") String password, Principal principal)
	{
		try
		{
			User user = model.getUserByName(principal.getName());
			model.createUser(login, password, user.getId());
		}
		catch(Model.InternalError internalError)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalError.getMessage());
		}
		catch(Model.Unauthorized unauthorized)
		{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(unauthorized.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}
	@PostMapping("/note")
	public ResponseEntity<String> createNote(@RequestParam(value="name", defaultValue="") String name,
	@RequestParam(value="note", defaultValue="") String note, Principal principal)
	{
		try
		{
			User user = model.getUserByName(principal.getName());
			model.createNote(name, note, user.getId());
		}
		catch(Model.InternalError internalError)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalError.getMessage());
		}
		catch(Model.Unauthorized unauthorized)
		{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(unauthorized.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}
	//read
	@RequestMapping("/user")
	public ResponseEntity<?> getCurrentUser(Principal principal)
	{
		java.util.List users;
		try
		{
			User user = model.getUserByName(principal.getName());
			users = model.getUsers(user.getId(), user.getId());
		}
		catch(Model.InternalError internalError)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalError.getMessage());
		}
		catch(Model.Unauthorized unauthorized)
		{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(unauthorized.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(users.get(0));
	}
	@RequestMapping("/user/{id}")
	public ResponseEntity<?> getUser(@PathVariable int id, Principal principal)
	{
		java.util.List users;
		try
		{
			User user = model.getUserByName(principal.getName());
			users = model.getUsers(id, user.getId());
		}
		catch(Model.InternalError internalError)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalError.getMessage());
		}
		catch(Model.Unauthorized unauthorized)
		{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(unauthorized.getMessage());
		}
		if(id == 0)
			return ResponseEntity.status(HttpStatus.OK).body(users);
		else
			return ResponseEntity.status(HttpStatus.OK).body(users.get(0));
	}
	@RequestMapping("/searchusers/{loginLike}")
	public ResponseEntity<?> SearchUsers(@PathVariable String loginLike, Principal principal)
	{
		java.util.List users;
		try
		{
			User user = model.getUserByName(principal.getName());
			users = model.searchUsers(loginLike, user.getId());
		}
		catch(Model.InternalError internalError)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalError.getMessage());
		}
		catch(Model.Unauthorized unauthorized)
		{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(unauthorized.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}
	@RequestMapping("/note/{id}")
	public ResponseEntity<?> getNote(@PathVariable int id, Principal principal)
	{
		java.util.List notes;
		try
		{
			User user = model.getUserByName(principal.getName());
			notes = model.getNotes(id, user.getId());
		}
		catch(Model.InternalError internalError)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalError.getMessage());
		}
		if(id == 0)
			return ResponseEntity.status(HttpStatus.OK).body(notes);
		else
			return ResponseEntity.status(HttpStatus.OK).body(notes.get(0));
	}
	@RequestMapping("/searchnotes/{nameOrNoteLike}")
	public ResponseEntity<?> searchNotes(@PathVariable String nameOrNoteLike, Principal principal)
	{
		java.util.List notes;
		try
		{
			User user = model.getUserByName(principal.getName());
			notes = model.searchNotes(nameOrNoteLike, user.getId());
		}
		catch(Model.InternalError internalError)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalError.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(notes);
	}
	@RequestMapping("/setups")
	public ResponseEntity<?> getSetups(Principal principal)
	{
		java.util.List setups;
		try
		{
			setups = model.getSetups();
		}
		catch(Model.InternalError internalError)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalError.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(setups);
	}
	//Update
	@PutMapping("/user/{id}")
	public ResponseEntity<String> updateUser(@PathVariable int id,
	@RequestParam(value="login", defaultValue="") String login,
	@RequestParam(value="password", defaultValue="") String password
	, Principal principal)
	{
		try
		{
			User user = model.getUserByName(principal.getName());
			model.updateUser(id, login, password, user.getId());
		}
		catch(Model.InternalError internalError)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalError.getMessage());
		}
		catch(Model.Unauthorized unauthorized)
		{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(unauthorized.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	@PutMapping("/note/{id}")
	public ResponseEntity<String> updateNote(@PathVariable int id,
	@RequestParam(value="name", defaultValue="") String name,
	@RequestParam(value="note", defaultValue="") String note
	, Principal principal)
	{
		try
		{
			User user = model.getUserByName(principal.getName());
			model.updateNote(id, name, note, user.getId());
		}
		catch(Model.InternalError internalError)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalError.getMessage());
		}
		catch(Model.Unauthorized unauthorized)
		{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(unauthorized.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	@PutMapping("/setups")
	public ResponseEntity<String> updateSetups(
	@RequestParam(value="max_name_length", defaultValue = "0") int maxNameLength,
	@RequestParam(value="max_note_length", defaultValue = "0") int maxNoteLength,
	@RequestParam(value="max_note_count", defaultValue = "0") int maxNoteCount
	, Principal principal)
	{
		try
		{
			User user = model.getUserByName(principal.getName());
			model.updateSetups(maxNameLength, maxNoteLength, maxNoteCount, user.getId());
		}
		catch(Model.InternalError internalError)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalError.getMessage());
		}
		catch(Model.Unauthorized unauthorized)
		{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(unauthorized.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	//Delete
	@DeleteMapping("/user")
	public ResponseEntity<String> removeUser(@RequestParam(value="id") int id, Principal principal)
	{
		try
		{
			User user = model.getUserByName(principal.getName());
			model.deleteUser(id, user.getId());
		}
		catch(Model.InternalError internalError)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalError.getMessage());
		}
		catch(Model.Unauthorized unauthorized)
		{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(unauthorized.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	@DeleteMapping("/note")
	public ResponseEntity<String> removeNote(@RequestParam(value="id") int id, Principal principal)
	{
		try
		{
			User user = model.getUserByName(principal.getName());
			model.deleteNote(id, user.getId());
		}
		catch(Model.InternalError internalError)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalError.getMessage());
		}
		catch(Model.Unauthorized unauthorized)
		{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(unauthorized.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
}
