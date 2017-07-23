package zone.brothers;

import java.sql.SQLException;
import java.util.List;
import org.testng.annotations.*;
import zone.brothers.*;

import static org.testng.Assert.*;

public class NoteServiceTest {
	private static final String body = "Test Note";

	private NoteService noteService;

	@BeforeTest
	public void setup() {
		this.noteService = new NoteService();
	}

	@Test
	public void createNoteTest() throws SQLException {
		Note note = new Note(body);
		note = this.noteService.createNote(note);
		assertEquals(note.getID(), 1);
		assertEquals(note.getBody(), body);
	}

	@Test(dependsOnMethods = { "createNoteTest" })
	public void getAllNotesTest() throws SQLException {
		List<Note> notes = this.noteService.getAllNotes();
		assertEquals(notes.size(), 1);
		Note note = notes.get(0);
		assertEquals(note.getID(), 1);
		assertEquals(note.getBody(), body);
	}

	@Test(dependsOnMethods = { "createNoteTest" })
	public void getNoteTest() throws SQLException {
		Note note = this.noteService.getNote(1);
		assertEquals(note.getID(), 1);
		assertEquals(note.getBody(), body);
	}

	@Test
	public void getMissingNoteTest() throws SQLException {
		Note note = this.noteService.getNote(2);
		assertEquals(note, null);
	}

	@Test(dependsOnMethods = { "createNoteTest" })
	public void getNotesTest() throws SQLException {
		List<Note> notes = this.noteService.getNotes("Note");
		assertEquals(notes.size(), 1);
		Note note = notes.get(0);
		assertEquals(note.getID(), 1);
		assertEquals(note.getBody(), body);
	}

	@Test(dependsOnMethods = { "createNoteTest" })
	public void getNoNotesTest() throws SQLException {
		List<Note> notes = this.noteService.getNotes("Boosh");
		assertEquals(notes.size(), 0);
	}
}
