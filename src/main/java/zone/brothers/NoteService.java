package zone.brothers;

import java.sql.*;
import java.util.*;
import org.h2.jdbcx.JdbcConnectionPool;
import org.slf4j.*;

public class NoteService {
	private static final Logger log = LoggerFactory.getLogger(NoteService.class);

	private static final String createNotes = "CREATE TABLE notes(id int auto_increment primary key, body varchar(255))";
	private static final String insertNote = "INSERT INTO notes(body) values (?)";
	private static final String selectAllNotes = "SELECT * FROM notes";
	private static final String selectNoteByID = "SELECT * FROM notes WHERE id = ?";
	private static final String queryNotes = "SELECT * FROM NOTES WHERE body LIKE ?";

	private JdbcConnectionPool connectionPool;

	public NoteService() {
		this.connectionPool = JdbcConnectionPool.create("jdbc:h2:mem:", "sa", "sa");
		this.init();
	}

	public void init() {
		try (Connection connection = this.connectionPool.getConnection()) {
			log.info("Creating notes table");
			PreparedStatement statement = connection.prepareStatement(createNotes);
			statement.executeUpdate();
		} catch (SQLException e) {
			log.error("Exception creating notes table", e);
			throw new RuntimeException(e);
		}
	}

	public Note createNote(Note note) throws SQLException {
		try (Connection connection = this.connectionPool.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(insertNote);
			statement.setString(1, note.getBody());
			statement.executeUpdate();
			ResultSet results = statement.getGeneratedKeys();
			results.first();
			note.setID(results.getInt(1));
			return note;
		}
	}

	public List<Note> getAllNotes() throws SQLException {
		try (Connection connection = this.connectionPool.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(selectAllNotes);
			ResultSet results = statement.executeQuery();
			List<Note> notes = new ArrayList<>();
			while(results.next()) {
				Note note = new Note(results.getInt("id"), results.getString("body"));
				notes.add(note);
			}
			return notes;
		}
	}

	public Note getNote(int id) throws SQLException {
		try (Connection connection = this.connectionPool.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(selectNoteByID);
			statement.setInt(1, id);
			ResultSet results = statement.executeQuery();
			results.first();
			Note note = new Note(results.getInt("id"), results.getString("body"));
			return note;
		} catch (SQLException e) {
			if (e.getSQLState() == "02000") {
				return null;
			} else {
				throw e;
			}
		}
	}

	public List<Note> getNotes(String query) throws SQLException {
		try (Connection connection = this.connectionPool.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(queryNotes);
			statement.setString(1, "%" + query + "%");
			ResultSet results = statement.executeQuery();
			List<Note> notes = new ArrayList<>();
			while(results.next()) {
				Note note = new Note(results.getInt("id"), results.getString("body"));
				notes.add(note);
			}
			return notes;
		}
	}
}
