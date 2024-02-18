import java.util.Objects;

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

	@Override
	public String toString() {
		return "Task{" +
				"name='" + name + '\'' +
				", description='" + description + '\'' +
				", status=" + status +
				", ID=" + ID +
				'}';
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		Task task = (Task) object;
		System.out.println();
		return Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description, status);
	}
}