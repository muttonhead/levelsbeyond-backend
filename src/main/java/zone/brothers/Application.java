package zone.brothers;

import com.google.gson.*;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.*;
import spark.servlet.SparkApplication;
import zone.brothers.*;

import static spark.Spark.*;

public class Application implements SparkApplication {
	private static final Logger log = LoggerFactory.getLogger(Application.class);

	private Gson gson = new GsonBuilder().create();
	private NoteService noteService;

	public static void main(String[] args) {
		new Application().init();
	}

	public Application() {
		this.noteService = new NoteService();
	}

	@Override
	public void init() {
		get("/api/notes/:id", (request, response) -> {
			int id = Integer.parseInt(request.params(":id"));
			log.info("GET /api/notes/{}", id);
			try {
				Note note = this.noteService.getNote(id);
				if (note == null) {
					response.status(404);
					return "404 Not Found";
				}
				return gson.toJson(note);
			} catch (SQLException e) {
				log.error("Error getting note by id", e);
				throw e;
			} 
		});

		get("/api/notes", (request, response) -> {
			String query = request.queryParams("query");
			log.info("GET /api/notes {}", query);
			try {
				List<Note> notes;
				if (query == null) {
					notes = this.noteService.getAllNotes();
				} else {
					notes = this.noteService.getNotes(query);
				}
				if (notes.size() == 0) {
					response.status(404);
					return "404 Not Found";
				}
				return gson.toJson(notes);
			} catch (SQLException e) {
				log.error("Error getting notes", e);
				throw e;
			} 
		});

		post("/api/notes", (request, response) -> {
			String body = request.body();
			Note note = gson.fromJson(body, Note.class);
			log.info("POST /api/notes {}", body);
			try {
				this.noteService.createNote(note);
				return gson.toJson(note);
			} catch (SQLException e) {
				log.error("Error inserting note", e);
				throw e;
			} 
		});
	}
}
