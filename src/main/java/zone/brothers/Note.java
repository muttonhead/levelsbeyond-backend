package zone.brothers;

public class Note {
	private int id;
	private String body;

	public Note() {
	}

	public Note(String body) {
		this.body = body;
	}

	public Note(int id, String body) {
		this.id = id;
		this.body = body;
	}

	public int getID() {
		return this.id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
