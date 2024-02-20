package TaskPackage;

public class Subtask extends Task {
	private int epicID;

	public Subtask(String name, String description, int epicID) {
		super(name, description);
		this.epicID = epicID;
	}

	public Subtask(String name, String description, Status status, int epicID) {
		super(name, description, status);
		this.epicID = epicID;
	}

	public Subtask(String name, String description, Status status, int epicID, int ID) {
		super(name, description, status, ID);
		this.epicID = epicID;
	}

	@Override
	public String toString() {
		return "Subtask{" +
				"epicID=" + epicID +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", status=" + status +
				", ID=" + ID +
				'}';
	}

	public int getEpicID() {
		return epicID;
	}

	public void setEpicID(int epicID) {
		this.epicID = epicID;
	}
}
