public class Task {
	 String name;
	 String description;
	 Status status;
	 int ID;

	public Task(String name, String description, Status status) {
		this.name = name;
		this.description = description;
		this.status = status;
	}

	public Task(String name) {
		this.name = name;
	}

	public Task(String name, String description, Status status, int ID) {
		this.name = name;
		this.description = description;
		this.status = status;
		this.ID = ID;
	}

	@Override
	public String toString() {
		return "Task{" +
				"name='" + name + '\'' +
				", description='" + description + '\'' +
				", status=" + status +
				", ID=" + ID +
				'}';
	}
}