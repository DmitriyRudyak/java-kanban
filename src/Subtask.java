public class Subtask extends Task {
	public Subtask(String name, String description, Status status) {
		super(name, description, status);
	}

	@Override
	public String toString() {
		return "Subtask{" +
				"name='" + name + '\'' +
				", description='" + description + '\'' +
				", status=" + status +
				", ID=" + ID +
				'}';
	}
}
