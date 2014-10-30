package bigws.todo;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.XmlElement;

import todo.Note;
import todo.NoteList;

@WebService
public class ToDoService {
	private NoteList notes;

	public ToDoService() {
		notes = new NoteList();
	}

	@WebMethod()
	public String addNote(String task, String context, String project, int priority) {
		notes.add(new Note(task, context, project, priority));
		return "Ok";
	}

	@XmlElement(name="Note", required=true) 
	@WebMethod()
	public List<Note> listarNotas() {
		return notes.getAllNotes().toString();
	}
	
	@WebMethod()
	public String sayHello(String name) {
		return name + " " + notes.num();
	}
}